package com.atividadeaws.back_end.infrastructure.repositories.usuario;

import com.atividadeaws.back_end.infrastructure.entities.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String email);
}
