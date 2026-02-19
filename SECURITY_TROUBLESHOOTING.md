# 🔧 Troubleshooting: Segurança não está funcionando

## ❌ Problema: Endpoint acessível sem token

### Sintomas
```bash
curl http://localhost:8081/api/v1/clientes/all
# Retorna 200 OK com dados (deveria retornar 401)
```

## ✅ Soluções

### 1. Verificar se a segurança está habilitada

**Arquivo: `.env`**
```bash
SECURITY_INTERCEPTOR_ENABLED=true  # Deve estar como true
```

Se estiver como `false`, altere para `true` e **reinicie a aplicação**.

### 2. Verificar o profile ativo

**Arquivo: `.env`**
```bash
SPRING_PROFILES_ACTIVE=develop  # Ou staging, master
```

**Arquivo: `application-develop.yaml`**
```yaml
security:
  interceptor:
    enabled: ${SECURITY_INTERCEPTOR_ENABLED:false}  # Padrão é false em develop
```

A variável de ambiente `.env` sobrescreve o padrão do YAML.

### 3. Reiniciar a aplicação

Após alterar o `.env`, você **DEVE** reiniciar a aplicação:

```bash
# Parar a aplicação (Ctrl+C)

# Reiniciar
mvn spring-boot:run
```

### 4. Verificar logs de inicialização

Ao iniciar, procure por estas linhas nos logs:

```
✅ Segurança HABILITADA:
INFO  SecurityConfig : Security enabled: true
INFO  JwtAuthenticationFilter : JWT Authentication Filter initialized

❌ Segurança DESABILITADA:
INFO  SecurityConfig : Security enabled: false
WARN  SecurityConfig : All endpoints are public (security disabled)
```

### 5. Testar manualmente

```bash
# Teste 1: Sem token (deve retornar 401)
curl -v http://localhost:8081/api/v1/clientes/all

# Teste 2: Com token inválido (deve retornar 401)
curl -v http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer token-invalido"

# Teste 3: Endpoint público (deve retornar 200)
curl -v http://localhost:8081/api/actuator/health
```

### 6. Usar script de teste

**Windows:**
```bash
test-security.bat
```

**Linux/Mac:**
```bash
chmod +x test-security.sh
./test-security.sh
```

## 🔍 Checklist de Verificação

- [ ] `.env` tem `SECURITY_INTERCEPTOR_ENABLED=true`
- [ ] Aplicação foi reiniciada após alterar `.env`
- [ ] Logs mostram "Security enabled: true"
- [ ] Endpoint protegido retorna 401 sem token
- [ ] Endpoint público retorna 200 sem token
- [ ] Swagger mostra botão "Authorize" (🔒)

## 📋 Configuração Correta

### Arquivo: `.env`
```bash
# Security Configuration
AUTH_SERVICE_URL=http://localhost:8080
SECURITY_INTERCEPTOR_ENABLED=true  # ← IMPORTANTE: true
```

### Arquivo: `application-develop.yaml`
```yaml
security:
  auth:
    service:
      url: ${AUTH_SERVICE_URL:http://localhost:8080}
  interceptor:
    enabled: ${SECURITY_INTERCEPTOR_ENABLED:false}  # Padrão false, mas .env sobrescreve
```

### Arquivo: `SecurityConfig.java`
```java
@Value("${security.interceptor.enabled:true}")
private boolean securityEnabled;

if (securityEnabled) {
    // Segurança habilitada
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/swagger/**", "/api/actuator/**").permitAll()
        .anyRequest().authenticated()
    );
}
```

## 🚨 Erros Comuns

### Erro 1: Esqueceu de reiniciar
```
❌ Alterou .env mas não reiniciou a aplicação
✅ Sempre reinicie após alterar .env
```

### Erro 2: Profile errado
```
❌ SPRING_PROFILES_ACTIVE=develop (segurança desabilitada por padrão)
✅ Usar .env para sobrescrever: SECURITY_INTERCEPTOR_ENABLED=true
```

### Erro 3: Typo na variável
```
❌ SECURITY_INTERCEPTOR_ENABLE=true (falta o D)
✅ SECURITY_INTERCEPTOR_ENABLED=true
```

### Erro 4: Serviço de auth não está rodando
```
❌ AUTH_SERVICE_URL aponta para serviço que não existe
✅ Verificar: curl http://localhost:8080/api/auth/validate-token
```

## 🔧 Forçar Segurança Sempre Habilitada

Se quiser que a segurança esteja **sempre habilitada**, independente do `.env`:

**Arquivo: `SecurityConfig.java`**
```java
@Value("${security.interceptor.enabled:true}")  // ← Padrão true
private boolean securityEnabled;

// Ou forçar:
private final boolean securityEnabled = true;  // Sempre habilitado
```

## 📊 Fluxo de Configuração

```
1. application.yaml (base)
   ↓
2. application-{profile}.yaml (develop, staging, master)
   ↓
3. .env (sobrescreve tudo)
   ↓
4. Variáveis de ambiente do sistema (sobrescreve .env)
```

**Ordem de precedência** (maior para menor):
1. Variáveis de ambiente do sistema
2. Arquivo `.env`
3. `application-{profile}.yaml`
4. `application.yaml`

## 🎯 Solução Rápida

```bash
# 1. Editar .env
echo "SECURITY_INTERCEPTOR_ENABLED=true" >> .env

# 2. Reiniciar aplicação
# Ctrl+C para parar
mvn spring-boot:run

# 3. Testar
curl http://localhost:8081/api/v1/clientes/all
# Deve retornar: 401 Unauthorized
```

## 📞 Ainda não funciona?

Se após seguir todos os passos ainda não funcionar:

1. **Verificar logs completos**:
   ```bash
   mvn spring-boot:run > app.log 2>&1
   cat app.log | grep -i security
   ```

2. **Verificar se o filtro está registrado**:
   ```bash
   cat app.log | grep -i "JwtAuthenticationFilter"
   ```

3. **Verificar ordem dos filtros**:
   ```bash
   cat app.log | grep -i "Filter"
   ```

4. **Habilitar debug de segurança**:
   ```yaml
   # application.yaml
   logging:
     level:
       org.springframework.security: DEBUG
       com.devmaster.security: DEBUG
   ```

5. **Criar issue** com:
   - Conteúdo do `.env`
   - Logs de inicialização
   - Resultado dos testes
   - Versão do Spring Boot
