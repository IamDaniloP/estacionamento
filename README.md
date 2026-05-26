# API de Estacionamento

API REST desenvolvida com Spring Boot para controle de um estacionamento. O projeto permite cadastrar veiculos, criar vagas, registrar entradas e saidas, consultar veiculos estacionados e visualizar o historico de movimentacoes.

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Maven
- PostgreSQL para persistencia da API
- H2 Database para testes em memoria
- Springdoc OpenAPI para documentacao da API

## Funcionalidades

- Cadastro de veiculos por placa, modelo, cor e tipo.
- Suporte aos tipos `CARRO`, `MOTO` e `CAMINHONETE`.
- Criacao de vagas numeradas.
- Registro de entrada de veiculo em uma vaga.
- Registro de saida com calculo automatico do valor pago.
- Listagem de veiculos atualmente estacionados.
- Consulta do historico de movimentacoes.
- Tratamento de erros de regra de negocio com respostas HTTP 400.

## Regras principais

- A placa do veiculo e obrigatoria e unica.
- O veiculo precisa estar cadastrado antes de registrar entrada.
- A vaga precisa existir antes de registrar entrada.
- Uma vaga ocupada nao pode receber outro veiculo.
- Um veiculo ja estacionado nao pode ter outra entrada aberta.
- A saida calcula o valor pago e libera a vaga.
- Enquanto `dataSaida` estiver nula, a movimentacao representa um veiculo estacionado.

## Como executar

Antes de iniciar a API, crie um container PostgreSQL:

```bash
docker run --name estacionamento-postgres -e POSTGRES_DB=estacionamento -e POSTGRES_USER=estacionamento -e POSTGRES_PASSWORD=estacionamento -p 5432:5432 -d postgres:16
```

Execute o projeto com o Maven Wrapper:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Por padrao, a aplicacao persiste os dados no PostgreSQL local, configurado em `src/main/resources/application.properties`.

Configuracao padrao do PostgreSQL:

- JDBC URL: `jdbc:postgresql://localhost:5432/estacionamento`
- Usuario: `estacionamento`
- Senha: `estacionamento`

As configuracoes podem ser sobrescritas por variaveis de ambiente:

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

## Acessos locais

Com a aplicacao em execucao:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints principais

### Veiculos

- `POST /veiculos`: cadastra um veiculo.
- `GET /veiculos`: lista os veiculos cadastrados.

Exemplo de cadastro:

```json
{
  "placa": "ABC1234",
  "modelo": "Civic",
  "cor": "Prata",
  "tipo": "CARRO"
}
```

### Vagas

- `POST /vagas`: cria vagas em lote.
- `GET /vagas`: lista as vagas cadastradas.

Exemplo de criacao:

```json
{
  "quantidade": 10
}
```

### Estacionamento

- `POST /estacionamento/entradas`: registra a entrada de um veiculo.
- `POST /estacionamento/saidas`: registra a saida de um veiculo.
- `GET /estacionamento/estacionados`: lista movimentacoes abertas.
- `GET /estacionamento/historico`: lista todo o historico de movimentacoes.

Exemplo de entrada:

```json
{
  "placa": "ABC1234",
  "numeroVaga": "1"
}
```

Exemplo de saida:

```json
{
  "placa": "ABC1234"
}
```

## Estrutura do projeto

```text
src/main/java/com/danilo_pereira/estacionamento
|-- config        # Configuracao do OpenAPI
|-- controller    # Endpoints REST e tratamento de excecoes
|-- dto           # Objetos de entrada e saida da API
|-- exception     # Excecoes de regra de negocio
|-- model         # Classes de dominio e entidades JPA
|-- repository    # Interfaces Spring Data JPA
`-- service       # Regras de negocio da aplicacao
```

## Banco de dados

As tabelas persistidas pelo projeto sao:

- `veiculo`
- `vaga`
- `movimentacao`

O script para criacao das tabelas no PostgreSQL esta em [scripts/create_tables_postgres.sql](scripts/create_tables_postgres.sql).

Com o container em execucao, o script pode ser aplicado pelo PowerShell:

```powershell
Get-Content scripts\create_tables_postgres.sql | docker exec -i estacionamento-postgres psql -U estacionamento -d estacionamento
```

## Documentacao complementar

- [Relatorio de classes e tabelas](docs/relatorio-classes-tabelas.md)
- [Script de criacao das tabelas PostgreSQL](scripts/create_tables_postgres.sql)

## Testes

Os testes usam a configuracao de `src/test/resources/application.properties`. Essa configuracao aponta para um banco H2 em memoria, em modo de compatibilidade com PostgreSQL, para que testes de integracao nao dependam do container PostgreSQL.

Execute os testes com:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```
