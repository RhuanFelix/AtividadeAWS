# API de gerenciamento de arquivo

Esta API faz parte de uma atividade proposta na disciplina de Sistemas Distribuídos, cujo objetivo é praticar as seguintes tecnologias da AWS:

- **EC2**: oferece capacidade computacional sob demanda e escalável na nuvem, permitindo lançar e configurar servidores virtuais (instâncias) conforme a necessidade.
- **S3**: serviço de armazenamento de objetos que provê escalabilidade, disponibilidade, segurança e desempenho para armazenar e recuperar qualquer volume de dados.
- **IAM**: serviço de gerenciamento de identidade e acesso que permite controlar, de forma segura, quem pode autenticar-se e quais permissões possui sobre os recursos da AWS.

## Tecnologias utilizadas

- Java 21
- Maven
- Spring Boot
- Docker

## Funcionalidades

Nem todas as funcionalidades estão implementadas; apenas as de autenticação e autorização estão prontas. As demais são planejadas para implementação futura.

- Autenticação e autorização com Spring Security e JWT.
- Hospedagem do servidor no EC2.
- Armazenamento e consultas de arquivos no S3.
- Utilização do IAM para definir permissões de usuário, limitando o que ele pode fazer dentro da aplicação.

## Endpoints

Atualmente, estão implementados os seguintes endpoints:

- `/api/swagger-ui.html`: acessa o Swagger da API.
- `/api/auth/register`: permite que o usuário se cadastre.
- `/api/auth/login`: permite que o usuário faça login.

## Executando o projeto

Para executar o projeto, é necessário ter as tecnologias listadas em *Tecnologias utilizadas*.

Abra o terminal e digite o seguinte comando:

```shell
docker compose up -d
```

Caso o comando acima não funcione, utilize:

```shell
docker-compose up -d
```

Após subir o banco no Docker, acesse a classe [BackEndApplication.java](./src/main/java/com/atividadeaws/back_end/BackEndApplication.java) e clique no botão de run.