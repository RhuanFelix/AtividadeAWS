package com.atividadeaws.back_end.business.arquivos;

import com.atividadeaws.back_end.infrastructure.entities.usuario.Usuario;
import com.atividadeaws.back_end.infrastructure.enums.usuario.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.web.multipart.MultipartFile;

class ArquivoServiceTest {
    private final S3ClientFactory s3ClientFactory = mock(S3ClientFactory.class);
    private final S3Client s3Client = mock(S3Client.class);
    private final ArquivoService service = new ArquivoService(s3ClientFactory);

    @BeforeEach
    void configurarBucket() {
        ReflectionTestUtils.setField(service, "bucket", "bucket-teste");
    }

    @Test
    void enviaArquivoUsandoRoleDoUsuarioEChaveGerada() {
        Usuario admin = usuario(UserRole.ADMIN);
        when(s3ClientFactory.criarCliente(UserRole.ADMIN)).thenReturn(s3Client);
        var arquivo = new MockMultipartFile("arquivo", "relatorio.txt", "text/plain", "conteudo".getBytes());

        var resposta = service.enviar(arquivo, admin);

        assertNotNull(resposta.chave());
        assertEquals("relatorio.txt", resposta.nome());
        assertEquals(8, resposta.tamanho());
        verify(s3ClientFactory).criarCliente(UserRole.ADMIN);
        verify(s3Client).putObject(any(PutObjectRequest.class), any(software.amazon.awssdk.core.sync.RequestBody.class));
    }

    @Test
    void baixaArquivoComRoleDoUsuarioEConteudoDoS3() {
        Usuario comum = usuario(UserRole.USER);
        byte[] conteudo = "conteudo".getBytes();
        var respostaS3 = ResponseBytes.fromByteArray(GetObjectResponse.builder()
                .contentType("text/plain")
                .metadata(java.util.Map.of("nome-original-base64", java.util.Base64.getUrlEncoder().withoutPadding()
                        .encodeToString("relatorio.txt".getBytes(java.nio.charset.StandardCharsets.UTF_8))))
                .build(), conteudo);
        when(s3ClientFactory.criarCliente(UserRole.USER)).thenReturn(s3Client);
        when(s3Client.getObjectAsBytes(any(GetObjectRequest.class))).thenReturn(respostaS3);

        var resposta = service.baixar("chave-123", comum);

        assertArrayEquals(conteudo, resposta.conteudo());
        assertEquals("relatorio.txt", resposta.nome());
        assertEquals("text/plain", resposta.tipoConteudo());
        verify(s3ClientFactory).criarCliente(UserRole.USER);
    }

    @Test
    void rejeitaArquivoAcimaDoLimiteSemChamarAws() {
        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.isEmpty()).thenReturn(false);
        when(arquivo.getSize()).thenReturn(10L * 1024 * 1024 + 1);

        assertThrows(ArquivoInvalidoException.class, () -> service.enviar(arquivo, usuario(UserRole.ADMIN)));
    }

    @Test
    void rejeitaArquivoVazioSemChamarAws() {
        MultipartFile arquivo = mock(MultipartFile.class);
        when(arquivo.isEmpty()).thenReturn(true);

        assertThrows(ArquivoInvalidoException.class, () -> service.enviar(arquivo, usuario(UserRole.ADMIN)));
    }

    private Usuario usuario(UserRole role) {
        Usuario usuario = new Usuario();
        usuario.setRegra(role);
        return usuario;
    }
}
