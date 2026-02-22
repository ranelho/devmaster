# 🚀 Melhorias Sugeridas - API Delivery

## 1. ❌ REMOVER `X-User-Id` Header (CRÍTICO)

### Problema
- Header manual é redundante
- JWT já contém o userId
- Risco de manipulação/inconsistência
- Código verboso e repetitivo

### Solução
Usar `SecurityContextHolder` ou `@AuthenticationPrincipal`:

```java
// ❌ ANTES (Ruim)
@GetMapping
Page<ClienteResponse> listarClientes(
    @RequestHeader("X-User-Id") UUID usuarioId,  // ❌ Desnecessário
    @RequestParam Boolean ativo
) {
    return service.listarClientes(usuarioId, ativo);
}

// ✅ DEPOIS (Bom)
@GetMapping
Page<ClienteResponse> listarClientes(
    Authentication auth,  // ✅ Spring injeta automaticamente
    @RequestParam Boolean ativo
) {
    UUID usuarioId = UUID.fromString(auth.getName());
    return service.listarClientes(usuarioId, ativo);
}

// ✅ MELHOR AINDA (Annotation)
@GetMapping
Page<ClienteResponse> listarClientes(
    @AuthenticationPrincipal String username,  // ✅ Direto do token
    @RequestParam Boolean ativo
) {
    UUID usuarioId = UUID.fromString(username);
    return service.listarClientes(usuarioId, ativo);
}
```

---

## 2. 🔐 Simplificar Autenticação nos Services

### Problema
Services recebem `usuarioId` mas muitas vezes não usam para validação real.

### Solução
Criar um `SecurityService` centralizado:

```java
@Service
public class SecurityService {
    
    public UUID getUsuarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }
        return UUID.fromString(auth.getName());
    }
    
    public boolean hasRole(String role) {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals(role));
    }
    
    public boolean isSuperAdmin() {
        return hasRole("ROLE_SUPER_ADMIN");
    }
}
```

Uso nos services:
```java
@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {
    
    private final SecurityService securityService;
    
    public RestauranteResponse criar(RestauranteRequest request) {
        UUID usuarioId = securityService.getUsuarioAutenticado();
        // ... lógica
    }
}
```

---

## 3. 🎯 Separar APIs Públicas e Privadas Melhor

### Estrutura Atual (Confusa)
```
/public/v1/clientes          → Público
/v1/clientes                 → Privado (mas não está claro)
```

### Estrutura Sugerida
```
/api/public/v1/clientes      → Público (sem auth)
/api/admin/v1/clientes       → Privado (com auth)
```

