package com.atividadeaws.back_end.infrastructure.dtos.arquivos;

public record ArquivoBaixado(byte[] conteudo, String nome, String tipoConteudo) {
}
