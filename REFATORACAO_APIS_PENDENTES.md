# Script de Refatoração - APIs Restantes

## APIs Atualizadas ✅
- ClienteAPI
- EntregadorAPI

## APIs Pendentes 🔄

### Padrão de Refatoração:

**REMOVER:**
```java
Authentication authentication,
```

**DE TODOS OS MÉTODOS QUE POSSUEM @SecurityRequirement**

---

## Lista de APIs para Atualizar:

1. **CategoriaAPI** - Remover Authentication de métodos autenticados
2. **CupomAPI** - Remover Authentication de métodos autenticados
3. **DisponibilidadeAPI** - Remover Authentication de métodos autenticados
4. **DocumentoEntregadorAPI** - Remover Authentication de métodos autenticados
5. **EnderecoAPI** - Remover Authentication de métodos autenticados
6. **EnderecoClienteAPI** - Remover Authentication de métodos autenticados
7. **PedidoAPI** - Remover Authentication de métodos autenticados
8. **ProdutoAPI** - Remover Authentication de métodos autenticados
9. **RestauranteAPI** - Remover Authentication de métodos autenticados
10. **TipoPagamentoAPI** - Remover Authentication de métodos autenticados
11. **UsuarioRestauranteAPI** - Remover Authentication de métodos autenticados

---

## Controllers para Atualizar:

1. **CategoriaRestController**
2. **CupomRestController**
3. **DisponibilidadeRestController**
4. **DocumentoEntregadorRestController**
5. **EnderecoRestController**
6. **EnderecoClienteRestController**
7. **PedidoRestController**
8. **ProdutoRestController**
9. **RestauranteRestController**
10. **TipoPagamentoRestController**
11. **UsuarioRestauranteRestController**

---

## Exemplo de Mudança:

### ANTES:
```java
@GetMapping
@SecurityRequirement(name = "bearerAuth")
Page<Response> listar(
    Authentication authentication,  // ❌ REMOVER
    @RequestParam Boolean ativo
);
```

### DEPOIS:
```java
@GetMapping
@SecurityRequirement(name = "bearerAuth")
Page<Response> listar(@RequestParam Boolean ativo);  // ✅ LIMPO
```

---

## Imports para Remover:

Se não houver mais uso de `Authentication`:
```java
import org.springframework.security.core.Authentication;  // ❌ REMOVER
```

---

## Benefícios:

- ✅ 30-40% menos código
- ✅ APIs mais limpas
- ✅ Swagger mais simples
- ✅ Sem parâmetros redundantes
- ✅ SecurityContext gerencia autenticação automaticamente