Configuração no `SecurityConfig`:
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/public/**").permitAll()
    .requestMatchers("/api/admin/**").authenticated()
    .anyRequest().authenticated()
)
```

---

## 4. 📦 Criar DTOs de Contexto

### Problema
Passar `usuarioId` em todos os métodos polui assinaturas.

### Solução
```java
@Getter
@Builder
public class UserContext {
    private UUID userId;
    private String username;
    private Set<String> roles;
    private Long restauranteId;  // Se aplicável
    
    public boolean isSuperAdmin() {
        return roles.contains("ROLE_SUPER_ADMIN");
    }
    
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}

// Service
@Service
public class UserContextService {
    
    public UserContext getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Extrair dados do token JWT
        return UserContext.builder()
            .userId(UUID.fromString(auth.getName()))
            .username(auth.getName())
            .roles(auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()))
            .build();
    }
}
```

---

## 5. 🔄 Simplificar Controllers

### Antes (Verboso)
```java
@Override
public EntregadorResponse criarEntregador(
    Authentication authentication, 
    EntregadorRequest request
) {
    validarAutenticacao(authentication);
    UUID usuarioId = UUID.fromString(authentication.getName());
    return entregadorService.criarEntregador(usuarioId, request);
}

private void validarAutenticacao(Authentication authentication) {
    if (authentication == null || authentication.getName() == null) {
        throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
    }
}
```

### Depois (Simples)
```java
@Override
public EntregadorResponse criarEntregador(EntregadorRequest request) {
    return entregadorService.criarEntregador(request);
}

// Service obtém usuário internamente
@Service
public class EntregadorServiceImpl {
    
    private final SecurityService securityService;
    
    public EntregadorResponse criarEntregador(EntregadorRequest request) {
        UUID usuarioId = securityService.getUsuarioAutenticado();
        // ... lógica
    }
}
```

---

## 6. 🎭 Usar AOP para Auditoria

### Problema
Passar `usuarioId` para auditoria em cada método.

### Solução
```java
@Aspect
@Component
public class AuditAspect {
    
    @Autowired
    private SecurityService securityService;
    
    @Before("@annotation(Auditable)")
    public void audit(JoinPoint joinPoint) {
        UUID userId = securityService.getUsuarioAutenticado();
        String method = joinPoint.getSignature().getName();
        log.info("Auditoria: Usuario {} executou {}", userId, method);
    }
}

// Uso
@Auditable
public RestauranteResponse criar(RestauranteRequest request) {
    // Auditoria automática
}
```

---

## 7. 🔒 Validação de Acesso Centralizada

### Criar Annotation Customizada
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRestauranteAccess {
    String restauranteIdParam() default "restauranteId";
}

@Aspect
@Component
public class RestauranteAccessAspect {
    
    @Autowired
    private UsuarioRestauranteService usuarioRestauranteService;
    
    @Autowired
    private SecurityService securityService;
    
    @Before("@annotation(requireAccess)")
    public void checkAccess(JoinPoint joinPoint, RequireRestauranteAccess requireAccess) {
        UUID userId = securityService.getUsuarioAutenticado();
        Long restauranteId = extractRestauranteId(joinPoint, requireAccess.restauranteIdParam());
        
        if (!usuarioRestauranteService.temAcessoAoRestaurante(userId, restauranteId)) {
            throw new ForbiddenException("Sem acesso ao restaurante");
        }
    }
}

// Uso
@RequireRestauranteAccess
public ProdutoResponse criar(Long restauranteId, ProdutoRequest request) {
    // Validação automática de acesso
}
```

---

## 8. 📊 Resumo de Mudanças

### Remover
- ❌ `@RequestHeader("X-User-Id")` em TODOS os endpoints
- ❌ Métodos `validarAutenticacao()` repetidos
- ❌ Passar `usuarioId` manualmente nos services

### Adicionar
- ✅ `SecurityService` centralizado
- ✅ `UserContextService` para contexto completo
- ✅ Annotations customizadas para validação
- ✅ AOP para auditoria e validação de acesso

### Benefícios
- 🎯 Código 50% mais limpo
- 🔒 Mais seguro (sem manipulação de headers)
- 🚀 Mais fácil de manter
- 📦 Menos repetição de código
- ✨ Melhor separação de responsabilidades

---

## 9. 🛠️ Plano de Implementação

### Fase 1 - Infraestrutura (1-2 dias)
1. Criar `SecurityService`
2. Criar `UserContextService`
3. Criar annotations customizadas
4. Criar aspects para validação

### Fase 2 - Refatoração Controllers (2-3 dias)
1. Remover `X-User-Id` de todos os endpoints
2. Usar `Authentication` ou `@AuthenticationPrincipal`
3. Remover métodos `validarAutenticacao()`

### Fase 3 - Refatoração Services (2-3 dias)
1. Remover parâmetro `usuarioId` dos métodos
2. Usar `SecurityService` internamente
3. Adicionar annotations de validação

### Fase 4 - Testes (1-2 dias)
1. Testar autenticação
2. Testar validação de acesso
3. Testar auditoria

---

## 10. 💡 Exemplo Completo de Refatoração

### ANTES
```java
// API
@PostMapping
RestauranteResponse criar(
    @RequestHeader("X-User-Id") UUID usuarioId,
    @RequestBody RestauranteRequest request
);

// Controller
@Override
public RestauranteResponse criar(UUID usuarioId, RestauranteRequest request) {
    return service.criarRestaurante(usuarioId, request);
}

// Service
public RestauranteResponse criarRestaurante(UUID usuarioId, RestauranteRequest request) {
    // Validar se é SUPER_ADMIN manualmente
    // ... lógica
}
```

### DEPOIS
```java
// API
@PostMapping
@PreAuthorize("hasRole('SUPER_ADMIN')")
RestauranteResponse criar(@RequestBody RestauranteRequest request);

// Controller
@Override
public RestauranteResponse criar(RestauranteRequest request) {
    return service.criarRestaurante(request);
}

// Service
@Auditable
public RestauranteResponse criarRestaurante(RestauranteRequest request) {
    UUID usuarioId = securityService.getUsuarioAutenticado();
    // ... lógica
}
```

**Resultado**: Código 60% mais limpo e mais seguro! 🎉
