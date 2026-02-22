# Refatoração de Endpoints Públicos - Análise e Plano

## 📊 Situação Atual

### Duplicidades Identificadas

#### 1. Cliente - DUPLICAÇÃO TOTAL ❌

**ClienteAPI** (`/clientes`)
- ✅ POST `/clientes` - Criar cliente (público)
- ✅ GET `/clientes/{id}` - Buscar por ID (público)
- ✅ GET `/clientes/telefone/{telefone}` - Buscar por telefone (público)
- 🔒 GET `/clientes` - Listar com paginação (autenticado)
- ✅ PUT `/clientes/{id}` - Atualizar (público)
- 🔒 DELETE `/clientes/{id}` - Desativar (autenticado - ADMIN)
- 🔒 PATCH `/clientes/{id}/reativar` - Reativar (autenticado - ADMIN)

**PublicClienteAPI** (`/public/v1/clientes`) - DUPLICADO
- ✅ POST `/public/v1/clientes` - Criar cliente (público)
- ✅ GET `/public/v1/clientes/{id}` - Buscar por ID (público)
- ✅ GET `/public/v1/clientes/telefone/{telefone}` - Buscar por telefone (público)

**Problema:** 3 endpoints completamente duplicados!

#### 2. Endereço Cliente - DUPLICAÇÃO PARCIAL ⚠️

**EnderecoClienteAPI** (`/clientes/{clienteId}/enderecos`)
- ✅ POST - Adicionar endereço (público)
- ✅ GET - Listar endereços (público)
- ✅ GET `/{enderecoId}` - Buscar endereço (público)
- ✅ GET `/padrao` - Buscar endereço padrão (público)
- ✅ PUT `/{enderecoId}` - Atualizar endereço (público)
- ✅ PATCH `/{enderecoId}/padrao` - Definir como padrão (público)
- ✅ DELETE `/{enderecoId}` - Remover endereço (público)

**PublicEnderecoClienteAPI** (`/public/v1/clientes/{clienteId}/enderecos`) - DUPLICADO PARCIAL
- ✅ POST - Adicionar endereço (público)
- ✅ GET - Listar endereços (público)
- ✅ GET `/{enderecoId}` - Buscar endereço (público)
- ✅ GET `/principal` - Buscar endereço principal (público) ⚠️ Nome diferente: `/padrao` vs `/principal`

**Problema:** 4 endpoints duplicados + inconsistência de nomenclatura!

#### 3. Pedidos - SEM ENDPOINT PÚBLICO NO DEVMASTER ✅

**PedidoAPI** (`/v1/pedidos`)
- 🔒 Todos os endpoints requerem autenticação (`X-User-Id`)
- Não há endpoints públicos para criar pedidos

**Observação:** A API de Entrega tem `PedidoPublicoAPI` (`/api/public/pedidos`), mas é outro microserviço.

#### 4. Endereço (CEP e Entrega) - OK ✅

**EnderecoAPI** (`/public/v1/enderecos`)
- ✅ GET `/cep/{cep}` - Buscar por CEP (público)
- ✅ POST `/calcular-entrega` - Calcular entrega (público)

**Status:** Sem duplicações, endpoints únicos e bem definidos.

---

## 🎯 Plano de Refatoração

### Objetivo
Consolidar endpoints públicos, eliminar duplicações e manter consistência na API.

### Estratégia

#### Opção 1: Manter apenas `/public/v1/*` (RECOMENDADO) ✅

**Vantagens:**
- Separação clara entre público e privado
- Facilita configuração de segurança
- Padrão REST moderno
- Facilita rate limiting por path

**Desvantagens:**
- Requer atualização do frontend
- Breaking change para clientes existentes

#### Opção 2: Manter apenas paths sem `/public` ⚠️

**Vantagens:**
- Menos verboso
- Não requer mudança no frontend

**Desvantagens:**
- Mistura endpoints públicos e privados no mesmo path
- Dificulta configuração de segurança
- Menos explícito

### ✅ Decisão: Opção 1 - Consolidar em `/public/v1/*`

---

## 📋 Ações de Refatoração

### 1. Cliente

#### Remover
- ❌ `PublicClienteAPI.java`
- ❌ `PublicClienteRestController.java`

#### Manter e Ajustar
- ✅ `ClienteAPI.java` - Manter todos os endpoints
- ✅ `ClienteRestController.java` - Ajustar path para `/public/v1/clientes`

