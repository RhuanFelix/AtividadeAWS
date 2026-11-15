package com.atividadeaws.back_end.infrastructure.security;

import com.atividadeaws.back_end.exceptions.token.TokenInvalidoException;
import com.atividadeaws.back_end.exceptions.token.TokenNaoPodeSerGeradoException;
import com.atividadeaws.back_end.infrastructure.entities.usuario.Usuario;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.jwt.secret}")
    private String secret;

    public String gerarToken(Usuario user){
        try{
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("atividade-aws")
                    .withSubject(user.getEmail())
                    .withClaim("nome", user.getNome())
                    .withClaim("regra", user.getRegra().toString())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception) {
            throw new TokenNaoPodeSerGeradoException("Erro ao gerar o token");
        }
    }

    public String validarToken(String token){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("atividade-aws")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new TokenInvalidoException("Token inválido");
        }
    }

    private Instant dataExpiracao(){
        return LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.of("-03:00"));
    }
}