package com.atividadeaws.back_end.exceptions;

import com.atividadeaws.back_end.exceptions.genericos.RecursoNaoEncontradoException;
import com.atividadeaws.back_end.exceptions.token.TokenInvalidoException;
import com.atividadeaws.back_end.exceptions.token.TokenNaoPodeSerGeradoException;
import com.atividadeaws.back_end.business.arquivos.ArquivoInvalidoException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import software.amazon.awssdk.awscore.exception.AwsServiceException;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ArquivoInvalidoException.class)
    public ResponseEntity<RestErrorMessager> handleArquivoInvalido(ArquivoInvalidoException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new RestErrorMessager(status.value(), "Arquivo inválido", ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<RestErrorMessager> handleArquivoGrande(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.PAYLOAD_TOO_LARGE;
        return ResponseEntity.status(status).body(new RestErrorMessager(status.value(), "Arquivo muito grande", "O limite de upload é 10 MB.", request.getRequestURI()));
    }

    @ExceptionHandler(AwsServiceException.class)
    public ResponseEntity<RestErrorMessager> handleErroAws(AwsServiceException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        return ResponseEntity.status(status).body(new RestErrorMessager(status.value(), "Falha no serviço AWS", "Não foi possível concluir a operação no armazenamento.", request.getRequestURI()));
    }

    // TOKEN:

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<RestErrorMessager> handleTokenInvalido(TokenInvalidoException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        RestErrorMessager erro = new RestErrorMessager(
                status.value(),
                "Token inválido",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<RestErrorMessager> handleTokenNaoPodeSerGerado(TokenNaoPodeSerGeradoException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        RestErrorMessager erro = new RestErrorMessager(
                status.value(),
                "O Token não pode ser gerado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }

    // GENÉRICOS:

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RestErrorMessager> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        RestErrorMessager erro = new RestErrorMessager(
          status.value(),
          "Recurso não encontrado",
          ex.getMessage(),
          request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }
}
