# Regras de Negócio - Gestão de Usuários (Frontend)

Este documento detalha as regras de negócio e permissões para a interface de criação e vinculação de usuários em restaurantes.

## 1. Níveis de Permissão (Hierarquia)

O sistema possui uma hierarquia estrita de quem pode criar ou vincular novos usuários, baseada no papel (`Role`) do usuário logado.

| Usuário Logado | Pode Criar/Vincular | Roles Permitidas | Restrições |
| :--- | :--- | :--- | :--- |
| **SUPER_ADMIN** | Todos | `ADMIN`, `GERENTE`, `ATENDENTE` | Acesso total a qualquer restaurante. |
| **ADMIN** (Restaurante) | Gerentes e Atendentes | `GERENTE`, `ATENDENTE` | **NÃO** pode criar outros `ADMIN`. Apenas SUPER_ADMIN cria ADMINs. |
| **GERENTE** | Atendentes | `ATENDENTE` | **NÃO** pode criar `ADMIN` ou `GERENTE`. |
| **ATENDENTE** | Ninguém | - | Não tem acesso à funcionalidade de gestão de usuários. |

## 2. Funcionalidades

### 2.1. Criar Novo Usuário
Utilizado quando o colaborador ainda não possui conta na plataforma.

**Campos Obrigatórios:**
*   **Nome Completo**: Texto.
*   **Email**: Email válido (único no sistema).
*   **Senha**: Texto (aplicar regras de complexidade se houver).
*   **Perfil (Role)**: Seleção única (`ADMIN`, `GERENTE`, `ATENDENTE`).
    *   *O Frontend deve filtrar as opções disponíveis neste campo com base na tabela de hierarquia acima.*

**Validações de Backend (para tratar erros no front):**
*   Email já cadastrado (Status `409 Conflict`).
*   Usuário logado sem permissão para a role selecionada (Status `403 Forbidden`).

### 2.2. Vincular Usuário Existente
Utilizado quando o usuário já existe na plataforma (ex: já trabalha em outro restaurante ou foi cadastrado anteriormente).

**Campos Obrigatórios:**
*   **ID do Usuário**: UUID do usuário existente.
*   **Perfil (Role)**: Seleção única (`ADMIN`, `GERENTE`, `ATENDENTE`).

**Validações:**
*   Usuário já vinculado ao restaurante (Status `409 Conflict`).
*   Permissão insuficiente (Status `403 Forbidden`).

## 3. Regras de Interface (UX)

1.  **Visibilidade do Menu**: A opção "Gerenciar Usuários" ou "Equipe" deve estar oculta ou desabilitada para usuários com perfil **ATENDENTE**.
2.  **Filtro de Roles**: No formulário de criação/vinculação, o dropdown "Perfil" deve mostrar apenas as opções que o usuário logado pode criar.
    *   *Se sou ADMIN*: Mostrar `GERENTE` e `ATENDENTE`.
    *   *Se sou GERENTE*: Mostrar apenas `ATENDENTE`.
3.  **Feedback de Erro**:
    *   Tratar erro `409` (Conflito): "Este usuário já está vinculado a este restaurante" ou "Email já cadastrado".
    *   Tratar erro `403` (Proibido): "Você não tem permissão para realizar esta operação".

## 4. Glossário de Roles

*   **ADMIN**: Administrador do Restaurante. Acesso total às configurações do restaurante, cardápio e equipe.
*   **GERENTE**: Gerente operacional. Pode gerenciar produtos, pedidos e atendentes, mas não altera dados sensíveis do restaurante ou cria outros gerentes.
*   **ATENDENTE**: Operacional. Foca no processamento de pedidos e atendimento.

## 5. Padrão de Resposta de Erro (API)

A API retorna erros em um formato JSON padronizado. O Frontend deve estar preparado para capturar e exibir a mensagem amigável ao usuário.

### Estrutura do JSON de Erro

```json
{
  "message": "Mensagem amigável para o usuário (pode ser exibida no toast/alert)",
  "description": "Detalhes técnicos do erro (opcional, útil para debug)"
}
```

### Exemplos de Tratamento no Frontend

#### Exemplo 1: Erro de Negócio (ex: Usuário já vinculado)
**Status HTTP**: `409 Conflict`
**Resposta API**:
```json
{
  "message": "Usuário já está vinculado a este restaurante",
  "description": null
}
```
**Ação Front**: Exibir Toast de Erro com o campo `message`.

#### Exemplo 2: Erro de Permissão (ex: Gerente tentando criar Admin)
**Status HTTP**: `403 Forbidden`
**Resposta API**:
```json
{
  "message": "Acesso negado: você não tem permissão para acessar este recurso",
  "description": "ACCESS_DENIED"
}
```
**Ação Front**: Exibir Toast de Erro ou Redirecionar.

#### Exemplo 3: Erro de Validação de Campos
**Status HTTP**: `400 Bad Request`
**Resposta API**:
```json
{
  "email": "formato de email inválido",
  "nome": "não deve estar em branco"
}
```
**Nota**: Para erros de validação (`@Valid`), a API retorna um Map onde a chave é o nome do campo e o valor é a mensagem de erro.
**Ação Front**: Destacar os campos inválidos no formulário e exibir as mensagens abaixo de cada input.
