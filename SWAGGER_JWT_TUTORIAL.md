# 🔒 Tutorial: Usando Swagger UI com JWT

## 📋 Passo a Passo Completo

### 1️⃣ Abrir o Swagger UI

Acesse no navegador:
```
http://localhost:8081/api/swagger
```

### 2️⃣ Localizar o Botão "Authorize"

No topo da página do Swagger, você verá:

```
┌─────────────────────────────────────────────────────────┐
│  devamaster API                            🔒 Authorize │
│  Version: 1.0.0                                         │
└─────────────────────────────────────────────────────────┘
```

### 3️⃣ Clicar em "Authorize"

Um modal será aberto:

```
┌──────────────────────────────────────────────────────┐
│  Available authorizations                            │
│                                                      │
│  Bearer Authentication (http, Bearer)                │
│  Insira o token JWT no formato: seu-token-aqui      │
│  (sem 'Bearer')                                      │
│                                                      │
│  ┌────────────────────────────────────────────────┐ │
│  │ Value:                                         │ │
│  │ [Digite seu token aqui]                        │ │
│  └────────────────────────────────────────────────┘ │
│                                                      │
│  [Authorize]  [Close]                                │
└──────────────────────────────────────────────────────┘
```

### 4️⃣ Obter um Token JWT

Antes de inserir o token, você precisa obtê-lo do serviço de autenticação:

#### Opção A: Via cURL
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "seu-usuario",
    "password": "sua-senha"
  }'
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "expiresIn": 3600
}
```

#### Opção B: Via Postman
1. Criar requisição POST para `http://localhost:8080/api/auth/login`
2. Body (JSON):
   ```json
   {
     "username": "seu-usuario",
     "password": "sua-senha"
   }
   ```
3. Copiar o token da resposta

### 5️⃣ Inserir o Token no Swagger

Copie apenas o valor do token (sem aspas, sem "Bearer"):

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

Cole no campo "Value" do modal.

### 6️⃣ Clicar em "Authorize"

Após clicar, você verá:

```
┌──────────────────────────────────────────────────────┐
│  Bearer Authentication (http, Bearer)                │
│  ✅ Authorized                                        │
│                                                      │
│  [Logout]                                            │
└──────────────────────────────────────────────────────┘
```

### 7️⃣ Fechar o Modal

Clique em "Close" para voltar à lista de endpoints.

### 8️⃣ Testar um Endpoint Protegido

Agora todos os endpoints terão um cadeado fechado (🔒) indicando que estão autenticados.

#### Exemplo: GET /api/clientes

1. Expanda o endpoint clicando nele
2. Clique em "Try it out"
3. Clique em "Execute"

O Swagger automaticamente incluirá o header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### Resposta de Sucesso (200 OK):
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com"
  }
]
```

#### Resposta de Erro (401 Unauthorized):
```json
{
  "error": "Unauthorized",
  "message": "Token inválido ou expirado",
  "status": 401
}
```

### 9️⃣ Fazer Logout (Remover Token)

Para remover o token:

1. Clique novamente em "Authorize" (🔒)
2. Clique em "Logout"
3. Clique em "Close"

Agora os endpoints protegidos retornarão 401 novamente.

## 🎯 Dicas Importantes

### ✅ O que fazer:
- ✅ Copie apenas o token (sem "Bearer")
- ✅ Verifique se o token não expirou
- ✅ Use o botão "Authorize" uma vez para todos os endpoints
- ✅ Teste endpoints públicos sem token (Actuator, Health)

### ❌ O que NÃO fazer:
- ❌ Não inclua "Bearer" no campo Value
- ❌ Não inclua aspas no token
- ❌ Não use tokens expirados
- ❌ Não compartilhe tokens em ambientes públicos

## 🔍 Identificando Endpoints Protegidos vs Públicos

### Endpoints Protegidos (Requerem Token)
```
🔒 GET /api/clientes
🔒 POST /api/clientes
🔒 PUT /api/clientes/{id}
🔒 DELETE /api/clientes/{id}
```

### Endpoints Públicos (Não Requerem Token)
```
🔓 GET /api/actuator/health
🔓 GET /api/actuator/info
🔓 GET /api/api-docs
```

## 🐛 Troubleshooting

### Problema: Botão "Authorize" não aparece

**Causa**: Configuração do Swagger não está correta.

**Solução**:
1. Verificar `SwaggerConfig.java`
2. Verificar se `SecurityScheme` está configurado
3. Reiniciar a aplicação

### Problema: Token inserido mas ainda retorna 401

**Possíveis causas**:

1. **Token expirado**
   - Obtenha um novo token
   - Verifique o campo `expiresIn` na resposta do login

2. **Token inválido**
   - Verifique se copiou o token completo
   - Verifique se não há espaços extras

3. **Serviço de auth indisponível**
   - Verifique se o serviço está rodando
   - Teste: `curl http://localhost:8080/api/auth/validate-token`

4. **Formato incorreto**
   - ❌ Errado: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
   - ✅ Correto: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

### Problema: Swagger não carrega

**Solução**:
1. Verificar se a aplicação está rodando
2. Verificar a URL: `http://localhost:8081/api/swagger`
3. Verificar se Swagger está habilitado no `application.yaml`

## 📊 Exemplo Completo de Fluxo

### Cenário: Listar Clientes

```bash
# 1. Obter token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Resposta:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}

# 2. Copiar token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# 3. Abrir Swagger: http://localhost:8081/api/swagger

# 4. Clicar em "Authorize" (🔒)

# 5. Colar token no campo "Value"

# 6. Clicar em "Authorize" e depois "Close"

# 7. Expandir GET /api/clientes

# 8. Clicar em "Try it out"

# 9. Clicar em "Execute"

# 10. Ver resposta 200 OK com lista de clientes
```

## 🎓 Conceitos Importantes

### JWT (JSON Web Token)

Um JWT é composto por três partes separadas por pontos:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9  ← Header
.
eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjE2MjM5MDIyfQ  ← Payload
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c  ← Signature
```

### Bearer Authentication

O esquema "Bearer" é usado para tokens JWT:

```
Authorization: Bearer <token>
```

O Swagger adiciona automaticamente o prefixo "Bearer" quando você insere o token.

### Stateless Authentication

- Não há sessões no servidor
- Cada requisição é independente
- Token contém todas as informações necessárias
- Servidor valida token a cada requisição

## 📚 Recursos Adicionais

- [JWT.io](https://jwt.io/) - Decodificar e validar tokens
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [OpenAPI Specification](https://swagger.io/specification/)

## 🎉 Conclusão

Agora você sabe como:
- ✅ Obter um token JWT
- ✅ Configurar autenticação no Swagger
- ✅ Testar endpoints protegidos
- ✅ Identificar e resolver problemas comuns

**Happy Testing!** 🚀
