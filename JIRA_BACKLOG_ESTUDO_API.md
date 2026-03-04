# JIRA Backlog - Estudo API do Zero (Baseado no Banco)

## 1. Objetivo
Montar backlog Jira completo para time de 5-7 pessoas, com estrutura `Epic -> Story -> Subtask`, baixa colisao entre branches e integracao final no dominio de pedidos.

Resultado esperado:
- Tickets prontos para cadastro no Jira.
- Descricao objetiva por item.
- Campos tecnicos a codificar por story.
- Fluxo funcional MVP de checkout e fluxo tecnico de integracao.
- Sequencia de merge com `Pedidos` por ultimo.

## 2. Convencoes de Trabalho

### 2.1 Estrutura de branch
- Branch de epic: `epic/<codigo>-<nome-curto>`
- Branch de story: `feature/<codigo-story>-<nome-curto>`
- Fluxo de PR: `feature/*` -> `epic/*` -> `develop`

### 2.2 Regras de merge
- Merge em `develop` apenas quando o epic estiver com criterios de aceite fechados.
- EPIC-05 (Pedidos) inicia somente apos contratos dos EPIC-02, EPIC-03 e EPIC-04 estarem congelados.
- Integrador do EPIC-06 executa rebase diario das branches de epico.

### 2.3 Dono de arquivos sensiveis
Arquivos de alto risco de conflito devem ter owner unico por sprint:
- Config de seguranca
- Handlers globais de erro
- Contratos base de API
- Configuracoes de OpenAPI

### 2.4 Definition of Done (DoD)
Toda story so pode ser concluida quando:
- Endpoint implementado e documentado.
- Validacoes de entrada aplicadas.
- Regras de negocio cobertas por testes unitarios.
- Testes de integracao do modulo passando.
- Erros padronizados conforme contrato global.
- PR aprovado por pelo menos 1 revisor.

## 3. Mapa de Dependencias entre Dominios
- EPIC-01 Fundacao Tecnica e Contratos: sem dependencia.
- EPIC-02 Catalogo Publico: depende de EPIC-01.
- EPIC-03 Cliente e Endereco: depende de EPIC-01.
- EPIC-04 Entrega e Tipo de Pagamento: depende de EPIC-02 e EPIC-03.
- EPIC-05 Pedidos e Acompanhamento: depende de EPIC-02, EPIC-03, EPIC-04.
- EPIC-06 Integracao Final e Hardening: depende de todos.

## 4. Fluxo MVP (Checkout Publico)
1. Listar restaurantes.
2. Selecionar restaurante e carregar categorias/produtos/opcoes.
3. Identificar cliente por telefone e criar/recuperar cadastro.
4. Cadastrar ou selecionar endereco.
5. Calcular taxa e prazo de entrega.
6. Listar tipos de pagamento validos para o restaurante.
7. Criar pedido com itens/opcoes.
8. Consultar acompanhamento do pedido por numero.
9. Cancelar pedido se status permitir.

## 5. Contratos de API Minimos (Escopo MVP)
- `GET /public/v1/restaurantes`
- `GET /public/v1/restaurantes/{id}`
- `GET /public/v1/categorias?restauranteId=`
- `GET /public/v1/produtos?restauranteId=&categoriaId=`
- `POST /public/v1/clientes`
- `GET /public/v1/clientes/{id}`
- `POST /public/v1/clientes/{clienteId}/enderecos`
- `GET /public/v1/clientes/{clienteId}/enderecos`
- `POST /public/v1/enderecos/calcular-entrega`
- `GET /public/v1/tipos-pagamento?restauranteId=`
- `POST /public/v1/pedidos`
- `GET /public/v1/pedidos/{numeroPedido}`
- `PATCH /public/v1/pedidos/{numeroPedido}/cancelar`

DTOs minimos:
- `ClienteRequest`, `ClienteResponse`
- `EnderecoClienteRequest`, `EnderecoClienteResponse`
- `CalcularEntregaRequest`, `CalcularEntregaResponse`
- `ItemPedidoRequest`, `OpcaoItemRequest`, `PedidoRequest`, `PedidoResponse`
- `AtualizarStatusPedidoRequest` (interno/admin)