#### Mudanças em ClienteAPI
```java
// ANTES
@RequestMapping("/clientes")

// DEPOIS
@RequestMapping("/public/v1/clientes")
```

#### Endpoints Finais
```
✅ POST   /public/v1/clientes                    (público)
✅ GET    /public/v1/clientes/{id}               (público)
✅ GET    /public/v1/clientes/telefone/{telefone} (público)
✅ PUT    /public/v1/clientes/{id}               (público)
🔒 GET    /public/v1/clientes                    (autenticado - listar)
🔒 DELETE /public/v1/clientes/{id}               (autenticado - ADMIN)
🔒 PATCH  /public/v1/clientes/{id}/reativar      (autenticado - ADMIN)
```

### 2. Endereço Cliente

#### Remover
- ❌ `PublicEnderecoClienteAPI.java`
- ❌ `PublicEnderecoClienteRestController.java`

#### Manter e Ajustar
- ✅ `EnderecoClienteAPI.java` - Ajustar path
- ✅ `EnderecoClienteRestController.java` - Ajustar path

#### Mudanças em EnderecoClienteAPI
```java
// ANTES
@RequestMapping("/clientes/{clienteId}/enderecos")

// DEPOIS
@RequestMapping("/public/v1/clientes/{clienteId}/enderecos")
```

#### Padronizar Nomenclatura
```java
// ANTES (inconsistente)
GET /padrao      (EnderecoClienteAPI)
GET /principal   (PublicEnderecoClienteAPI)

// DEPOIS (padronizado)
GET /padrao      (único endpoint)
```

#### Endpoints Finais
```
✅ POST   /public/v1/clientes/{clienteId}/enderecos                (público)
✅ GET    /public/v1/clientes/{clienteId}/enderecos                (público)
✅ GET    /public/v1/clientes/{clienteId}/enderecos/{enderecoId}   (público)
✅ GET    /public/v1/clientes/{clienteId}/enderecos/padrao         (público)
✅ PUT    /public/v1/clientes/{clienteId}/enderecos/{enderecoId}   (público)
✅ PATCH  /public/v1/clientes/{clienteId}/enderecos/{enderecoId}/padrao (público)
✅ DELETE /public/v1/clientes/{clienteId}/enderecos/{enderecoId}   (público)
```

### 3. Pedidos

#### Criar Novo
- ✅ `PublicPedidoAPI.java` - Nova interface
- ✅ `PublicPedidoRestController.java` - Nova implementação

#### Path
```
/public/v1/pedidos
```

#### Endpoints Necessários (Checkout)
```
✅ POST   /public/v1/pedidos                    (criar pedido - público)
✅ GET    /public/v1/pedidos/{id}               (buscar pedido - público)
✅ GET    /public/v1/pedidos/numero/{numero}    (buscar por número - público)
✅ GET    /public/v1/pedidos/cliente/{clienteId} (listar pedidos do cliente - público)
```

#### Manter Endpoints Privados
```
🔒 PedidoAPI (/v1/pedidos) - Todos os endpoints de gestão (autenticados)
```

### 4. Endereço (CEP e Entrega)

#### Status
✅ Já está correto - Nenhuma ação necessária

```
✅ GET  /public/v1/enderecos/cep/{cep}
✅ POST /public/v1/enderecos/calcular-entrega
```

---

## 🔧 Implementação

### Fase 1: Ajustar Paths Existentes

#### 1.1 ClienteAPI
```java
@RequestMapping("/public/v1/clientes")
public interface ClienteAPI {
    // Manter todos os métodos
}
```

#### 1.2 EnderecoClienteAPI
```java
@RequestMapping("/public/v1/clientes/{clienteId}/enderecos")
public interface EnderecoClienteAPI {
    // Manter todos os métodos
    // Remover endpoint /principal (duplicado)
}
```

### Fase 2: Remover Duplicados

#### 2.1 Deletar Arquivos
```bash
rm devmaster/src/main/java/com/devmaster/application/api/PublicClienteAPI.java
rm devmaster/src/main/java/com/devmaster/application/api/PublicClienteRestController.java
rm devmaster/src/main/java/com/devmaster/application/api/PublicEnderecoClienteAPI.java
rm devmaster/src/main/java/com/devmaster/application/api/PublicEnderecoClienteRestController.java
```

### Fase 3: Criar Endpoint Público de Pedidos

