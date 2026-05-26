# Relatorio de classes e tabelas

## Visao geral

O projeto e uma API Spring Boot para controle de estacionamento. A aplicacao permite cadastrar veiculos, criar vagas, registrar entradas e saidas, listar veiculos estacionados e consultar o historico de movimentacoes.

A persistencia usa Spring Data JPA. As entidades mapeadas para tabelas sao `Veiculo`, `Vaga` e `Movimentacao`. As classes `Carro`, `Moto` e `Caminhonete` herdam de `Veiculo` usando a estrategia `SINGLE_TABLE`, ou seja, todos os tipos de veiculo ficam na mesma tabela `veiculo`.

A classe `Veiculo` usa heranca para concentrar os dados comuns e deixar o comportamento especifico de cobranca nas subclasses. O enum `TipoVeiculo` e usado para representar os tipos aceitos pela aplicacao nas requisicoes, respostas e no registro salvo no banco.

## Classes de dominio

### `Veiculo`

Classe abstrata que representa os dados comuns de qualquer veiculo:

- `id`: identificador gerado pelo banco.
- `placa`: placa do veiculo, obrigatoria e unica.
- `modelo`: modelo do veiculo, obrigatorio.
- `cor`: cor do veiculo, obrigatoria.
- `tipo`: enum `TipoVeiculo`, com os valores `CARRO`, `MOTO` e `CAMINHONETE`.

Responsabilidades:

- Normalizar a placa para maiusculas.
- Calcular o valor a pagar com base no periodo entre entrada e saida.
- Definir o contrato para o multiplicador de cobranca por tipo de veiculo.
- Manter os atributos comuns que existem em todos os tipos de veiculo.

Restricoes:

- A placa nao pode ser nula e deve ser unica.
- A placa tem limite de 10 caracteres no mapeamento JPA.
- `modelo`, `cor` e `tipo` sao obrigatorios.
- A data de saida usada no calculo deve ser posterior a data de entrada.

### `Carro`, `Moto` e `Caminhonete`

Classes concretas que especializam `Veiculo`.

Responsabilidades:

- Definir o tipo do veiculo.
- Definir o multiplicador da cobranca:
  - `Carro`: 1.0
  - `Moto`: 0.5
  - `Caminhonete`: 1.5
- Implementar o comportamento especifico exigido por `Veiculo` para o calculo do valor.

Restricoes:

- Sao gravadas na tabela `veiculo`.
- O campo `tipo_discriminador` identifica a subclasse usada pelo JPA.

### `TipoVeiculo`

Enum com os tipos aceitos pela aplicacao:

- `CARRO`
- `MOTO`
- `CAMINHONETE`

Responsabilidade:

- Restringir os tipos validos para cadastro e cobranca.
- Representar o tipo recebido no cadastro de veiculo.
- Representar o tipo retornado nas respostas da API.
- Registrar o tipo do veiculo na tabela `veiculo`.

### `Vaga`

Representa uma vaga do estacionamento.

Campos:

- `id`: identificador gerado pelo banco.
- `numero`: numero da vaga, obrigatorio e unico.
- `ocupada`: indica se a vaga esta ocupada.

Responsabilidades:

- Controlar o estado da vaga.
- Impedir ocupacao quando a vaga ja esta ocupada.
- Liberar a vaga na saida do veiculo.

Restricoes:

- `numero` nao pode ser nulo.
- `numero` deve ser unico.
- `ocupada` nao pode ser nulo.

### `Movimentacao`

Representa uma entrada e possivel saida de um veiculo em uma vaga.

Campos:

- `id`: identificador gerado pelo banco.
- `veiculo`: veiculo associado.
- `vaga`: vaga associada.
- `dataEntrada`: data e hora da entrada.
- `dataSaida`: data e hora da saida, nula enquanto o veiculo estiver estacionado.
- `valorPago`: valor calculado na saida, nulo enquanto nao houver saida.

Responsabilidades:

- Registrar a permanencia de um veiculo em uma vaga.
- Calcular e armazenar o valor pago quando a saida e registrada.
- Liberar a vaga ao registrar a saida.

Restricoes:

- `veiculo`, `vaga` e `dataEntrada` sao obrigatorios.
- A saida nao pode ser registrada mais de uma vez.
- A saida deve ocorrer depois da entrada.
- Enquanto `dataSaida` for nula, a movimentacao representa um veiculo estacionado.

### `Estacionamento`

Classe de dominio sem mapeamento JPA.

Responsabilidade:

- Centralizar o calculo de valor a partir de uma movimentacao e uma data de saida.

Restricoes:

- Nao gera tabela no banco.

## Services

### `CadastrarVeiculoService`

Responsabilidades:

- Validar dados obrigatorios do cadastro.
- Normalizar a placa.
- Impedir cadastro duplicado por placa.
- Criar a subclasse correta (`Carro`, `Moto` ou `Caminhonete`) conforme o tipo informado.

Restricoes:

- Placa, modelo, cor e tipo sao obrigatorios.
- Nao permite duas placas iguais.

### `VagaService`

Responsabilidades:

- Criar vagas em lote.
- Numerar vagas sequencialmente a partir do maior numero existente.
- Listar vagas cadastradas.

Restricoes:

- A quantidade deve ser maior que zero.
- O numero da vaga deve ser unico.
- A implementacao assume que os numeros das vagas sao numericos.

### `RegistrarEntradaService`

Responsabilidades:

- Validar placa e numero da vaga.
- Verificar se o veiculo esta cadastrado.
- Verificar se a vaga esta cadastrada.
- Impedir que o mesmo veiculo tenha uma movimentacao aberta.
- Impedir entrada em vaga ocupada.
- Marcar a vaga como ocupada e criar a movimentacao.