Estados de pedido (enum):
- `PENDENTE`, `CONFIRMADO`, `EM_PREPARO`, `PRONTO`, `EM_ENTREGA`, `ENTREGUE`, `CANCELADO`

Regras minimas:
- Calculo de subtotal, total e taxa.
- Validacao de item/opcao por restaurante.
- Transicoes validas de status.
- Idempotencia basica por `numero_pedido` (ou chave equivalente).

---

## 6. Backlog Jira por Epic (Copiar e Cadastrar)

Formato padrao por story:
- Objetivo
- Entradas/Saidas da API
- Campos a codificar (entidades, repositorios, DTOs, service, controller, validacoes, erros, testes)
- Criterios de aceite
- Dependencias
- DoD

### EPIC-01 - Fundacao Tecnica e Contratos
Resumo: base tecnica unica para reduzir conflito entre squads.

#### Story EPIC-01-ST01 - Setup inicial e padrao arquitetural (SP: 3)
Objetivo: preparar estrutura base, profiles, padrao de pacotes e contrato de resposta.
Entradas/Saidas da API: N/A (infra).
Campos a codificar:
- Entidades/tabelas: N/A
- Repositorios: N/A
- DTO request/response: `ErrorResponse` padrao, `MessageResponse` padrao
- Service: N/A
- Controller/rotas: endpoint de health basico
- Validacoes: padrao de validacao global
- Tratamento de erro: handler global com codigos consistentes
- Testes: smoke test de contexto e health
Criterios de aceite:
- Aplicacao sobe em ambiente local com profile definido.
- Contrato de erro unico validado por teste.
Dependencias: nenhuma.
DoD: checklist completo.
Subtasks:
- ST01-SUB01 Configurar profiles e variaveis de ambiente
- ST01-SUB02 Padronizar contrato de erro e handler
- ST01-SUB03 Criar endpoint healthcheck
- ST01-SUB04 Criar guia de convencoes tecnicas

#### Story EPIC-01-ST02 - Migrations e baseline de banco (SP: 5)
Objetivo: criar baseline com schema entregue e estrategia de migracoes.
Entradas/Saidas da API: N/A.
Campos a codificar:
- Entidades/tabelas: mapeamento inicial das tabelas do MVP
- Repositorios: interfaces base por dominio
- DTOs: N/A
- Service: N/A
- Controller/rotas: N/A
- Validacoes: constraints alinhadas com banco
- Tratamento de erro: violacao de integridade
- Testes: migracao sobe em banco limpo
Criterios de aceite:
- Banco sobe com migracoes sem erro.
- Integridade referencial preservada no MVP.
Dependencias: ST01.
DoD: checklist completo.
Subtasks:
- ST02-SUB01 Criar baseline de migracao
- ST02-SUB02 Separar scripts por modulo
- ST02-SUB03 Testar migrate/rollback em ambiente de estudo

#### Story EPIC-01-ST03 - OpenAPI, paginacao e contratos base (SP: 3)
Objetivo: padronizar documentacao e resposta paginada.
Entradas/Saidas da API: contrato de metadata de paginacao.
Campos a codificar:
- DTO request/response: `PageResponse<T>`
- Controller: exemplos de endpoints com paginacao
- Validacoes: query params de pagina
- Erros: pagina invalida
- Testes: serializacao e contrato OpenAPI
Criterios de aceite:
- Swagger expondo contratos base.
- Paginacao consistente entre endpoints list.
Dependencias: ST01.
DoD: checklist completo.
Subtasks:
- ST03-SUB01 Definir objeto de paginacao
- ST03-SUB02 Atualizar docs OpenAPI
- ST03-SUB03 Validar exemplos no Swagger

### EPIC-02 - Catalogo Publico
Resumo: disponibilizar leitura publica de restaurantes, categorias e produtos.

