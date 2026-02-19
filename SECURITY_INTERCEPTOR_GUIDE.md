# Guia do Interceptor de Segurança (Spring Security + JWT)

## Visão Geral

O sistema de segurança utiliza Spring Security com filtro JWT para validar tokens em todas as requisições HTTP, comunicando-se com um microserviço de autenticação externo.

## Componentes

### 1. JwtAuthenticationFilter
Filtro Spring Security que intercepta todas as requisições e valida o token Bearer.

**Características:**
- Extends `OncePerRequestFilter` para garantir execução única por requisição
- Valida token antes de processar a requisição
- Configura o SecurityContext com autenticação válida
- Retorna 401 Unauthorized para tokens inválidos

### 2. SecurityConfig
Configuração do Spring Security com suporte a JWT.

**Características:**
- CSRF desabilitado (API stateless)
- Session management: STATELESS
- Endpoints públicos configurados (Swagger, Actuator)
- Pode ser desabilitado via variável de ambiente

### 3. TokenValidationService
Serviço responsável por validar o token com o microserviço de autenticação.

**Características:**
- Circuit Breaker para proteção contra falhas
- Retry automático em caso de erro
- Timeout configurável
- Fallback que nega acesso em caso de falha

## Configuração

### Variáveis de Ambiente

```bash
# URL do serviço de autenticação (obrigatório)
AUTH_SERVICE_URL=http://localhost:8080

# Habilitar/desabilitar segurança (opcional, padrão: true)
SECURITY_INTERCEPTOR_ENABLED=true
```

### Por Ambiente

**Desenvolvimento (develop):**
- Segurança desabilitada por padrão
- URL: http://localhost:8080

**Staging:**
- Segurança habilitada
- URL: https://auth-staging.example.com

**Produção (master):**
- Segurança habilitada
- URL: https://auth.example.com

## Swagger UI com JWT

### 🔒 Cadeado de Autenticação

O Swagger UI agora possui um botão de cadeado (🔒) no canto superior direito que permite configurar o token JWT para todas as requisições.

#### Como Usar:

1. **Abra o Swagger UI**: http://localhost:8081/api/swagger
2. **Clique no botão "Authorize" (🔒)** no topo da página
3. **Insira seu token JWT** no campo "Value" (sem o prefixo "Bearer")
4. **Clique em "Authorize"**
5. **Clique em "Close"**

Agora todas as requisições feitas pelo Swagger incluirão automaticamente o header:
```
Authorization: Bearer seu-token-aqui
```

#### Testando:

```bash
# 1. Obtenha um token válido do seu serviço de autenticação
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass"}'

# 2. Use o token retornado no Swagger UI
# 3. Teste qualquer endpoint protegido
```

## Uso

### Formatos de Token Aceitos

O sistema aceita o token em **dois formatos**:

#### Opção 1: Apenas o token (mais simples)
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

#### Opção 2: Com prefixo Bearer (padrão OAuth2)
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**💡 Ambos funcionam!** O cliente pode escolher o formato mais conveniente.

### Requisição com Token

```bash
curl -X GET http://localhost:8081/api/clientes \
  -H "Authorization: Bearer seu-token-aqui"
```

### Endpoints Públicos (Não Requerem Token)

- `/api/swagger/**` - Swagger UI
- `/api/swagger-ui/**` - Recursos do Swagger
- `/api/api-docs/**` - Documentação OpenAPI
- `/api/actuator/**` - Endpoints de monitoramento
- `/api/health/**` - Health checks

## Respostas

### Token Válido
- Status: 200 OK
- Requisição processada normalmente
- SecurityContext configurado com autenticação

### Token Ausente
- Status: 401 Unauthorized
- Body: `{"error":"Unauthorized","message":"Token não fornecido","status":401}`
- Requisição não processada

### Token Inválido
- Status: 401 Unauthorized
- Body: `{"error":"Unauthorized","message":"Token inválido ou expirado","status":401}`
- Requisição não processada

### Serviço de Auth Indisponível
- Status: 401 Unauthorized
- Fallback nega acesso por segurança
- Circuit breaker pode abrir após múltiplas falhas

## Resilience4j

### Circuit Breaker
- Nome: `auth-service`
- Janela: 15 requisições
- Threshold: 40% de falha
- Tempo em aberto: 20s

### Retry
- Nome: `auth-service`
- Tentativas: 2
- Intervalo: 500ms
- Backoff exponencial

### Timeout
- Nome: `auth-service`
- Duração: 5s
- Cancela futures em execução

## Desabilitar em Desenvolvimento

Para desabilitar a segurança localmente:

```bash
# No arquivo .env
SECURITY_INTERCEPTOR_ENABLED=false
```

