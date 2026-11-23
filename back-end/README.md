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

- Autenticação e autorização com Spring Security e JWT.
- Hospedagem do servidor no EC2.
- Upload de arquivos de até 10 MB e download pelo backend, com armazenamento privado no S3.
- Uso do STS para assumir `AdminS3Role` para administradores e `UserS3Role` para usuários comuns.

## Configuração AWS

A aplicação usa a cadeia padrão de credenciais do AWS SDK. Localmente, configure um perfil AWS; na EC2, anexe uma role de instância com `sts:AssumeRole` para as roles de acesso ao bucket. Não configure access keys no arquivo da aplicação.

Propriedades configuráveis por ambiente (os valores entre parênteses são os padrões deste projeto):

| Variável | Padrão |
| --- | --- |
| `AWS_REGION` | `us-east-2` |
| `AWS_S3_BUCKET` | `armazenamento-s3-arquivos-sd` |
| `AWS_ADMIN_ROLE_ARN` | `arn:aws:iam::286713861555:role/AdminS3Role` |
| `AWS_USER_ROLE_ARN` | `arn:aws:iam::286713861555:role/UserS3Role` |

A role da EC2 precisa de permissão para chamar `sts:AssumeRole` nos dois ARNs. A política de confiança de cada role S3 precisa confiar na role da EC2. As políticas dessas roles devem conceder `s3:PutObject` ao admin e `s3:GetObject` ao usuário, nos objetos do bucket. Os endpoints atuais não precisam de `s3:ListBucket`.

## Endpoints

Estão implementados os seguintes endpoints:

- `/api/swagger-ui.html`: acessa o Swagger da API.
- `/api/auth/register`: permite que o usuário se cadastre.
- `/api/auth/login`: permite que o usuário faça login.
- `POST /api/files`: envia multipart no campo `arquivo`; somente `ADMIN`; retorna a chave gerada.
- `GET /api/files/{chave}`: baixa o arquivo; disponível para `ADMIN` e `USER`.

O cadastro atualmente aceita a role enviada no corpo, conforme o requisito didático escolhido. Como o cadastro é público, qualquer pessoa pode solicitar `ADMIN`; isso não é apropriado para um ambiente real.

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