#### Story EPIC-02-ST01 - Listar restaurantes publicos (SP: 3)
Objetivo: implementar listagem e detalhe de restaurantes ativos.
Entradas/Saidas da API:
- Entrada: filtros opcionais (nome, aberto, pagina)
- Saida: lista resumida e detalhe
Campos a codificar:
- Entidades/tabelas: `restaurantes`, `enderecos_restaurante`, `horarios_restaurante`
- Repositorios: consulta por ativo/aberto
- DTOs: `RestauranteResumoResponse`, `RestauranteResponse`
- Service: regras de visibilidade publica
- Controller/rotas: `GET /public/v1/restaurantes`, `GET /public/v1/restaurantes/{id}`
- Validacoes: id existente
- Erros: restaurante nao encontrado
- Testes: unitario + integracao de listagem
Criterios de aceite:
- Retorna apenas restaurantes ativos.
- Detalhe retorna dados minimos do checkout.
Dependencias: EPIC-01.
DoD: checklist completo.
Subtasks:
- ST01-SUB01 Query de restaurantes ativos
- ST01-SUB02 Endpoint de detalhe
- ST01-SUB03 Testes de filtro e paginacao

#### Story EPIC-02-ST02 - Categorias por restaurante (SP: 2)
Objetivo: listar categorias ativas por restaurante.
Entradas/Saidas da API:
- Entrada: `restauranteId`
- Saida: categorias ordenadas
Campos a codificar:
- Entidades/tabelas: `categorias`
- Repositorios: busca por restaurante e ativo
- DTOs: `CategoriaResponse`
- Service: ordenacao por `ordem_exibicao`
- Controller/rotas: `GET /public/v1/categorias?restauranteId=`
- Validacoes: restaurante obrigatorio
- Erros: restaurante invalido
- Testes: cobertura de ordenacao
Criterios de aceite:
- Apenas categorias ativas e do restaurante informado.
Dependencias: ST01.
DoD: checklist completo.
Subtasks:
- ST02-SUB01 Repository de categorias publicas
- ST02-SUB02 Endpoint com validacao
- ST02-SUB03 Testes de ordenacao

#### Story EPIC-02-ST03 - Produtos e opcoes publicas (SP: 5)
Objetivo: listar produtos por restaurante/categoria com grupos e opcoes.
Entradas/Saidas da API:
- Entrada: `restauranteId`, `categoriaId` opcional
- Saida: produtos com grupos/opcoes e imagem principal
Campos a codificar:
- Entidades/tabelas: `produtos`, `grupos_opcoes`, `opcoes`, `imagens_produto`
- Repositorios: joins otimizados por restaurante
- DTOs: `ProdutoResponse`, `GrupoOpcaoResponse`, `OpcaoResponse`, `ImagemProdutoResponse`
- Service: somente produtos disponiveis
- Controller/rotas: `GET /public/v1/produtos?restauranteId=&categoriaId=`
- Validacoes: categoria pertence ao restaurante
- Erros: categoria invalida
- Testes: filtro por categoria, disponibilidade e ordenacao
Criterios de aceite:
- Produto de restaurante A nao aparece no restaurante B.
Dependencias: ST01, ST02.
DoD: checklist completo.
Subtasks:
- ST03-SUB01 Query de produtos publicos
- ST03-SUB02 Montagem de DTO hierarquico
- ST03-SUB03 Testes de filtro cruzado

### EPIC-03 - Cliente e Endereco
Resumo: identificar cliente e seus enderecos para checkout.

#### Story EPIC-03-ST01 - Criar/recuperar cliente publico (SP: 3)
Objetivo: cadastrar cliente por telefone (ou recuperar existente).
Entradas/Saidas da API:
- Entrada: nome, telefone, email opcional, cpf opcional
- Saida: cliente consolidado
Campos a codificar:
- Entidades/tabelas: `clientes`
- Repositorios: busca por telefone/cpf/email
- DTOs: `ClienteRequest`, `ClienteResponse`
- Service: regra de reativacao automatica
- Controller/rotas: `POST /public/v1/clientes`, `GET /public/v1/clientes/{id}`
- Validacoes: telefone obrigatorio e formato
- Erros: dados invalidos/duplicidade
- Testes: cria novo, retorna existente, reativa inativo
Criterios de aceite:
- POST idempotente por telefone.
Dependencias: EPIC-01.
DoD: checklist completo.
Subtasks:
- ST01-SUB01 Validacao de telefone e email
- ST01-SUB02 Logica de create-or-get
- ST01-SUB03 Testes de idempotencia

