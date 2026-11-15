package com.atividadeaws.back_end.infrastructure.dtos.auth;

import com.atividadeaws.back_end.infrastructure.utils.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutenticacaoRequest(
        @NotBlank @Email @Size(max = 100, message = "Só é permitido até 100 caracteres") String email,
        @NotBlank @SenhaForte String senha
) {
}