Restricoes:

- A placa e o numero da vaga sao obrigatorios.
- O veiculo precisa estar cadastrado antes da entrada.
- A vaga precisa existir.
- Um veiculo nao pode estar estacionado em duas vagas ao mesmo tempo.
- Uma vaga nao pode receber dois veiculos ao mesmo tempo.

### `RegistrarSaidaService`

Responsabilidades:

- Localizar a movimentacao aberta pela placa.
- Registrar a data de saida.
- Calcular o valor pago.
- Liberar a vaga.

Restricoes:

- A placa e obrigatoria.
- O veiculo precisa estar estacionado.
- Uma movimentacao encerrada nao pode receber nova saida.

### `ListarEstacionadosService`

Responsabilidade:

- Listar movimentacoes ainda abertas, ordenadas pela data de entrada.

Restricao:

- Retorna apenas movimentacoes com `dataSaida` nula.

### `HistoricoMovimentacoesService`

Responsabilidade:

- Listar todo o historico, ordenado pela data de entrada de forma decrescente.

## Controllers

### `VeiculoController`

Endpoints:

- `POST /veiculos`: cadastra veiculo.
- `GET /veiculos`: lista veiculos.

Responsabilidade:

- Expor a API HTTP de veiculos e converter entidades para DTOs de resposta.

### `VagaController`

Endpoints:

- `POST /vagas`: cria vagas.
- `GET /vagas`: lista vagas.

Responsabilidade:

- Expor a API HTTP de vagas e converter entidades para DTOs de resposta.

### `EstacionamentoController`

Endpoints:

- `POST /estacionamento/entradas`: registra entrada.
- `POST /estacionamento/saidas`: registra saida.
- `GET /estacionamento/estacionados`: lista veiculos estacionados.
- `GET /estacionamento/historico`: lista historico.

Responsabilidade:

- Expor as operacoes principais do estacionamento.

### `ApiExceptionHandler`

Responsabilidade:

- Transformar excecoes de regra de negocio, argumentos invalidos e estado invalido em respostas HTTP 400 com `ProblemDetail`.

## DTOs

### Requests

- `CadastrarVeiculoRequest`: recebe `placa`, `modelo`, `cor` e `tipo`.
- `CriarVagasRequest`: recebe `quantidade`.
- `RegistrarEntradaRequest`: recebe `placa` e `numeroVaga`.
- `RegistrarSaidaRequest`: recebe `placa`.

Responsabilidade:

- Representar os dados recebidos nas requisicoes HTTP.

### Responses

- `VeiculoResponse`: retorna dados do veiculo.
- `VagaResponse`: retorna dados da vaga.
- `MovimentacaoResponse`: retorna dados da movimentacao com veiculo e vaga.

Responsabilidade:

- Evitar expor diretamente as entidades JPA na resposta da API.

## Repositories

### `VeiculoRepository`

Responsabilidades:

- Persistir e consultar veiculos.
- Verificar existencia por placa.
- Buscar veiculo por placa.

### `VagaRepository`

Responsabilidades:

- Persistir e consultar vagas.
- Verificar existencia por numero.
- Buscar vaga por numero.

### `MovimentacaoRepository`

Responsabilidades:

- Persistir e consultar movimentacoes.
- Buscar movimentacao aberta por placa.
- Verificar se existe veiculo estacionado.
- Verificar se existe vaga com movimentacao aberta.
- Listar estacionados e historico.

## Tabelas

### `veiculo`

Armazena todos os veiculos cadastrados. Por causa da heranca `SINGLE_TABLE`, tambem armazena o discriminador da subclasse.

Colunas principais:

- `id`: chave primaria.
- `tipo_discriminador`: usado pelo JPA para identificar `CARRO`, `MOTO` ou `CAMINHONETE`.
- `placa`: placa unica.
- `modelo`: modelo do veiculo.
- `cor`: cor do veiculo.
- `tipo`: tipo de veiculo usado pela aplicacao.

Restricoes:

- `placa` unica e obrigatoria.
- `placa` com ate 10 caracteres.
- `modelo`, `cor`, `tipo` e `tipo_discriminador` obrigatorios.
- `tipo` e `tipo_discriminador` aceitam apenas `CARRO`, `MOTO` e `CAMINHONETE`.

### `vaga`

Armazena as vagas do estacionamento.

Colunas principais:

- `id`: chave primaria.
- `numero`: numero da vaga.
- `ocupada`: estado atual da vaga.

Restricoes:

- `numero` unico e obrigatorio.
- `ocupada` obrigatorio.

### `movimentacao`

Armazena as entradas e saidas.

Colunas principais:

- `id`: chave primaria.
- `veiculo_id`: chave estrangeira para `veiculo`.
- `vaga_id`: chave estrangeira para `vaga`.
- `data_entrada`: data e hora da entrada.
- `data_saida`: data e hora da saida.
- `valor_pago`: valor pago na saida.

Restricoes:

- `veiculo_id`, `vaga_id` e `data_entrada` obrigatorios.
- `veiculo_id` referencia `veiculo(id)`.
- `vaga_id` referencia `vaga(id)`.
- `data_saida`, quando preenchida, deve ser maior ou igual a `data_entrada`.
- `valor_pago`, quando preenchido, nao deve ser negativo.
- O script SQL cria um indice unico parcial para impedir mais de uma movimentacao aberta por veiculo.
- O script SQL cria um indice unico parcial para impedir mais de uma movimentacao aberta por vaga.
