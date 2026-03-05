# 📊 API Dashboard e Relatórios - Guia de Integração Frontend

Este documento detalha os endpoints disponíveis para o módulo de relatórios e dashboard de vendas.

> **🔒 Autenticação Obrigatória**: Todos os endpoints requerem token Bearer válido e usuário com role `ADMIN` ou `GERENTE`.

## 1. Dashboard de Vendas

Retorna as métricas consolidadas para exibição no painel administrativo.

### Endpoint
`GET /v1/dashboard/vendas`

### Parâmetros (Query Params)
| Parâmetro | Tipo | Obrigatório | Descrição | Exemplo |
|-----------|------|-------------|-----------|---------|
| `inicio` | Date | Não | Data inicial do filtro (yyyy-MM-dd) | `2024-01-01` |
| `fim` | Date | Não | Data final do filtro (yyyy-MM-dd) | `2024-01-31` |

> **Nota**: Se as datas não forem informadas, retorna dados de todo o período histórico.

### Exemplo de Requisição
```http
GET /v1/dashboard/vendas?inicio=2024-01-01&fim=2024-01-31
Authorization: Bearer <seu_token>
```

### Exemplo de Resposta (200 OK)
```json
{
  "totalVendas": 15430.50,
  "quantidadePedidos": 125,
  "ticketMedio": 123.44,
  "vendasPorDia": [
    {
      "data": "2024-01-01",
      "totalVendas": 1250.00,
      "quantidadePedidos": 10
    },
    {
      "data": "2024-01-02",
      "totalVendas": 980.50,
      "quantidadePedidos": 8
    }
    // ... mais dias
  ]
}
```

### Uso no Frontend (Sugestão)
- **Cards de Métricas**: Use `totalVendas`, `quantidadePedidos` e `ticketMedio` para exibir cards de resumo no topo.
- **Gráfico de Linha/Barra**: Use o array `vendasPorDia` para plotar a evolução das vendas no período.
  - Eixo X: `data`
  - Eixo Y: `totalVendas` ou `quantidadePedidos`

---

## 2. Relatório de Vendas (Excel)

Gera e baixa um arquivo `.xlsx` contendo a listagem detalhada de vendas no período.

### Endpoint
`GET /v1/relatorios/vendas/excel`

### Parâmetros (Query Params)
| Parâmetro | Tipo | Obrigatório | Descrição | Exemplo |
|-----------|------|-------------|-----------|---------|
| `inicio` | Date | Não | Data inicial do filtro (yyyy-MM-dd) | `2024-01-01` |
| `fim` | Date | Não | Data final do filtro (yyyy-MM-dd) | `2024-01-31` |

### Exemplo de Requisição
```http
GET /v1/relatorios/vendas/excel?inicio=2024-01-01&fim=2024-01-31
Authorization: Bearer <seu_token>
```

### Resposta
- **Content-Type**: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Content-Disposition**: `attachment; filename=relatorio_vendas_20240131.xlsx`
- **Body**: Arquivo binário (blob)

### Uso no Frontend (Axios)
```javascript
const downloadRelatorioVendas = async (inicio, fim) => {
  const response = await api.get('/v1/relatorios/vendas/excel', {
    params: { inicio, fim },
    responseType: 'blob' // Importante para download de arquivos
  });
  
  // Criar link temporário para download
  const url = window.URL.createObjectURL(new Blob([response.data]));
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', 'relatorio_vendas.xlsx');
  document.body.appendChild(link);
  link.click();
  link.remove();
};
```

---

## 3. Relatório de Produtos Mais Vendidos (Excel)

Gera e baixa um arquivo `.xlsx` com o ranking de produtos mais vendidos.

### Endpoint
`GET /v1/relatorios/produtos-mais-vendidos/excel`

### Parâmetros (Query Params)
| Parâmetro | Tipo | Obrigatório | Descrição | Exemplo |
|-----------|------|-------------|-----------|---------|
| `inicio` | Date | Não | Data inicial do filtro (yyyy-MM-dd) | `2024-01-01` |
| `fim` | Date | Não | Data final do filtro (yyyy-MM-dd) | `2024-01-31` |

### Exemplo de Requisição
```http
GET /v1/relatorios/produtos-mais-vendidos/excel?inicio=2024-01-01&fim=2024-01-31
Authorization: Bearer <seu_token>
```

### Resposta
- **Content-Type**: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`
- **Content-Disposition**: `attachment; filename=produtos_mais_vendidos_20240131.xlsx`
- **Body**: Arquivo binário (blob)

---

## Códigos de Erro Comuns

| Código | Descrição | Causa Provável |
|--------|-----------|----------------|
| `401` | Unauthorized | Token inválido ou não enviado |
| `403` | Forbidden | Usuário logado não tem permissão (requer ADMIN ou GERENTE) |
| `500` | Internal Server Error | Erro ao gerar o arquivo Excel |