#### Story EPIC-03-ST02 - Enderecos de cliente (SP: 5)
Objetivo: cadastrar e listar enderecos de cliente.
Entradas/Saidas da API:
- Entrada: CEP, logradouro, numero, cidade, estado, complemento
- Saida: endereco criado/lista
Campos a codificar:
- Entidades/tabelas: `enderecos_cliente`
- Repositorios: busca por cliente e principal
- DTOs: `EnderecoClienteRequest`, `EnderecoClienteResponse`
- Service: primeiro endereco vira principal automaticamente
- Controller/rotas: `POST /public/v1/clientes/{clienteId}/enderecos`, `GET /public/v1/clientes/{clienteId}/enderecos`
- Validacoes: cliente dono do endereco
- Erros: cliente nao encontrado
- Testes: principal automatico e multiplos enderecos
Criterios de aceite:
- Endereco de cliente A nao pode ser usado por cliente B.
Dependencias: ST01.
DoD: checklist completo.
Subtasks:
- ST02-SUB01 Persistencia de endereco
- ST02-SUB02 Regra de endereco principal
- ST02-SUB03 Testes de ownership

### EPIC-04 - Entrega e Tipo de Pagamento
Resumo: fechar calculo de entrega e metodos de pagamento por restaurante.

#### Story EPIC-04-ST01 - Calculo de entrega (SP: 5)
Objetivo: calcular taxa e tempo estimado de entrega para checkout.
Entradas/Saidas da API:
- Entrada: restauranteId, latitude, longitude
- Saida: distanciaKm, taxaEntrega, tempoEstimadoMinutos
Campos a codificar:
- Entidades/tabelas: `restaurantes`, `enderecos_restaurante`
- Repositorios: coordenadas e taxa base
- DTOs: `CalcularEntregaRequest`, `CalcularEntregaResponse`
- Service: formula de distancia e regra de taxa
- Controller/rotas: `POST /public/v1/enderecos/calcular-entrega`
- Validacoes: coordenadas validas
- Erros: restaurante sem coordenada
- Testes: cenarios curta/media/longa distancia
Criterios de aceite:
- Retorno sempre com duas casas decimais para taxa.
Dependencias: EPIC-02, EPIC-03.
DoD: checklist completo.
Subtasks:
- ST01-SUB01 Implementar calculo de distancia
- ST01-SUB02 Implementar regra de taxa e prazo
- ST01-SUB03 Testes de borda geografica

#### Story EPIC-04-ST02 - Tipos de pagamento por restaurante (SP: 3)
Objetivo: listar tipos de pagamento ativos e vinculados ao restaurante.
Entradas/Saidas da API:
- Entrada: restauranteId
- Saida: lista de tipos com `requerTroco`
Campos a codificar:
- Entidades/tabelas: `tipos_pagamento`, `restaurante_tipo_pagamento`
- Repositorios: vinculos ativos
- DTOs: `TipoPagamentoResponse`
- Service: ordenacao por `ordem_exibicao`
- Controller/rotas: `GET /public/v1/tipos-pagamento?restauranteId=`
- Validacoes: restaurante valido
- Erros: sem pagamento disponivel
- Testes: filtro por restaurante
Criterios de aceite:
- Nao retornar pagamento inativo.
Dependencias: EPIC-02.
DoD: checklist completo.
Subtasks:
- ST02-SUB01 Query de tipos por restaurante
- ST02-SUB02 Endpoint publico
- ST02-SUB03 Testes de consistencia

