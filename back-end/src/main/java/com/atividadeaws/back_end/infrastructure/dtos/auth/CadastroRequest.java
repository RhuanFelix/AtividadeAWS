package com.atividadeaws.back_end.infrastructure.dtos.auth;

import com.atividadeaws.back_end.infrastructure.enums.usuario.UserRole;
import com.atividadeaws.back_end.infrastructure.utils.SenhaForte;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroRequest(
        @NotBlank String nome,
        @NotBlank @Email @Size(min = 8) String email,
        @NotBlank @SenhaForte String senha,
        @NotNull UserRole regra
        ) {
}