#### 3.1 PublicPedidoAPI.java
```java
@Tag(name = "Pedidos Público", description = "APIs públicas para pedidos (checkout)")
@RequestMapping("/public/v1/pedidos")
public interface PublicPedidoAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PedidoResponse criarPedido(@Valid @RequestBody PedidoRequest request);
    
    @GetMapping("/{id}")
    PedidoResponse buscarPedido(@PathVariable Long id);
    
    @GetMapping("/numero/{numero}")
    PedidoResponse buscarPorNumero(@PathVariable String numero);
    
    @GetMapping("/cliente/{clienteId}")
    List<PedidoResumoResponse> listarPedidosCliente(@PathVariable Long clienteId);
}
```

#### 3.2 PublicPedidoRestController.java
```java
@RestController
@RequiredArgsConstructor
public class PublicPedidoRestController implements PublicPedidoAPI {
    private final PedidoService pedidoService;
    
    // Implementar métodos delegando para PedidoService
}
```

### Fase 4: Atualizar Frontend

#### 4.1 Atualizar URLs nos Services
```typescript
// ANTES
const API_URL = '/clientes';
const API_URL = '/clientes/{clienteId}/enderecos';

// DEPOIS
const API_URL = '/public/v1/clientes';
const API_URL = '/public/v1/clientes/{clienteId}/enderecos';
const API_URL = '/public/v1/pedidos';
```

#### 4.2 Arquivos a Atualizar
- `devmaster-web/src/services/cliente.service.ts`
- `devmaster-web/src/services/endereco.service.ts`
- `devmaster-web/src/services/pedido.service.ts` (criar)

---

## 📊 Resumo de Mudanças

### Arquivos a Deletar (4)
1. ❌ `PublicClienteAPI.java`
2. ❌ `PublicClienteRestController.java`
3. ❌ `PublicEnderecoClienteAPI.java`
4. ❌ `PublicEnderecoClienteRestController.java`

### Arquivos a Modificar (4)
1. ✏️ `ClienteAPI.java` - Mudar path para `/public/v1/clientes`
2. ✏️ `ClienteRestController.java` - Ajustar implementação
3. ✏️ `EnderecoClienteAPI.java` - Mudar path para `/public/v1/clientes/{clienteId}/enderecos`
4. ✏️ `EnderecoClienteRestController.java` - Ajustar implementação

### Arquivos a Criar (2)
1. ➕ `PublicPedidoAPI.java` - Nova interface
2. ➕ `PublicPedidoRestController.java` - Nova implementação

### Frontend a Atualizar (3)
1. ✏️ `cliente.service.ts`
2. ✏️ `endereco.service.ts`
3. ➕ `pedido.service.ts` (criar)

---

## ✅ Benefícios da Refatoração

1. **Eliminação de Duplicação**
   - 7 endpoints duplicados removidos
   - Código mais limpo e manutenível

2. **Consistência**
   - Todos os endpoints públicos em `/public/v1/*`
   - Nomenclatura padronizada

3. **Segurança**
   - Separação clara entre público e privado
   - Facilita configuração de CORS e rate limiting

4. **Documentação**
   - Swagger mais organizado
   - Endpoints agrupados logicamente

5. **Manutenibilidade**
   - Menos código para manter
   - Mudanças em um único lugar

---

## 🚀 Próximos Passos

1. ✅ Revisar e aprovar plano de refatoração
2. ⏳ Implementar Fase 1 (ajustar paths)
3. ⏳ Implementar Fase 2 (remover duplicados)
4. ⏳ Implementar Fase 3 (criar endpoint público de pedidos)
5. ⏳ Implementar Fase 4 (atualizar frontend)
6. ⏳ Testar todos os fluxos
7. ⏳ Atualizar documentação
8. ⏳ Deploy

---

## ⚠️ Considerações

### Breaking Changes
- URLs antigas deixarão de funcionar
- Requer atualização do frontend
- Considerar período de transição com ambos os endpoints

### Estratégia de Migração
1. Implementar novos endpoints
2. Manter antigos como deprecated
3. Atualizar frontend
4. Remover endpoints antigos após período de transição

### Testes Necessários
- ✅ Criar cliente (público)
- ✅ Buscar cliente por telefone (público)
- ✅ Adicionar endereço (público)
- ✅ Listar endereços (público)
- ✅ Criar pedido (público)
- ✅ Buscar pedido (público)
- ✅ Calcular entrega (público)
- 🔒 Listar clientes (autenticado)
- 🔒 Desativar cliente (autenticado - ADMIN)
