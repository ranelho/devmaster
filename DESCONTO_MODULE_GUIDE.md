# 🎯 Módulo de Descontos - Guia Completo

## 📋 Visão Geral

O módulo de descontos permite criar promoções temporárias para produtos com percentual de desconto configurável e período de vigência flexível (dias ou horas).

## 🔑 Características

- ✅ **Desconto por percentual** (0.01% a 100%)
- ✅ **Período configurável** (dias ou horas)
- ✅ **Validação automática** de vigência
- ✅ **Múltiplos descontos** por produto (histórico)
- ✅ **Cálculo automático** de valores
- ✅ **Área protegida** (requer autenticação JWT)

## 🌐 Endpoints Disponíveis

### 🔒 Área Logada (Requer JWT)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/descontos` | Criar desconto |
| PUT | `/api/v1/descontos/{id}` | Atualizar desconto |
| GET | `/api/v1/descontos/{id}` | Buscar por ID |
| GET | `/api/v1/descontos` | Listar todos |
| GET | `/api/v1/descontos/produto/{produtoId}` | Listar por produto |
| GET | `/api/v1/descontos/vigentes` | Listar vigentes |
| DELETE | `/api/v1/descontos/{id}` | Deletar desconto |

## 📝 Exemplos de Uso

### 1️⃣ Criar Desconto (Intervalo em Dias)

```bash
curl -X POST http://localhost:8081/api/v1/descontos \
  -H "Authorization: Bearer seu-token-jwt" \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": 1,
    "percentualDesconto": 15.50,
    "tipoIntervalo": "DIAS",
    "dataHoraInicio": "2025-01-01T00:00:00",
    "dataHoraFim": "2025-01-31T23:59:59",
    "ativo": true
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "produtoId": 1,
  "nomeProduto": "Pizza Margherita",
  "percentualDesconto": 15.50,
  "tipoIntervalo": "DIAS",
  "dataHoraInicio": "2025-01-01T00:00:00",
  "dataHoraFim": "2025-01-31T23:59:59",
  "ativo": true,
  "vigente": false,
  "criadoEm": "2024-12-26T10:30:00",
  "atualizadoEm": "2024-12-26T10:30:00"
}
```

### 2️⃣ Criar Desconto (Intervalo em Horas)

```bash
curl -X POST http://localhost:8081/api/v1/descontos \
  -H "Authorization: Bearer seu-token-jwt" \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": 2,
    "percentualDesconto": 20.00,
    "tipoIntervalo": "HORAS",
    "dataHoraInicio": "2024-12-26T18:00:00",
    "dataHoraFim": "2024-12-26T22:00:00",
    "ativo": true
  }'
```

### 3️⃣ Atualizar Desconto

```bash
curl -X PUT http://localhost:8081/api/v1/descontos/1 \
  -H "Authorization: Bearer seu-token-jwt" \
  -H "Content-Type: application/json" \
  -d '{
    "produtoId": 1,
    "percentualDesconto": 25.00,
    "tipoIntervalo": "DIAS",
    "dataHoraInicio": "2025-01-01T00:00:00",
    "dataHoraFim": "2025-01-31T23:59:59",
    "ativo": true
  }'
```

### 4️⃣ Listar Descontos Vigentes

```bash
curl -X GET http://localhost:8081/api/v1/descontos/vigentes \
  -H "Authorization: Bearer seu-token-jwt"
```

### 5️⃣ Listar Descontos de um Produto

```bash
curl -X GET http://localhost:8081/api/v1/descontos/produto/1 \
  -H "Authorization: Bearer seu-token-jwt"
```

### 6️⃣ Deletar Desconto

```bash
curl -X DELETE http://localhost:8081/api/v1/descontos/1 \
  -H "Authorization: Bearer seu-token-jwt"
```

## 🎨 Integração com Listagem de Produtos

### Produto COM Desconto Vigente

