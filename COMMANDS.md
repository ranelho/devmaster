# 📋 Comandos Úteis do Projeto

## 🚀 Execução da Aplicação

### Desenvolvimento
```bash
# Executar com profile develop (padrão)
mvn spring-boot:run

# Executar com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=develop
mvn spring-boot:run -Dspring-boot.run.profiles=staging
mvn spring-boot:run -Dspring-boot.run.profiles=master
```

### Build
```bash
# Build completo com testes
mvn clean package

# Build sem testes
mvn clean package -DskipTests

# Apenas compilar
mvn clean compile
```

## 🐳 Docker

### PostgreSQL + PgAdmin
```bash
# Iniciar todos os serviços
docker-compose up -d

# Apenas PostgreSQL
docker-compose up -d postgres

# Parar serviços
docker-compose down

# Ver logs
docker-compose logs -f

# Verificar status
docker-compose ps
```

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=DevmasterApplicationTests

# Com cobertura
mvn clean test jacoco:report
```

## 🔒 Segurança - Spring Security + JWT

### Formatos de Token Aceitos

O sistema aceita o token em **dois formatos**:

```bash
# Opção 1: Apenas o token (mais simples)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: seu-token-aqui"

# Opção 2: Com Bearer (padrão OAuth2)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer seu-token-aqui"
```

**💡 Ambos funcionam!** O cliente pode enviar apenas o token.

### Swagger UI com Cadeado

```bash
# 1. Abrir Swagger UI no navegador
start http://localhost:8081/api/swagger

# 2. Clicar no botão "Authorize" (🔒) no topo
# 3. Inserir o token JWT (sem "Bearer")
# 4. Clicar em "Authorize" e depois "Close"
# 5. Testar qualquer endpoint - o token será incluído automaticamente
```

### Testar Validação de Token via cURL

```bash
# Opção 1: Apenas o token (mais simples)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: seu-token-aqui"

# Opção 2: Com Bearer (padrão OAuth2)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer seu-token-aqui"

# Requisição sem token (retorna 401)
curl -X GET http://localhost:8081/api/v1/clientes/all

# Requisição com token inválido (retorna 401)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: token-invalido"

# Testar endpoint público (não requer token)
curl -X GET http://localhost:8081/api/actuator/health
```

**💡 Dica**: O sistema aceita o token com ou sem "Bearer". Escolha o formato mais conveniente!

### Obter Token do Serviço de Autenticação

```bash
# Fazer login e obter token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass"}'

# Salvar token em variável (Linux/Mac)
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"pass"}' | jq -r '.token')

# Usar token
curl -X GET http://localhost:8081/api/clientes \
  -H "Authorization: Bearer $TOKEN"
```

### Configurar Serviço de Autenticação

```bash
# No arquivo .env
AUTH_SERVICE_URL=http://localhost:8080
SECURITY_INTERCEPTOR_ENABLED=true

# Desabilitar segurança para desenvolvimento
SECURITY_INTERCEPTOR_ENABLED=false
```

### Testar Conectividade com Auth Service

```bash
# Verificar se o serviço de auth está acessível
curl -X POST http://localhost:8080/api/auth/validate-token \
  -H "Authorization: Bearer test-token"

# Verificar variável de ambiente
echo %AUTH_SERVICE_URL%
```

## 🛡️ Exception Handler

### Testar Tipos de Erro
```bash
# Listar todos os tipos de erro
curl http://localhost:8081/api/demo/exceptions/error-types

# Teste de validação
curl -X POST http://localhost:8081/api/demo/exceptions/validation \
  -H "Content-Type: application/json" \
  -d '{"name": "", "email": "inválido"}'

# Teste de recurso não encontrado
curl http://localhost:8081/api/demo/exceptions/not-found/123

# Teste de erro de tipo
curl "http://localhost:8081/api/demo/exceptions/type-mismatch?number=abc"
```

## 🔧 Circuit Breaker

### Testar Resiliência
```bash
# Status de todos os circuit breakers
curl http://localhost:8081/api/resilience/status

# Teste de sucesso
curl http://localhost:8081/api/resilience/test/success

# Teste de falha (execute várias vezes para abrir o circuito)
for i in {1..10}; do
  curl http://localhost:8081/api/resilience/test/failure
done

# Verificar status após falhas
curl http://localhost:8081/api/resilience/status

# Reset do circuit breaker
curl -X POST http://localhost:8081/api/resilience/reset/external-api

# Teste de timeout
curl http://localhost:8081/api/resilience/test/timeout

# Teste intermitente
curl http://localhost:8081/api/resilience/test/intermittent
```

## 📊 Monitoramento

### Actuator Endpoints
```bash
# Health check
curl http://localhost:8081/api/actuator/health

# Informações da aplicação
curl http://localhost:8081/api/actuator/info

