package com.atividadeaws.back_end.infrastructure.aws;

import com.atividadeaws.back_end.business.arquivos.S3ClientFactory;
import com.atividadeaws.back_end.infrastructure.enums.usuario.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PreDestroy;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.util.UUID;

@Component
public class StsS3ClientFactory implements S3ClientFactory {
    private final StsClient stsClient;
    private final Region region;
    private final String adminRoleArn;
    private final String userRoleArn;

    public StsS3ClientFactory(
            @Value("${aws.region}") String region,
            @Value("${aws.iam.admin-role-arn}") String adminRoleArn,
            @Value("${aws.iam.user-role-arn}") String userRoleArn) {
        this.region = Region.of(region);
        this.adminRoleArn = adminRoleArn;
        this.userRoleArn = userRoleArn;
        this.stsClient = StsClient.builder().region(this.region).build();
    }

    @Override
    public S3Client criarCliente(UserRole regra) {
        String roleArn = regra == UserRole.ADMIN ? adminRoleArn : userRoleArn;
        var credentials = stsClient.assumeRole(AssumeRoleRequest.builder()
                        .roleArn(roleArn)
                        .roleSessionName("api-arquivos-" + UUID.randomUUID())
                        .build())
                .credentials();

        var sessionCredentials = AwsSessionCredentials.create(
                credentials.accessKeyId(),
                credentials.secretAccessKey(),
                credentials.sessionToken());

        return S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(sessionCredentials))
                .build();
    }

    @PreDestroy
    void fechar() {
        stsClient.close();
    }
}