```json
{
  "id": 1,
  "nome": "Pizza Margherita",
  "preco": 45.00,
  "percentualDesconto": 15.50,
  "valorOriginal": 45.00,
  "valorComDesconto": 38.03,
  "temDescontoVigente": true
}
```

### Produto SEM Desconto

```json
{
  "id": 2,
  "nome": "Pizza Calabresa",
  "preco": 50.00,
  "percentualDesconto": null,
  "valorOriginal": 50.00,
  "valorComDesconto": 50.00,
  "temDescontoVigente": false
}
```

## ⚙️ Regras de Negócio

### ✅ Validações

1. **Percentual**: Entre 0.01% e 100%
2. **Período**: Data/hora fim deve ser posterior ao início
3. **Produto**: Deve existir no sistema
4. **Datas**: Devem ser futuras na criação

### 🔍 Vigência

Um desconto é considerado **vigente** quando:
- `ativo = true`
- Data/hora atual está entre `dataHoraInicio` e `dataHoraFim`

### 📊 Cálculo de Desconto

```
valorComDesconto = valorOriginal * (1 - percentualDesconto / 100)

Exemplo:
- Valor Original: R$ 45,00
- Desconto: 15.50%
- Valor com Desconto: R$ 45,00 * (1 - 15.50/100) = R$ 38,03
```

## 🗄️ Estrutura do Banco

```sql
CREATE TABLE descontos (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    percentual_desconto DECIMAL(5,2) NOT NULL,
    tipo_intervalo VARCHAR(20) NOT NULL,
    data_hora_inicio TIMESTAMP NOT NULL,
    data_hora_fim TIMESTAMP NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP NOT NULL
);
```

## 🧪 Testando no Swagger

1. Acesse: http://localhost:8081/api/swagger
2. Clique em **"Authorize"** 🔒
3. Insira seu token JWT
4. Navegue até **"Descontos (Protegido)"**
5. Teste os endpoints disponíveis

## 🚨 Tratamento de Erros

### 400 - Bad Request
```json
{
  "timestamp": "2024-12-26T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Data/hora de fim deve ser posterior à data/hora de início"
}
```

### 401 - Unauthorized
```json
{
  "timestamp": "2024-12-26T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Token JWT inválido ou expirado"
}
```

### 404 - Not Found
```json
{
  "timestamp": "2024-12-26T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Produto não encontrado"
}
```

## 📈 Casos de Uso

### 🎉 Happy Hour (Intervalo em Horas)
```json
{
  "produtoId": 5,
  "percentualDesconto": 30.00,
  "tipoIntervalo": "HORAS",
  "dataHoraInicio": "2024-12-26T17:00:00",
  "dataHoraFim": "2024-12-26T19:00:00"
}
```

### 🎊 Promoção de Fim de Ano (Intervalo em Dias)
```json
{
  "produtoId": 10,
  "percentualDesconto": 50.00,
  "tipoIntervalo": "DIAS",
  "dataHoraInicio": "2024-12-20T00:00:00",
  "dataHoraFim": "2024-12-31T23:59:59"
}
```

### 🌅 Café da Manhã Promocional
```json
{
  "produtoId": 3,
  "percentualDesconto": 25.00,
  "tipoIntervalo": "HORAS",
  "dataHoraInicio": "2024-12-27T06:00:00",
  "dataHoraFim": "2024-12-27T10:00:00"
}
```

## 🔧 Troubleshooting

### Problema: Desconto não aparece como vigente
**Solução**: Verifique se:
- `ativo = true`
- Data/hora atual está dentro do período
- Não há erro de timezone

### Problema: Erro 401 ao criar desconto
**Solução**: 
- Verifique se o token JWT está válido
- Configure o token no Swagger (botão Authorize)

### Problema: Produto não encontrado
**Solução**:
- Confirme que o `produtoId` existe no banco
- Verifique se o produto não foi deletado

---

**Happy Coding!** 🚀✨
