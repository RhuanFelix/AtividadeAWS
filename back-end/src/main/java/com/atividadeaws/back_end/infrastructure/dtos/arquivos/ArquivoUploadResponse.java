package com.atividadeaws.back_end.infrastructure.dtos.arquivos;

public record ArquivoUploadResponse(String chave, String nome, String tipoConteudo, long tamanho) {
}
