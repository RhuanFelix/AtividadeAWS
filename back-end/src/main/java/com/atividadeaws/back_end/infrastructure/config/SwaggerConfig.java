package com.atividadeaws.back_end.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI personalizarSwagger() {
        return new OpenAPI()
                .info(new Info()
                        .title("Atividade AWS")
                        .version("1.0.0")
                        .description("""
                                Este projeto faz parte de uma atividade da disciplina de Sistemas Distribuídos com o objetivo de praticar as tecnologias da AWS:
                                
                                - EC2
                                - IAM
                                - S3
                                
                                ## Funcionalidades
                                
                                Esta API possui a finalidade de realizar upload e consulta de arquivos que estão armazenados no S3, porém o usuário comum só pode visualizar os arquivos. Em contrapartida, o usuário administrador pode tanto fazer upload, quanto consultar arquivos no S3, passando pela autenticação do IAM.
                                """)
                );
    }
}