Ou via application-develop.yaml (já configurado como false por padrão).

## Fluxo de Autenticação

```
1. Cliente faz requisição com header Authorization: Bearer <token>
   ↓
2. JwtAuthenticationFilter intercepta a requisição
   ↓
3. Extrai o token do header
   ↓
4. TokenValidationService valida com microserviço externo
   ↓
5a. Token válido → Configura SecurityContext → Processa requisição
5b. Token inválido → Retorna 401 Unauthorized
```

## Logs

```yaml
logging:
  level:
    com.devmaster.security: DEBUG
    org.springframework.security: DEBUG
```

Logs incluem:
- Validação de token
- Erros de comunicação com auth service
- Ativação de fallback
- Circuit breaker events
- Configuração do SecurityContext

## Testando

### 1. Testar com Token Válido
```bash
# Obter token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass"}' | jq -r '.token')

# Opção 1: Usar token diretamente (mais simples)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: $TOKEN"

# Opção 2: Usar com Bearer (padrão OAuth2)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer $TOKEN"
```

### 2. Testar sem Token
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all
# Retorna: 401 Unauthorized
```

### 3. Testar com Token Inválido
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: token-invalido"
# Retorna: 401 Unauthorized
```

### 4. Testar Endpoint Público
```bash
curl -X GET http://localhost:8081/api/actuator/health
# Retorna: 200 OK (sem token necessário)
```

## Formatos de Token

O filtro `JwtAuthenticationFilter` aceita o token em dois formatos:

1. **Apenas o token**: `Authorization: eyJhbGciOi...`
2. **Com Bearer**: `Authorization: Bearer eyJhbGciOi...`

O sistema remove automaticamente o prefixo "Bearer " se presente (case-insensitive).

📋 **Detalhes completos**: Veja `TOKEN_FORMATS.md`

## Diferenças: Interceptor vs Spring Security Filter

### Interceptor (Antigo)
- ❌ Executado após o Spring Security
- ❌ Não integrado com SecurityContext
- ❌ Sem suporte nativo a Swagger
- ❌ Configuração manual de exclusões

### Spring Security Filter (Atual)
- ✅ Executado antes do processamento da requisição
- ✅ Integrado com SecurityContext
- ✅ Suporte nativo a Swagger com cadeado
- ✅ Configuração declarativa de endpoints públicos
- ✅ Melhor integração com ecossistema Spring

## Troubleshooting

### Problema: Swagger retorna 401

**Solução:**
1. Verificar se endpoints do Swagger estão na lista de permitAll
2. Verificar se o padrão de URL está correto (`/api/swagger/**`)
3. Desabilitar segurança temporariamente: `SECURITY_INTERCEPTOR_ENABLED=false`

### Problema: Todas as requisições retornam 401

**Solução:**
1. Verificar se AUTH_SERVICE_URL está correto
2. Verificar se o serviço de auth está rodando
3. Verificar logs do circuit breaker
4. Testar conectividade: `curl -X POST $AUTH_SERVICE_URL/api/auth/validate-token`

### Problema: Circuit breaker sempre aberto

**Solução:**
1. Verificar conectividade com serviço de auth
2. Aumentar failure-rate-threshold
3. Verificar timeout do serviço
4. Verificar logs: `logging.level.com.devmaster.security=DEBUG`

### Problema: Token válido mas retorna 401

**Solução:**
1. Verificar formato do header: `Authorization: Bearer <token>`
2. Verificar se o token não está expirado
3. Verificar resposta do serviço de auth
4. Verificar logs do TokenValidationService

## Segurança

### Boas Práticas Implementadas

✅ **Stateless**: Sem sessões no servidor
✅ **CSRF Disabled**: Apropriado para APIs REST
✅ **Token Validation**: Validação externa com microserviço
✅ **Circuit Breaker**: Proteção contra falhas em cascata
✅ **Fallback Seguro**: Nega acesso em caso de erro
✅ **Logs Estruturados**: Auditoria de acessos
✅ **Endpoints Públicos**: Apenas o necessário

### Recomendações Adicionais

- 🔒 Use HTTPS em produção
- 🔑 Implemente rotação de tokens
- ⏰ Configure expiração de tokens
- 📊 Monitore tentativas de acesso não autorizado
- 🚫 Implemente rate limiting
- 🔍 Adicione auditoria de acessos

## Próximos Passos

### Melhorias Futuras

- [ ] Cache de tokens validados (Redis)
- [ ] Refresh token automático
- [ ] Rate limiting por usuário
- [ ] Auditoria de acessos em banco
- [ ] Métricas de segurança (Prometheus)
- [ ] Blacklist de tokens revogados
- [ ] Suporte a múltiplos issuers
- [ ] Validação de claims customizados

