package com.atividadeaws.back_end.business.arquivos;

import com.atividadeaws.back_end.infrastructure.enums.usuario.UserRole;
import software.amazon.awssdk.services.s3.S3Client;

public interface S3ClientFactory {
    S3Client criarCliente(UserRole regra);
}
