# 📋 API Dashboard - Fluxo de Pedidos

> Documentação completa para integração do frontend com a API de Dashboard de Pedidos

## 🔐 Autenticação

Todas as requisições (exceto públicas) requerem token JWT no header:

```http
Authorization: Bearer SEU_TOKEN_JWT
```

**Base URL:** `http://localhost:8080/api`

---

## 📊 1. Dashboard Completo

### `GET /v1/dashboard/restaurante/{restauranteId}`

Retorna todos os dados do dashboard em uma única requisição.

**Parâmetros:**
- `restauranteId` (path) - ID do restaurante
- `data` (query, opcional) - Data para métricas (formato: `YYYY-MM-DD`)

**Exemplo:**
```http
GET /api/v1/dashboard/restaurante/1?data=2026-02-24
Authorization: Bearer SEU_TOKEN
```

**Resposta:**
```json
{
  "metricas": {
    "totalPedidos": 15,
    "faturamentoDia": 450.00,
    "ticketMedio": 30.00
  },
  "resumo": [
    {
      "status": "AGUARDANDO_CONFIRMACAO",
      "quantidade": 3
    },
    {
      "status": "EM_PREPARO",
      "quantidade": 5
    }
  ],
  "novos": [
    {
      "id": 1,
      "numeroPedido": "3180557896",
      "clienteNome": "João Silva",
      "restauranteNome": "Restaurante X",
      "valorTotal": 38.00,
      "status": "AGUARDANDO_CONFIRMACAO",
      "statusDescricao": "Aguardando Confirmação",
      "dataPedido": "2026-02-24T15:21:58",
      "criadoEm": "2026-02-24T15:21:58",
      "previsaoEntrega": "2026-02-24T16:01:58"
    }
  ],
  "preparo": [],
  "prontos": [],
  "entrega": []
}
```

**Campos da resposta:**
- `metricas` - Métricas do dia (total de pedidos, faturamento, ticket médio)
- `resumo` - Contagem de pedidos por status
- `novos` - Pedidos aguardando confirmação (AGUARDANDO_CONFIRMACAO)
- `preparo` - Pedidos em preparo (EM_PREPARO)
- `prontos` - Pedidos prontos para entrega (PRONTO)
- `entrega` - Pedidos em rota de entrega (EM_ENTREGA)

---

## 🔄 2. Fluxo de Status do Pedido

### Estados Possíveis

```
1. PENDENTE / AGUARDANDO_CONFIRMACAO  ← Pedido criado
2. CONFIRMADO                          ← Restaurante aceita
3. EM_PREPARO / PREPARANDO            ← Cozinha preparando
4. PRONTO                              ← Pronto para entrega
5. EM_ENTREGA / DESPACHADO            ← Saiu para entrega
6. ENTREGUE                            ← Cliente recebeu
7. CANCELADO                           ← Pedido cancelado
```

### Transições Válidas

```
PENDENTE → CONFIRMADO → EM_PREPARO → PRONTO → EM_ENTREGA → ENTREGUE
    ↓          ↓            ↓
CANCELADO  CANCELADO    CANCELADO
```

---

## 🎯 3. Atualizar Status do Pedido

### `PATCH /v1/pedidos/{pedidoId}/status`

Muda o status do pedido seguindo o fluxo válido.

**Request Body:**
```json
{
  "status": "CONFIRMADO",
  "observacoes": "Pedido aceito pelo restaurante"
}
```

**Status válidos:**
- `CONFIRMADO`
- `PREPARANDO` ou `EM_PREPARO`
- `PRONTO`
- `DESPACHADO` ou `EM_ENTREGA`
- `ENTREGUE`
- `CANCELADO`

**Resposta:** Retorna o pedido completo atualizado.

**Validações:**
- ❌ Não pode confirmar um pedido já confirmado
- ❌ Não pode marcar como pronto se não está em preparo
- ❌ Não pode cancelar se já foi despachado
- ✅ Registra timestamp de cada mudança de status

---

## 🖨️ 4. Gerar Comanda

### 4.1. Comanda HTML

```http
GET /v1/dashboard/pedido/{pedidoId}/comanda
```

**Resposta:** HTML pronto para impressão

### 4.2. Comanda PDF

```http
GET /v1/dashboard/pedido/{pedidoId}/comanda/pdf
```

**Resposta:** Arquivo PDF (Content-Type: application/pdf)

---

## 🎨 5. Exemplo de Fluxo no Frontend

### 5.1. Buscar Dashboard Completo

