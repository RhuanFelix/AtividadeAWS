package com.atividadeaws.back_end.controllers.arquivos;

import com.atividadeaws.back_end.business.arquivos.ArquivoService;
import com.atividadeaws.back_end.infrastructure.dtos.arquivos.ArquivoUploadResponse;
import com.atividadeaws.back_end.infrastructure.entities.usuario.Usuario;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
@Tag(name = "Arquivos S3")
public class ArquivoController {
    private final ArquivoService arquivoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArquivoUploadResponse> enviar(
            @RequestParam("arquivo") MultipartFile arquivo,
            @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(arquivoService.enviar(arquivo, usuario));
    }

    @GetMapping("/{chave}")
    public ResponseEntity<byte[]> baixar(@PathVariable String chave, @AuthenticationPrincipal Usuario usuario) {
        var arquivo = arquivoService.baixar(chave, usuario);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(arquivo.tipoConteudo()));
        headers.setContentLength(arquivo.conteudo().length);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(arquivo.nome(), StandardCharsets.UTF_8)
                .build());
        return new ResponseEntity<>(arquivo.conteudo(), headers, HttpStatus.OK);
    }
}
