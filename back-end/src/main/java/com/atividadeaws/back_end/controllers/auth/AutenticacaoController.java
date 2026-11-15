package com.atividadeaws.back_end.controllers.auth;

import com.atividadeaws.back_end.infrastructure.dtos.auth.AutenticacaoRequest;
import com.atividadeaws.back_end.infrastructure.dtos.auth.AutenticacaoResponse;
import com.atividadeaws.back_end.infrastructure.dtos.auth.CadastroRequest;
import com.atividadeaws.back_end.infrastructure.entities.usuario.Usuario;
import com.atividadeaws.back_end.infrastructure.repositories.usuario.UsuarioRepository;
import com.atividadeaws.back_end.infrastructure.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AutenticacaoController {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository repository;
    private final TokenService service;

    @PostMapping("/login")
    public ResponseEntity<AutenticacaoResponse> fazerLogin(@RequestBody @Valid AutenticacaoRequest dados) {
        var emailSenha = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var autenticacao = this.authenticationManager.authenticate(emailSenha);
        var token = service.gerarToken((Usuario) autenticacao.getPrincipal());
        return ResponseEntity.ok(new AutenticacaoResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<CadastroRequest> CadastarUsuario(@RequestBody @Valid CadastroRequest dados) {
        if (this.repository.findByEmail(dados.email()) != null) return ResponseEntity.badRequest().build();
        String senhaCriptografada = new BCryptPasswordEncoder().encode(dados.senha());
        Usuario novoUsuario = new Usuario(dados.nome(), dados.email(), senhaCriptografada, dados.regra());
        this.repository.save(novoUsuario);
        return ResponseEntity.ok().build();
    }
}
