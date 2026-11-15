package com.atividadeaws.back_end.exceptions.token;

public class TokenNaoPodeSerGeradoException extends RuntimeException {
    public TokenNaoPodeSerGeradoException(String message) {
        super(message);
    }
}
