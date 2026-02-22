# Setup: Tipos de Pagamento

## 📋 Visão Geral

Scripts SQL para inserir os tipos de pagamento no banco de dados.

## 📁 Arquivos Disponíveis

### 1. `insert_tipos_pagamento.sql` (Completo)
- ✅ Versão completa com comentários
- ✅ Inclui 6 tipos de pagamento
- ✅ Queries úteis para manutenção
- ✅ Documentação detalhada

### 2. `insert_tipos_pagamento_simples.sql` (Simplificado)
- ✅ Versão minimalista
- ✅ Inclui 4 tipos principais
- ✅ Rápido de executar

---

## 🚀 Como Executar

### Opção 1: Via MySQL Workbench / DBeaver
1. Abrir o arquivo SQL
2. Conectar ao banco de dados
3. Executar o script (Ctrl+Enter ou botão Run)

### Opção 2: Via Linha de Comando
```bash
# MySQL
mysql -u root -p devmaster < insert_tipos_pagamento.sql

# PostgreSQL
psql -U postgres -d devmaster -f insert_tipos_pagamento.sql
```

### Opção 3: Via Docker
```bash
# MySQL
docker exec -i mysql-container mysql -uroot -psenha devmaster < insert_tipos_pagamento.sql

# PostgreSQL
docker exec -i postgres-container psql -U postgres -d devmaster < insert_tipos_pagamento.sql
```

---

## 📊 Tipos de Pagamento Incluídos

### Versão Completa (6 tipos)

| ID | Nome | Código | Requer Troco | Ordem |
|----|------|--------|--------------|-------|
| 1 | Dinheiro | DINHEIRO | ✅ Sim | 1 |
| 2 | Cartão de Crédito | CARTAO_CREDITO | ❌ Não | 2 |
| 3 | Cartão de Débito | CARTAO_DEBITO | ❌ Não | 3 |
| 4 | PIX | PIX | ❌ Não | 4 |
| 5 | Vale Refeição | VALE_REFEICAO | ❌ Não | 5 |
| 6 | Vale Alimentação | VALE_ALIMENTACAO | ❌ Não | 6 |

### Versão Simples (4 tipos)

| ID | Nome | Código | Requer Troco | Ordem |
|----|------|--------|--------------|-------|
| 1 | Dinheiro | DINHEIRO | ✅ Sim | 1 |
| 2 | Cartão de Crédito | CARTAO_CREDITO | ❌ Não | 2 |
| 3 | Cartão de Débito | CARTAO_DEBITO | ❌ Não | 3 |
| 4 | PIX | PIX | ❌ Não | 4 |

---

## 🔧 Estrutura da Tabela

```sql
CREATE TABLE tipos_pagamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    codigo VARCHAR(30) NOT NULL UNIQUE,
    descricao TEXT,
    icone_url VARCHAR(500),
    ativo BOOLEAN NOT NULL DEFAULT true,
    requer_troco BOOLEAN NOT NULL DEFAULT false,
    ordem_exibicao INT DEFAULT 0,
    criado_em TIMESTAMP NOT NULL
);
```

---

## 📝 Campos Explicados

### `nome`
- Nome exibido para o usuário
- Exemplo: "Dinheiro", "Cartão de Crédito"

### `codigo`
- Identificador único do tipo
- Usado no código da aplicação
- Formato: SNAKE_CASE
- Exemplo: "DINHEIRO", "CARTAO_CREDITO"

### `descricao`
- Descrição detalhada do tipo de pagamento
- Exibida para o usuário quando necessário

### `icone_url`
- URL do ícone ou emoji
- Usado na interface do usuário
- Exemplo: "💵", "💳", "📱"

### `ativo`
- Indica se o tipo está ativo
- `true`: Disponível para uso
- `false`: Desabilitado (não aparece para usuário)

### `requer_troco`
- Indica se precisa informar troco
- `true`: Usuário deve informar valor do troco (ex: Dinheiro)
- `false`: Não precisa informar troco

### `ordem_exibicao`
- Ordem de exibição na lista
- Menor número = aparece primeiro
- Usado para ordenar os tipos na interface

---

## 🔄 Mapeamento Frontend ↔ Backend

### Atualmente (Hardcoded)
```typescript
// CheckoutNovo.tsx
const tipoPagamentoMap: Record<string, number> = {
  'dinheiro': 1,
  'cartao-credito': 2,
  'cartao-debito': 3,
  'pix': 4,
};
```

