# 🔑 Formatos de Token Aceitos

## ✅ Formatos Suportados

O sistema aceita o token JWT em **dois formatos** no header `Authorization`:

### 1️⃣ Apenas o Token (Recomendado)
```bash
Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 2️⃣ Com Prefixo "Bearer" (Padrão OAuth2)
```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Exemplo cURL:**
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

## 🎯 Vantagens

### ✅ Flexibilidade
- Cliente pode enviar apenas o token (mais simples)
- Ou seguir o padrão OAuth2 com "Bearer"
- Ambos funcionam perfeitamente

### ✅ Compatibilidade
- Compatível com clientes que seguem RFC 6750 (Bearer Token)
- Compatível com clientes que enviam token direto
- Case-insensitive: aceita "Bearer", "bearer", "BEARER"

### ✅ Simplicidade
- Cliente não precisa concatenar "Bearer " + token
- Menos código no cliente
- Menos chance de erro

## 📋 Exemplos Práticos

### JavaScript/Fetch
```javascript
// Opção 1: Apenas token (mais simples)
fetch('http://localhost:8081/api/v1/clientes/all', {
  headers: {
    'Authorization': token  // Apenas o token
  }
});

// Opção 2: Com Bearer (padrão OAuth2)
fetch('http://localhost:8081/api/v1/clientes/all', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### Axios
```javascript
// Opção 1: Apenas token
axios.get('http://localhost:8081/api/v1/clientes/all', {
  headers: {
    'Authorization': token
  }
});

// Opção 2: Com Bearer
axios.get('http://localhost:8081/api/v1/clientes/all', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### Java/RestTemplate
```java
// Opção 1: Apenas token
HttpHeaders headers = new HttpHeaders();
headers.set("Authorization", token);

// Opção 2: Com Bearer
HttpHeaders headers = new HttpHeaders();
headers.set("Authorization", "Bearer " + token);
```

### Python/Requests
```python
# Opção 1: Apenas token
headers = {'Authorization': token}
response = requests.get('http://localhost:8081/api/v1/clientes/all', headers=headers)

# Opção 2: Com Bearer
headers = {'Authorization': f'Bearer {token}'}
response = requests.get('http://localhost:8081/api/v1/clientes/all', headers=headers)
```

### C#/HttpClient
```csharp
// Opção 1: Apenas token
client.DefaultRequestHeaders.Add("Authorization", token);

// Opção 2: Com Bearer
client.DefaultRequestHeaders.Add("Authorization", $"Bearer {token}");
```

## 🔍 Como Funciona

O filtro `JwtAuthenticationFilter` extrai o token automaticamente:

```java
private String extractToken(String authHeader) {
    String token = authHeader.trim();
    
    // Remove "Bearer " se presente (case-insensitive)
    if (token.toLowerCase().startsWith("bearer ")) {
        token = token.substring(7).trim();
    }
    
    return token;
}
```

### Processamento:

1. **Recebe header**: `Authorization: Bearer eyJhbGciOi...`
2. **Remove "Bearer "**: `eyJhbGciOi...`
3. **Valida token**: Envia para serviço de autenticação
4. **Retorna resultado**: 200 OK ou 401 Unauthorized

## ❌ Formatos NÃO Aceitos

### Header vazio
```bash
Authorization: 
# ❌ Retorna 401 - Token não fornecido
```

### Sem header
```bash
# Sem header Authorization
# ❌ Retorna 401 - Token não fornecido
```

### Token com espaços extras
```bash
Authorization:    Bearer    eyJhbGciOi...   
# ✅ Funciona - Espaços são removidos automaticamente
```

### Outros esquemas
```bash
Authorization: Basic dXNlcjpwYXNz
# ❌ Não é JWT - será tratado como token inválido
```

## 🎨 Swagger UI

No Swagger, você pode inserir o token de **duas formas**:

### Opção 1: Apenas o token
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Opção 2: Com Bearer
```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Ambos funcionam! O Swagger adiciona "Bearer " automaticamente se você inserir apenas o token.

## 📊 Comparação

| Formato | Exemplo | Funciona? | Recomendado |
|---------|---------|-----------|-------------|
| Apenas token | `Authorization: eyJhbGci...` | ✅ Sim | ✅ Sim (mais simples) |
| Bearer + token | `Authorization: Bearer eyJhbGci...` | ✅ Sim | ✅ Sim (padrão OAuth2) |
| bearer + token | `Authorization: bearer eyJhbGci...` | ✅ Sim | ⚠️ Funciona mas não é padrão |
| BEARER + token | `Authorization: BEARER eyJhbGci...` | ✅ Sim | ⚠️ Funciona mas não é padrão |
| Sem header | - | ❌ Não | ❌ Não |
| Header vazio | `Authorization: ` | ❌ Não | ❌ Não |

## 🔒 Segurança

### ✅ Boas Práticas Mantidas
- Token é validado com serviço externo
- Circuit Breaker protege contra falhas
- Logs registram tentativas de acesso
- Fallback nega acesso em caso de erro

### ⚠️ Importante
- Sempre use HTTPS em produção
- Nunca exponha tokens em logs
- Implemente expiração de tokens
- Use tokens de curta duração

## 🧪 Testando

### Teste 1: Apenas token
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Teste 2: Com Bearer
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### Teste 3: Case-insensitive
```bash
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

Todos devem funcionar!

## 📚 Referências

- [RFC 6750 - Bearer Token Usage](https://tools.ietf.org/html/rfc6750)
- [JWT.io - JSON Web Tokens](https://jwt.io/)
- [OAuth 2.0 Authorization Framework](https://tools.ietf.org/html/rfc6749)

## 💡 Recomendação

Para novos projetos, recomendamos:

1. **Cliente envia**: Apenas o token (mais simples)
   ```
   Authorization: eyJhbGciOi...
   ```

2. **Servidor aceita**: Ambos os formatos (flexibilidade)

3. **Documentação**: Deixe claro que ambos funcionam

Isso simplifica o código do cliente e mantém compatibilidade com padrões OAuth2!
