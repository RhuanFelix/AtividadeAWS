package com.atividadeaws.back_end.business.arquivos;

import com.atividadeaws.back_end.exceptions.genericos.RecursoNaoEncontradoException;
import com.atividadeaws.back_end.infrastructure.dtos.arquivos.ArquivoBaixado;
import com.atividadeaws.back_end.infrastructure.dtos.arquivos.ArquivoUploadResponse;
import com.atividadeaws.back_end.infrastructure.entities.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ArquivoService {
    private static final long TAMANHO_MAXIMO = 10L * 1024 * 1024;
    private final S3ClientFactory s3ClientFactory;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public ArquivoUploadResponse enviar(MultipartFile arquivo, Usuario usuario) {
        if (arquivo.isEmpty()) {
            throw new ArquivoInvalidoException("O arquivo não pode estar vazio.");
        }
        if (arquivo.getSize() > TAMANHO_MAXIMO) {
            throw new ArquivoInvalidoException("O arquivo excede o limite de 10 MB.");
        }

        String nome = normalizarNome(arquivo.getOriginalFilename());
        String tipoConteudo = arquivo.getContentType() == null ? "application/octet-stream" : arquivo.getContentType();
        String chave = UUID.randomUUID().toString();

        try (S3Client s3 = s3ClientFactory.criarCliente(usuario.getRegra());
             var input = arquivo.getInputStream()) {
            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(chave)
                            .contentType(tipoConteudo)
                            .metadata(Map.of("nome-original-base64", Base64.getUrlEncoder().withoutPadding()
                                    .encodeToString(nome.getBytes(StandardCharsets.UTF_8))))
                            .build(),
                    RequestBody.fromInputStream(input, arquivo.getSize()));
        } catch (IOException e) {
            throw new ArquivoInvalidoException("Não foi possível ler o arquivo enviado.");
        }

        return new ArquivoUploadResponse(chave, nome, tipoConteudo, arquivo.getSize());
    }

    public ArquivoBaixado baixar(String chave, Usuario usuario) {
        try (S3Client s3 = s3ClientFactory.criarCliente(usuario.getRegra())) {
            var resposta = s3.getObjectAsBytes(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(chave)
                    .build());
            String nomeCodificado = resposta.response().metadata().get("nome-original-base64");
            String nome = nomeCodificado == null ? chave : new String(
                    Base64.getUrlDecoder().decode(nomeCodificado), StandardCharsets.UTF_8);
            String tipoConteudo = resposta.response().contentType() == null
                    ? "application/octet-stream" : resposta.response().contentType();
            return new ArquivoBaixado(resposta.asByteArray(), normalizarNome(nome), tipoConteudo);
        } catch (S3Exception e) {
            if (e.statusCode() == 404 || "NoSuchKey".equals(e.awsErrorDetails() == null
                    ? null : e.awsErrorDetails().errorCode())) {
                throw new RecursoNaoEncontradoException("Arquivo não encontrado.");
            }
            throw e;
        }
    }

    private String normalizarNome(String nome) {
        if (nome == null || nome.isBlank()) return "arquivo";
        String nomeSemDiretorio = nome.replace('\\', '/');
        return nomeSemDiretorio.substring(nomeSemDiretorio.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "_");
    }
}