### EPIC-05 - Pedidos e Acompanhamento (Integracao Final)
Resumo: modulo agregador de dependencias, implementar por ultimo.

#### Story EPIC-05-ST01 - Criar pedido publico (SP: 8)
Objetivo: criar pedido com validacao completa de cliente, endereco, itens, opcoes e pagamento.
Entradas/Saidas da API:
- Entrada: `PedidoRequest` (clienteId, enderecoId, restauranteId, tipoPagamentoId, itens)
- Saida: `PedidoResponse` (numeroPedido, status, valores)
Campos a codificar:
- Entidades/tabelas: `pedidos`, `itens_pedido`, `opcoes_item_pedido`
- Repositorios: persistencia transacional
- DTOs: `PedidoRequest`, `ItemPedidoRequest`, `OpcaoItemRequest`, `PedidoResponse`
- Service: calculo subtotal/total/taxa/desconto e validacao cruzada
- Controller/rotas: `POST /public/v1/pedidos`
- Validacoes: item/opcao pertence ao restaurante
- Erros: pagamento invalido, endereco invalido, produto indisponivel
- Testes: unitario de calculo + integracao transacional
Criterios de aceite:
- Pedido nao aceita produto de outro restaurante.
- Idempotencia por `numero_pedido` ou chave equivalente.
Dependencias: EPIC-02, EPIC-03, EPIC-04.
DoD: checklist completo.
Subtasks:
- ST01-SUB01 Definir contrato final `PedidoRequest`
- ST01-SUB02 Implementar motor de calculo
- ST01-SUB03 Persistencia atomica de pedido/itens/opcoes
- ST01-SUB04 Testes de regressao cruzada

#### Story EPIC-05-ST02 - Acompanhamento de pedido (SP: 3)
Objetivo: consultar pedido por numero.
Entradas/Saidas da API:
- Entrada: `numeroPedido`
- Saida: status atual, historico e previsao
Campos a codificar:
- Entidades/tabelas: `pedidos`, `historico_status_pedido`
- Repositorios: busca por numero
- DTOs: `PedidoResponse`, `HistoricoStatusPedidoResponse`
- Service: agregacao de timeline
- Controller/rotas: `GET /public/v1/pedidos/{numeroPedido}`
- Validacoes: numero pedido valido
- Erros: pedido nao encontrado
- Testes: consulta de pedido existente/inexistente
Criterios de aceite:
- Historico de status retornado em ordem cronologica.
Dependencias: ST01.
DoD: checklist completo.
Subtasks:
- ST02-SUB01 Endpoint de consulta por numero
- ST02-SUB02 Montagem de timeline
- ST02-SUB03 Testes de ordenacao historico

#### Story EPIC-05-ST03 - Cancelamento e transicoes de status (SP: 5)
Objetivo: cancelar pedido com regra de status e registrar historico.
Entradas/Saidas da API:
- Entrada: motivo cancelamento
- Saida: pedido atualizado
Campos a codificar:
- Entidades/tabelas: `pedidos`, `historico_status_pedido`
- Repositorios: update de status
- DTOs: `AtualizarStatusPedidoRequest`
- Service: maquina de estados (transicoes permitidas)
- Controller/rotas: `PATCH /public/v1/pedidos/{numeroPedido}/cancelar`
- Validacoes: status atual permite cancelamento
- Erros: transicao invalida
- Testes: todas transicoes permitidas e proibidas
Criterios de aceite:
- Nao permitir cancelar pedido entregue.
Dependencias: ST01, ST02.
DoD: checklist completo.
Subtasks:
- ST03-SUB01 Implementar regras de transicao
- ST03-SUB02 Registrar historico a cada mudanca
- ST03-SUB03 Testes de maquina de estados

### EPIC-06 - Integracao Final e Hardening
Resumo: consolidacao final, qualidade e documentacao.

#### Story EPIC-06-ST01 - Suite de testes E2E do MVP (SP: 5)
Objetivo: validar fluxo completo de checkout ponta a ponta.
Entradas/Saidas da API: roteiro E2E completo.
Campos a codificar:
- Testes de integracao/E2E:
  - lista restaurante -> produtos
  - cliente -> endereco
  - calcular entrega -> pagamento
  - criar pedido -> acompanhar -> cancelar (quando permitido)