```typescript
// Buscar dashboard completo
const dashboard = await fetch(
  `${API_URL}/v1/dashboard/restaurante/${restauranteId}`,
  {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  }
).then(r => r.json());

// Usar os dados
console.log('Métricas:', dashboard.metricas);
console.log('Novos:', dashboard.novos);        // Array de pedidos
console.log('Em preparo:', dashboard.preparo);  // Array de pedidos
console.log('Prontos:', dashboard.prontos);     // Array de pedidos
console.log('Em entrega:', dashboard.entrega);  // Array de pedidos
console.log('Resumo:', dashboard.resumo);       // Array com contagem por status
```

### 5.2. Confirmar Pedido

```typescript
// Confirmar pedido (AGUARDANDO_CONFIRMACAO → CONFIRMADO)
const confirmar = async (pedidoId: number) => {
  await fetch(`${API_URL}/v1/pedidos/${pedidoId}/status`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      status: 'CONFIRMADO',
      observacoes: 'Pedido aceito'
    })
  });
  
  // Atualizar dashboard
  recarregarDashboard();
};
```

### 5.3. Marcar como Pronto

```typescript
// Marcar como pronto (EM_PREPARO → PRONTO)
const marcarPronto = async (pedidoId: number) => {
  await fetch(`${API_URL}/v1/pedidos/${pedidoId}/status`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      status: 'PRONTO'
    })
  });
};
```

### 5.4. Atualizar Dashboard (Polling)

```typescript
// Atualizar dashboard a cada 10 segundos
const atualizarDashboard = async () => {
  const dashboard = await fetch(
    `${API_URL}/v1/dashboard/restaurante/${restauranteId}`,
    { headers: { 'Authorization': `Bearer ${token}` } }
  ).then(r => r.json());
  
  // Atualizar estado do React/Vue/Angular
  setDashboard(dashboard);
};

setInterval(atualizarDashboard, 10000);
```

---

## 🔔 6. Polling / WebSocket (Futuro)

Para atualização em tempo real, recomenda-se:

**Opção 1: Polling (Atual)**
```typescript
// Atualizar a cada 10 segundos
setInterval(() => {
  recarregarDashboard();
}, 10000);
```

**Opção 2: WebSocket (Futuro)**
```typescript
// Conectar ao WebSocket
const ws = new WebSocket('ws://localhost:8080/ws/dashboard');

ws.onmessage = (event) => {
  const update = JSON.parse(event.data);
  atualizarPedido(update);
};
```

---

## ⚠️ 7. Tratamento de Erros

### Erros Comuns

**401 Unauthorized**
```json
{
  "timestamp": "2026-02-24 15:30:45",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token inválido ou expirado. Faça login novamente.",
  "path": "/api/v1/dashboard/...",
  "method": "GET"
}
```

**400 Bad Request**
```json
{
  "message": "Pedido não pode ser confirmado neste status",
  "description": null
}
```

**404 Not Found**
```json
{
  "message": "Pedido não encontrado",
  "description": null
}
```

### Tratamento no Frontend

```typescript
try {
  const response = await fetch(url, options);
  
  if (response.status === 401) {
    // Token expirado - redirecionar para login
    redirectToLogin();
    return;
  }
  
  if (!response.ok) {
    const error = await response.json();
    showError(error.message);
    return;
  }
  
  const data = await response.json();
  // Processar dados
  
} catch (error) {
  showError('Erro de conexão com o servidor');
}
```

---

## 🎯 8. Checklist de Implementação

### Frontend

- [ ] Buscar dashboard completo (`GET /v1/dashboard/restaurante/{id}`)
- [ ] Exibir métricas do dia (total, faturamento, ticket médio)
- [ ] Exibir resumo por status (gráfico/cards)
- [ ] Tela de pedidos novos (lista `dashboard.novos`)
- [ ] Botão "Confirmar Pedido" → `PATCH /v1/pedidos/{id}/status`
- [ ] Tela de pedidos em preparo (lista `dashboard.preparo`)
- [ ] Botão "Marcar como Pronto"
- [ ] Tela de pedidos prontos (lista `dashboard.prontos`)
- [ ] Botão "Despachar"
- [ ] Tela de pedidos em entrega (lista `dashboard.entrega`)
- [ ] Botão "Marcar como Entregue"
- [ ] Botão "Imprimir Comanda" → `GET /v1/dashboard/pedido/{id}/comanda`
- [ ] Botão "Cancelar Pedido" (com confirmação)
- [ ] Polling automático (10-30 segundos)
- [ ] Tratamento de erros (401, 400, 404)
- [ ] Loading states
- [ ] Notificações de sucesso/erro

---

## 📞 9. Suporte

**Dúvidas?** Entre em contato com o time de backend.

**Swagger UI:** http://localhost:8080/api/swagger

**Postman Collection:** Disponível no repositório

---

**Última atualização:** 2026-02-24  
**Versão da API:** 1.0.0