# Métricas
curl http://localhost:8081/api/actuator/metrics

# Métricas específicas
curl http://localhost:8081/api/actuator/metrics/jvm.memory.used
curl http://localhost:8081/api/actuator/metrics/http.server.requests
```

## 📖 Documentação

### Swagger/OpenAPI
```bash
# Abrir Swagger UI no navegador
start http://localhost:8081/api/swagger

# API Docs JSON
curl http://localhost:8081/api/api-docs

# API Docs YAML
curl http://localhost:8081/api/api-docs.yaml
```

## 🔍 Análise de Código

### Dependências
```bash
# Árvore de dependências
mvn dependency:tree

# Verificar versão específica
mvn dependency:tree | findstr commons-lang3

# Dependências desatualizadas
mvn versions:display-dependency-updates
```

### Segurança
```bash
# Scan de vulnerabilidades
mvn org.owasp:dependency-check-maven:check

# Verificar CVE específico
mvn dependency:tree | findstr commons-lang3
```

## 🗄️ Banco de Dados

### PostgreSQL Local
```bash
# Conectar via psql
docker exec -it devmaster-postgres psql -U devmaster -d devmaster_dev

# Backup
docker exec devmaster-postgres pg_dump -U devmaster devmaster_dev > backup.sql

# Restore
docker exec -i devmaster-postgres psql -U devmaster devmaster_dev < backup.sql
```

### PgAdmin
```bash
# Acessar PgAdmin
start http://localhost:5050

# Credenciais:
# Email: admin@devmaster.com
# Password: admin123
```

## 🔄 Git

### Workflow
```bash
# Criar branch de feature
git checkout master
git pull origin master
git checkout -b feature/nova-funcionalidade

# Commit seguindo conventional commits
git add .
git commit -m "feat: add nova funcionalidade"

# Push e criar PR
git push origin feature/nova-funcionalidade
```

### Conventional Commits
```bash
# Nova funcionalidade
git commit -m "feat: add user authentication"

# Correção de bug
git commit -m "fix: resolve login validation issue"

# Documentação
git commit -m "docs: update API documentation"

# Refatoração
git commit -m "refactor: improve code structure"

# Performance
git commit -m "perf: optimize database queries"

# Testes
git commit -m "test: add unit tests for user service"
```

## 🧹 Limpeza

### Maven
```bash
# Limpar target
mvn clean

# Limpar e reinstalar dependências
mvn clean install -U

# Limpar cache local
mvn dependency:purge-local-repository
```

### Docker
```bash
# Remover containers parados
docker-compose down

# Remover volumes
docker-compose down -v

# Limpar tudo
docker system prune -a
```

## 🔧 Troubleshooting

### Porta em Uso
```bash
# Windows - Verificar porta 8081
netstat -ano | findstr :8081

# Matar processo
taskkill /PID <PID> /F
```

### Problemas de Compilação
```bash
# Limpar e recompilar
mvn clean compile

# Atualizar dependências
mvn clean install -U

# Verificar versão do Java
java -version
mvn -version
```

### Problemas com Banco de Dados
```bash
# Verificar se PostgreSQL está rodando
docker-compose ps

# Reiniciar PostgreSQL
docker-compose restart postgres

# Ver logs do PostgreSQL
docker-compose logs -f postgres
```

### Problemas com Spring Security
```bash
# Desabilitar temporariamente
# No arquivo .env:
SECURITY_INTERCEPTOR_ENABLED=false

# Verificar URL do serviço de auth
echo %AUTH_SERVICE_URL%

# Testar conectividade com serviço de auth
curl -X POST http://localhost:8080/api/auth/validate-token \
  -H "Authorization: Bearer test-token"

# Verificar logs de segurança
# Adicionar no application.yaml:
# logging:
#   level:
#     com.devmaster.security: DEBUG
#     org.springframework.security: DEBUG
```

## 📝 Variáveis de Ambiente

### Listar Variáveis Atuais
```bash
# Windows
set | findstr SPRING
set | findstr DATABASE
set | findstr AUTH

# Ver todas as variáveis do .env
type .env
```

### Configurar Variáveis
```bash
# Temporário (sessão atual)
set SPRING_PROFILES_ACTIVE=staging
set AUTH_SERVICE_URL=http://localhost:8080

# Permanente (editar .env)
notepad .env
```

## 🚀 Deploy

### Build para Produção
```bash
# Build com profile de produção
mvn clean package -Dspring.profiles.active=master -DskipTests

# Executar JAR
java -jar target/devmaster-0.0.1-SNAPSHOT.jar --spring.profiles.active=master
```

### Docker Build (futuro)
```bash
# Build da imagem
docker build -t devmaster:latest .

# Executar container
docker run -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=master \
  -e AUTH_SERVICE_URL=https://auth.example.com \
  devmaster:latest
```