Criterios de aceite:
- Pipeline de testes MVP verde.
Dependencias: todos epicos anteriores.
DoD: checklist completo.
Subtasks:
- ST01-SUB01 Escrever cenario happy path
- ST01-SUB02 Escrever cenarios de erro
- ST01-SUB03 Publicar relatorio de execucao

#### Story EPIC-06-ST02 - Performance minima e observabilidade (SP: 3)
Objetivo: garantir baseline de performance e logs para debug.
Campos a codificar:
- Metricas de endpoint critico (pedidos)
- Log estruturado para erros de validacao
- Timeout e retry apenas para integracoes externas
Criterios de aceite:
- Latencia media do checkout dentro da meta definida pelo grupo.
Dependencias: ST01.
DoD: checklist completo.
Subtasks:
- ST02-SUB01 Instrumentar metricas
- ST02-SUB02 Ajustar logs
- ST02-SUB03 Executar smoke de carga leve

#### Story EPIC-06-ST03 - Documentacao final e handoff (SP: 2)
Objetivo: consolidar guias para entrada de novos membros e manutencao.
Campos a codificar:
- Guia de setup
- Guia de contrato de API
- Guia de troubleshooting
Criterios de aceite:
- Novo membro sobe ambiente e roda fluxo MVP sem ajuda.
Dependencias: ST01, ST02.
DoD: checklist completo.
Subtasks:
- ST03-SUB01 Consolidar README tecnico
- ST03-SUB02 Publicar changelog de contratos
- ST03-SUB03 Checklist de handoff

---

## 7. Cenarios de Teste Obrigatorios por Epic

### EPIC-02 Catalogo
- Listar apenas restaurantes ativos.
- Categoria deve pertencer ao restaurante.
- Produto indisponivel nao aparece.

### EPIC-03 Cliente/Endereco
- POST cliente idempotente por telefone.
- Primeiro endereco vira principal.
- Endereco de um cliente nao pode ser manipulado por outro.

### EPIC-04 Entrega/Pagamento
- Taxa de entrega calculada para distancias distintas.
- Restaurante sem coordenada deve falhar com erro de negocio.
- Tipo de pagamento inativo nao deve ser retornado.

### EPIC-05 Pedidos
- Pedido nao aceita produto de outro restaurante.
- Pedido nao aceita tipo de pagamento invalido para restaurante.
- Transicoes de status invalidas devem falhar.

### EPIC-06 E2E
- Fluxo completo de checkout deve concluir sem erro.
- Cancelamento so em status permitido.
- Regressao geral apos ajustes de performance.

## 8. Sequencia de Execucao por Sprint

### Sprint 1
- Finalizar EPIC-01.
- Iniciar EPIC-02 e EPIC-03 em paralelo.

### Sprint 2
- Concluir EPIC-02 e EPIC-03.
- Concluir EPIC-04.

### Sprint 3
- Concluir EPIC-05 (ultimo dominio de negocio).
- Concluir EPIC-06 com integracao final e hardening.

## 9. Assumptions e Defaults
- Banco fornecido ja contem schema base e constraints.
- Escopo inicial e somente MVP de checkout publico.
- Modulos de cupons, descontos e entregador completo ficam fora do primeiro ciclo.
- Estimativas em Fibonacci: `1,2,3,5,8`.
- Endpoints publicos liberados para checkout; endpoints admin protegidos.

## 10. Ordem de Merge Recomendada
1. Merge de EPIC-01 em `develop`.
2. Merge de EPIC-02 e EPIC-03.
3. Merge de EPIC-04.
4. Merge de EPIC-05 (somente com contratos congelados).
5. Merge de EPIC-06 para estabilizacao final.

Este arquivo foi desenhado para uso direto no Jira, com granularidade suficiente para desenvolvimento paralelo e integracao com baixo conflito.
