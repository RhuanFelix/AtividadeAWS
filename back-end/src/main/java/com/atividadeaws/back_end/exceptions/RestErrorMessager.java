package com.atividadeaws.back_end.exceptions;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record RestErrorMessager(Instant timestamp, int status, String erro, String messagem, String caminho) {
    public RestErrorMessager(int status, String erro, String mensagem, String caminho) {
        this(Instant.now(), status, erro, mensagem, caminho);
    }
}