### Recomendado (Dinâmico)
```typescript
// Buscar da API
const tiposPagamento = await tipoPagamentoService.listar();

// Mapear por código
const tipoPagamentoMap = tiposPagamento.reduce((map, tipo) => {
  map[tipo.codigo.toLowerCase().replace('_', '-')] = tipo.id;
  return map;
}, {});
```

---

## 🧪 Queries Úteis

### Listar Todos os Tipos
```sql
SELECT * FROM tipos_pagamento ORDER BY ordem_exibicao;
```

### Listar Apenas Ativos
```sql
SELECT * FROM tipos_pagamento 
WHERE ativo = true 
ORDER BY ordem_exibicao;
```

### Desativar um Tipo
```sql
UPDATE tipos_pagamento 
SET ativo = false 
WHERE codigo = 'VALE_REFEICAO';
```

### Ativar um Tipo
```sql
UPDATE tipos_pagamento 
SET ativo = true 
WHERE codigo = 'VALE_REFEICAO';
```

### Alterar Ordem de Exibição
```sql
UPDATE tipos_pagamento 
SET ordem_exibicao = 10 
WHERE codigo = 'PIX';
```

### Adicionar Novo Tipo
```sql
INSERT INTO tipos_pagamento (
    nome, codigo, descricao, icone_url, 
    ativo, requer_troco, ordem_exibicao, criado_em
) VALUES (
    'Boleto', 
    'BOLETO', 
    'Pagamento via boleto bancário', 
    '🧾', 
    true, 
    false, 
    7, 
    NOW()
);
```

### Verificar Tipos que Requerem Troco
```sql
SELECT nome, codigo 
FROM tipos_pagamento 
WHERE requer_troco = true 
AND ativo = true;
```

---

## 🎯 Próximos Passos

### 1. Criar Endpoint Público
```java
// TipoPagamentoAPI.java
@GetMapping("/public/v1/tipos-pagamento")
List<TipoPagamentoResponse> listarAtivos();
```

### 2. Atualizar Frontend
```typescript
// tipoPagamento.service.ts
export const tipoPagamentoService = {
  listarAtivos: async (): Promise<TipoPagamento[]> => {
    const { data } = await api.get('/public/v1/tipos-pagamento');
    return data;
  },
};
```

### 3. Usar no Checkout
```typescript
// CheckoutNovo.tsx
const [tiposPagamento, setTiposPagamento] = useState<TipoPagamento[]>([]);

useEffect(() => {
  const carregarTiposPagamento = async () => {
    const tipos = await tipoPagamentoService.listarAtivos();
    setTiposPagamento(tipos);
  };
  carregarTiposPagamento();
}, []);
```

---

## ✅ Checklist de Implementação

- [x] Criar script SQL
- [x] Documentar estrutura
- [ ] Executar script no banco
- [ ] Verificar dados inseridos
- [ ] Criar endpoint público no backend
- [ ] Criar serviço no frontend
- [ ] Atualizar CheckoutNovo para buscar da API
- [ ] Remover mapeamento hardcoded
- [ ] Testar fluxo completo

---

## 🐛 Troubleshooting

### Erro: Duplicate entry for key 'codigo'
**Causa:** Já existem tipos de pagamento cadastrados

**Solução:**
```sql
-- Verificar tipos existentes
SELECT * FROM tipos_pagamento;

-- Opção 1: Limpar e reinserir
DELETE FROM tipos_pagamento;
-- Executar insert novamente

-- Opção 2: Atualizar existentes
UPDATE tipos_pagamento SET nome = 'Dinheiro' WHERE codigo = 'DINHEIRO';
```

### Erro: Table 'tipos_pagamento' doesn't exist
**Causa:** Tabela não foi criada

**Solução:**
```bash
# Executar migrations do Flyway
mvn flyway:migrate

# Ou verificar se a migration existe
ls devmaster/src/main/resources/db/migration/
```

### IDs Diferentes do Esperado
**Causa:** Auto-increment pode gerar IDs diferentes

**Solução:**
- Não depender de IDs fixos no código
- Sempre buscar tipos de pagamento da API
- Mapear por `codigo` em vez de `id`

---

## 📚 Referências

- Entidade: `devmaster/src/main/java/com/devmaster/domain/TipoPagamento.java`
- Migration: `devmaster/src/main/resources/db/migration/V*__create_tipos_pagamento.sql`
- Frontend: `devmaster-web/src/pages/public/CheckoutNovo.tsx`

---

## ✅ Status

**SCRIPTS CRIADOS** - Pronto para executar no banco de dados!

Execute o script e os tipos de pagamento estarão disponíveis para uso no sistema.
