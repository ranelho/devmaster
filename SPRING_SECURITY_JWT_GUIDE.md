# 🔒 Guia Completo: Spring Security + JWT

## 📋 Índice

1. [Visão Geral](#visão-geral)
2. [Arquitetura](#arquitetura)
3. [Componentes](#componentes)
4. [Configuração](#configuração)
5. [Swagger com JWT](#swagger-com-jwt)
6. [Testando](#testando)
7. [Troubleshooting](#troubleshooting)

## Visão Geral

Este projeto implementa autenticação JWT usando Spring Security com validação de token via microserviço externo.

### 🎯 Características Principais

- ✅ **Spring Security 6**: Framework de segurança nativo
- ✅ **JWT Bearer Token**: Autenticação stateless
- ✅ **Validação Externa**: Microserviço de autenticação
- ✅ **Swagger Integrado**: Cadeado de autenticação (🔒)
- ✅ **Circuit Breaker**: Resiliência com Resilience4j
- ✅ **Configurável**: Liga/desliga por ambiente

## Arquitetura

```
┌─────────────────┐
│   Cliente       │
│  (Browser/App)  │
└────────┬────────┘
         │ 1. Request + JWT
         ▼
┌─────────────────────────────────────┐
│   Spring Security Filter Chain      │
│                                     │
│  ┌──────────────────────────────┐  │
│  │  JwtAuthenticationFilter     │  │
│  │  - Extrai token do header    │  │
│  │  - Valida com auth service   │  │
│  │  - Configura SecurityContext │  │
│  └──────────┬───────────────────┘  │
└─────────────┼───────────────────────┘
              │ 2. Valida token
              ▼
┌─────────────────────────────────────┐
│   TokenValidationService            │
│   - Circuit Breaker                 │
│   - Retry                           │
│   - Timeout                         │
└──────────────┬──────────────────────┘
               │ 3. POST /validate-token
               ▼
┌─────────────────────────────────────┐
│   Microserviço de Autenticação      │
│   (Externo)                         │
└─────────────────────────────────────┘
```

## Componentes

### 1. JwtAuthenticationFilter

**Localização**: `src/main/java/com/devmaster/security/filter/JwtAuthenticationFilter.java`

**Responsabilidades**:
- Intercepta todas as requisições HTTP
- Extrai token do header `Authorization: Bearer <token>`
- Valida token usando `TokenValidationService`
- Configura `SecurityContext` com autenticação válida
- Retorna 401 para tokens inválidos

**Código Principal**:
```java
@Override
protected void doFilterInternal(HttpServletRequest request, 
                                HttpServletResponse response, 
                                FilterChain filterChain) {
    String authHeader = request.getHeader("Authorization");
    
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        
        if (tokenValidationService.validateToken(token)) {
            // Configura autenticação no SecurityContext
            UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
    
    filterChain.doFilter(request, response);
}
```

### 2. SecurityConfig

**Localização**: `src/main/java/com/devmaster/security/config/SecurityConfig.java`

**Responsabilidades**:
- Configura Spring Security
- Define endpoints públicos e protegidos
- Adiciona filtro JWT à cadeia de segurança
- Desabilita CSRF (API stateless)
- Configura session management como STATELESS

**Endpoints Públicos**:
```java
.requestMatchers(
    "/api/swagger/**",
    "/api/swagger-ui/**",
    "/api/api-docs/**",
    "/api/actuator/**",
    "/api/health/**"
).permitAll()
```

### 3. TokenValidationService

**Localização**: `src/main/java/com/devmaster/security/service/TokenValidationService.java`

**Responsabilidades**:
- Comunica com microserviço de autenticação
- Valida token via POST request
- Implementa Circuit Breaker
- Implementa Retry
- Implementa Timeout
- Fallback seguro (nega acesso)

**Resiliência**:
```java
@CircuitBreaker(name = "auth-service", fallbackMethod = "validateTokenFallback")
@Retry(name = "auth-service")
public boolean validateToken(String token) {
    // Valida token com serviço externo
}

private boolean validateTokenFallback(String token, Exception e) {
    log.error("Fallback ativado - negando acesso");
    return false; // Segurança: nega acesso em caso de falha
}
```

### 4. SwaggerConfig

**Localização**: `src/main/java/com/devmaster/config/SwaggerConfig.java`

**Responsabilidades**:
- Configura OpenAPI/Swagger
- Adiciona esquema de segurança JWT
- Habilita botão "Authorize" (🔒)

**Configuração JWT**:
```java
.components(new Components()
    .addSecuritySchemes("Bearer Authentication", 
        new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
    ))
.addSecurityItem(new SecurityRequirement()
    .addList("Bearer Authentication"));
```

## Configuração

### Variáveis de Ambiente

**Arquivo**: `.env`

```bash
# URL do serviço de autenticação
AUTH_SERVICE_URL=http://localhost:8080

# Habilitar/desabilitar segurança
SECURITY_INTERCEPTOR_ENABLED=true
```

### Por Ambiente

#### Desenvolvimento (`application-develop.yaml`)
```yaml
security:
  auth:
    service:
      url: ${AUTH_SERVICE_URL:http://localhost:8080}
  interceptor:
    enabled: ${SECURITY_INTERCEPTOR_ENABLED:false}  # Desabilitado
```

#### Staging (`application-staging.yaml`)
```yaml
security:
  auth:
    service:
      url: ${AUTH_SERVICE_URL:https://auth-staging.example.com}
  interceptor:
    enabled: ${SECURITY_INTERCEPTOR_ENABLED:true}  # Habilitado
```

#### Produção (`application-master.yaml`)
```yaml
security:
  auth:
    service:
      url: ${AUTH_SERVICE_URL:https://auth.example.com}
  interceptor:
    enabled: ${SECURITY_INTERCEPTOR_ENABLED:true}  # Habilitado
```

### Resilience4j

**Circuit Breaker**:
```yaml
resilience4j:
  circuitbreaker:
    instances:
      auth-service:
        sliding-window-size: 15
        failure-rate-threshold: 40
        wait-duration-in-open-state: 20s
```

**Retry**:
```yaml
resilience4j:
  retry:
    instances:
      auth-service:
        max-attempts: 2
        wait-duration: 500ms
```

**Timeout**:
```yaml
resilience4j:
  timelimiter:
    instances:
      auth-service:
        timeout-duration: 5s
```

## Swagger com JWT

### 🔒 Como Usar o Cadeado

1. **Abra o Swagger UI**
   ```
   http://localhost:8081/api/swagger
   ```

2. **Localize o botão "Authorize"**
   - Está no canto superior direito
   - Ícone de cadeado (🔒)

3. **Clique em "Authorize"**
   - Abre modal de autenticação

4. **Insira o Token JWT**
   - Campo: "Value"
   - Formato: `seu-token-aqui` (SEM "Bearer")
   - Exemplo: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

5. **Clique em "Authorize"**
   - Token é salvo na sessão do Swagger

6. **Clique em "Close"**
   - Modal fecha

7. **Teste qualquer endpoint**
   - Token é incluído automaticamente
   - Header: `Authorization: Bearer seu-token-aqui`

### 📸 Visual

```
┌─────────────────────────────────────────┐
│  Swagger UI                    🔒 Authorize │
├─────────────────────────────────────────┤
│                                         │
│  Available authorizations               │
│                                         │
│  Bearer Authentication (http, Bearer)   │
│  ┌───────────────────────────────────┐ │
│  │ Value: [seu-token-aqui]           │ │
│  └───────────────────────────────────┘ │
│                                         │
│  [Authorize]  [Close]                   │
└─────────────────────────────────────────┘
```

## Testando

### 1. Obter Token

```bash
# Fazer login no serviço de autenticação
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user",
    "password": "password"
  }'

# Resposta esperada:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

### 2. Testar com cURL

```bash
# Requisição com token válido
curl -X GET http://localhost:8081/api/clientes \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

# Resposta: 200 OK + dados

# Requisição sem token
curl -X GET http://localhost:8081/api/clientes

# Resposta: 401 Unauthorized
{
  "error": "Unauthorized",
  "message": "Token não fornecido",
  "status": 401
}

# Requisição com token inválido
curl -X GET http://localhost:8081/api/clientes \
  -H "Authorization: Bearer token-invalido"

# Resposta: 401 Unauthorized
{
  "error": "Unauthorized",
  "message": "Token inválido ou expirado",
  "status": 401
}
```

### 3. Testar Endpoint Público

```bash
# Actuator health (não requer token)
curl -X GET http://localhost:8081/api/actuator/health

# Resposta: 200 OK
{
  "status": "UP"
}
```

### 4. Testar Circuit Breaker

```bash
# Parar o serviço de auth
# Fazer várias requisições para abrir o circuito

for i in {1..10}; do
  curl -X GET http://localhost:8081/api/clientes \
    -H "Authorization: Bearer test-token"
done

# Verificar logs - circuit breaker deve abrir
# Próximas requisições falham imediatamente (fallback)
```

## Troubleshooting

### ❌ Problema: Swagger retorna 401

**Sintomas**:
- Swagger UI carrega mas endpoints retornam 401
- Mesmo após clicar em "Authorize"

**Soluções**:

1. **Verificar endpoints públicos**:
   ```java
   // SecurityConfig.java
   .requestMatchers(
       "/api/swagger/**",
       "/api/swagger-ui/**",
       "/api/api-docs/**"
   ).permitAll()
   ```

2. **Verificar padrão de URL**:
   ```bash
   # URL correta
   http://localhost:8081/api/swagger
   
   # URL incorreta
   http://localhost:8081/swagger  # Falta /api
   ```

3. **Desabilitar segurança temporariamente**:
   ```bash
   # .env
   SECURITY_INTERCEPTOR_ENABLED=false
   ```

### ❌ Problema: Token válido mas retorna 401

**Sintomas**:
- Token funciona em outro sistema
- Retorna 401 neste projeto

**Soluções**:

1. **Verificar formato do header**:
   ```bash
   # Correto
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   
   # Incorreto
   Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...  # Falta "Bearer"
   ```

2. **Verificar serviço de auth**:
   ```bash
   # Testar diretamente
   curl -X POST http://localhost:8080/api/auth/validate-token \
     -H "Authorization: Bearer seu-token"
   
   # Deve retornar 200 OK
   ```

3. **Verificar logs**:
   ```yaml
   # application.yaml
   logging:
     level:
       com.devmaster.security: DEBUG
       org.springframework.security: DEBUG
   ```

### ❌ Problema: Circuit breaker sempre aberto

**Sintomas**:
- Todas as requisições falham
- Logs mostram "Circuit breaker OPEN"

**Soluções**:

1. **Verificar conectividade**:
   ```bash
   curl -X POST http://localhost:8080/api/auth/validate-token
   ```

2. **Aumentar threshold**:
   ```yaml
   resilience4j:
     circuitbreaker:
       instances:
         auth-service:
           failure-rate-threshold: 60  # Era 40
   ```

3. **Reset manual** (desenvolvimento):
   ```bash
   # Reiniciar aplicação
   mvn spring-boot:run
   ```

### ❌ Problema: Performance lenta

**Sintomas**:
- Requisições demoram muito
- Timeout frequente

**Soluções**:

1. **Reduzir timeout**:
   ```yaml
   resilience4j:
     timelimiter:
       instances:
         auth-service:
           timeout-duration: 3s  # Era 5s
   ```

2. **Implementar cache** (futuro):
   ```java
   @Cacheable(value = "tokens", key = "#token")
   public boolean validateToken(String token) {
       // Validação
   }
   ```

3. **Verificar latência do auth service**:
   ```bash
   time curl -X POST http://localhost:8080/api/auth/validate-token
   ```

## Logs

### Habilitar Logs Detalhados

```yaml
logging:
  level:
    com.devmaster.security: DEBUG
    org.springframework.security: DEBUG
    io.github.resilience4j: DEBUG
```

### Exemplos de Logs

**Token válido**:
```
DEBUG JwtAuthenticationFilter : Token validado com sucesso para: GET /api/clientes
DEBUG SecurityContextHolder : Set SecurityContext to UsernamePasswordAuthenticationToken
```

**Token inválido**:
```
WARN  JwtAuthenticationFilter : Token inválido para: GET /api/clientes
DEBUG JwtAuthenticationFilter : Retornando 401 Unauthorized
```

**Circuit breaker aberto**:
```
ERROR TokenValidationService : Fallback ativado para validação de token
WARN  CircuitBreaker : Circuit breaker 'auth-service' changed state from CLOSED to OPEN
```

## Segurança

### ✅ Boas Práticas Implementadas

- **Stateless**: Sem sessões no servidor
- **CSRF Disabled**: Apropriado para APIs REST
- **Token Validation**: Validação externa
- **Circuit Breaker**: Proteção contra falhas
- **Fallback Seguro**: Nega acesso em erro
- **Logs Estruturados**: Auditoria
- **Endpoints Públicos**: Apenas necessário

### 🔒 Recomendações Adicionais

1. **HTTPS em Produção**
   ```yaml
   server:
     ssl:
       enabled: true
       key-store: classpath:keystore.p12
       key-store-password: ${SSL_PASSWORD}
   ```

2. **Rotação de Tokens**
   - Implementar refresh tokens
   - Expiração curta (15-30 min)

3. **Rate Limiting**
   ```java
   @RateLimiter(name = "api")
   public ResponseEntity<?> endpoint() {
       // ...
   }
   ```

4. **Auditoria**
   ```java
   @Aspect
   public class SecurityAuditAspect {
       @AfterReturning("@annotation(Secured)")
       public void auditAccess(JoinPoint joinPoint) {
           // Log acesso
       }
   }
   ```

## Próximos Passos

- [ ] Cache de tokens validados (Redis)
- [ ] Refresh token automático
- [ ] Rate limiting por usuário
- [ ] Auditoria em banco de dados
- [ ] Métricas de segurança (Prometheus)
- [ ] Blacklist de tokens revogados
- [ ] Suporte a múltiplos issuers
- [ ] Validação de claims customizados
- [ ] Integração com OAuth2/OIDC
