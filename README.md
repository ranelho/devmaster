# 🚀 DevMaster - Projeto de Estudos Spring Boot 3 + Java 21

> **Projeto educacional completo** para desenvolvedores que querem **evoluir** suas habilidades ou **retomar** os estudos em Spring Boot com as **tecnologias mais modernas** disponíveis.

## 🎯 Objetivo do Projeto

Este projeto foi criado especificamente para:

- **📚 Estudantes** que querem aprender Spring Boot do zero
- **🔄 Desenvolvedores** que estão retomando os estudos após um tempo
- **⬆️ Profissionais** que querem se atualizar com as versões mais recentes
- **🏗️ Arquitetos** que precisam de uma base sólida para novos projetos

## 🔥 Tecnologias Atuais (2025)

### Core Technologies
- **☕ Java 21 LTS** - Versão LTS estável e moderna
- **🍃 Spring Boot 3.5.9** - Framework mais recente para Java
- **🐘 PostgreSQL 15** - Banco de dados relacional robusto
- **📊 HikariCP** - Pool de conexões de alta performance (integrado)

### Documentation & API
- **📖 SpringDoc OpenAPI 2.7.0** - Documentação OpenAPI nativa
- **🎨 Swagger UI** - Interface visual para testar APIs
- **🔒 Swagger JWT** - Autenticação integrada com cadeado
- **📋 Spring Boot Actuator** - Monitoramento e métricas

### Security
- **🔐 Spring Security 6** - Framework de segurança enterprise
- **🔑 JWT Authentication** - Autenticação stateless com tokens
- **🛡️ Token Validation** - Validação via microserviço externo
- **🚫 CSRF Protection** - Desabilitado para APIs REST (stateless)

### Resilience & Reliability
- **🔧 Resilience4j 2.2.0** - Circuit Breaker, Retry, Timeout
- **🛡️ Fallback Methods** - Respostas alternativas em caso de falha
- **📊 Métricas de Resiliência** - Monitoramento de saúde dos serviços

### Development Tools
- **🔧 Lombok** - Redução de boilerplate code
- **🎯 Spring AOP** - Programação orientada a aspectos
- **📝 Logging Aspect** - Monitoramento automático de performance
- **🧪 Spring Boot Test** - Framework completo de testes
- **🌍 Spring DotEnv** - Suporte nativo para arquivos .env

## 🏗️ Arquitetura do Projeto

### Configurações Multi-Ambiente
```
📁 src/main/resources/
├── 🔧 application.yaml              # Configurações gerais
├── 🟢 application-develop.yaml      # Desenvolvimento local
├── 🟡 application-staging.yaml      # Homologação
└── 🔴 application-master.yaml       # Produção
```

### Estrutura de Código Organizada
```
📁 src/main/java/com/devmaster/
├── 📁 config/                       # Configurações centralizadas
│   ├── 📝 LoggingAspect.java        # Logging automático com AOP
│   ├── 📖 SwaggerConfig.java        # Configuração do OpenAPI
│   └── 🌐 WebConfig.java            # Configurações web
├── 📁 controller/                   # Controllers REST (em desenvolvimento)
└── 🚀 DevmasterApplication.java     # Classe principal
```

## 🚀 Quick Start

### Pré-requisitos
- **Java 21 LTS** instalado
- **Maven 3.9+** para build
- **PostgreSQL 15+** para banco de dados (opcional - pode usar Supabase)
- **IDE** de sua preferência (IntelliJ IDEA, VS Code, Eclipse)

### 1. Clone e Configure
```bash
git clone <repository-url>
cd devmaster
cp .env.example .env
# Edite o arquivo .env com suas configurações
```

> **⚠️ IMPORTANTE**: O arquivo `.env` contém informações sensíveis e está no `.gitignore`. Nunca commite credenciais reais no repositório!

### 2. Inicie o Banco de Dados (Opção Local com Docker)
```bash
# Inicia PostgreSQL + PgAdmin
docker-compose up -d

# Apenas PostgreSQL
docker-compose up -d postgres

# Verificar status
docker-compose ps
```

**Serviços disponíveis:**
- **PostgreSQL**: `localhost:5432`
  - Database: `devmaster_dev`
  - User: `devmaster`
  - Password: `devmaster123`
- **PgAdmin**: `localhost:5050`
  - Email: `admin@devmaster.com`
  - Password: `admin123`

### 3. Execute a Aplicação
```bash
# Desenvolvimento
mvn spring-boot:run

# Ou especifique o ambiente
mvn spring-boot:run -Dspring-boot.run.profiles=develop
```

### 4. Acesse as URLs
- **🏠 Aplicação**: http://localhost:8081/api
- **📖 Swagger UI**: http://localhost:8081/api/swagger
  - **🔒 Clique em "Authorize"** para configurar seu token JWT
  - Insira o token (sem "Bearer") e teste os endpoints protegidos
- **📋 API Docs**: http://localhost:8081/api/api-docs
- **📊 Actuator**: http://localhost:8081/api/actuator
- **🔧 Circuit Breaker Status**: http://localhost:8081/api/resilience/status
- **🧪 Teste de Resiliência**: http://localhost:8081/api/resilience/test/success
- **🛡️ Teste de Exception Handler**: http://localhost:8081/api/demo/exceptions/error-types

## 📚 Conceitos Abordados

### 🔧 Configuração e Setup
- ✅ **Multi-ambiente** com profiles do Spring
- ✅ **Variáveis de ambiente** com suporte nativo .env
- ✅ **Properties externalizadas** para flexibilidade
- ✅ **Docker Compose** para dependências locais (PostgreSQL + PgAdmin)
- ✅ **Circuit Breaker** com Resilience4j para resiliência
- ✅ **Segurança** - Vulnerabilidades corrigidas (CVE-2025-48924)
- ✅ **Spring Security + JWT** - Autenticação com microserviços
- ✅ **Swagger com JWT** - Cadeado de autenticação integrado

### 🗄️ Banco de Dados
- ✅ **Spring Data JPA** configurado (temporariamente desabilitado para testes)
- ✅ **HikariCP** integrado para pool de conexões
- ✅ **Suporte Supabase** para banco cloud
- ✅ **PostgreSQL local** via Docker

### 🌐 APIs REST
- ✅ **Estrutura base** para controllers
- ✅ **Documentação automática** com SpringDoc OpenAPI
- ✅ **Swagger UI** integrado e funcional
- ✅ **Configurações web** otimizadas
- ✅ **Global Exception Handler** - Tratamento centralizado de erros

### 📊 Monitoramento e Logging
- ✅ **Logging estruturado** com Logback
- ✅ **Aspect-Oriented Programming** para cross-cutting concerns
- ✅ **Métricas** com Spring Actuator
- ✅ **Performance monitoring** automático com AOP

#### 🎯 Características do LoggingAspect
- **🎯 Controllers**: Log de entrada e tempo de execução com emojis
- **🔧 Services**: Monitoramento de performance com StopWatch
- **🗄️ Repositories**: Detecção automática de queries lentas (>1s)
- **🚨 Exception Handling**: Log estruturado de erros com stack trace em debug
- **⏱️ Performance Tracking**: Medição precisa com Instant e Duration (Java 21)

### 🧪 Qualidade de Código
- ✅ **Lombok** para código limpo
- ✅ **Padrões de projeto** aplicados
- ✅ **Separação de responsabilidades**
- ✅ **Configuração centralizada**

### 🚀 Funcionalidades Implementadas
- ✅ **Aplicação base** funcional
- ✅ **Swagger UI** acessível em `/api/swagger`
- ✅ **Actuator endpoints** para monitoramento
- ✅ **Logging automático** com emojis e performance tracking
- ✅ **Configuração multi-ambiente** pronta para uso
- ✅ **Suporte completo a .env** para desenvolvimento local
- ✅ **Circuit Breaker** com Resilience4j para resiliência
- ✅ **Endpoints de teste** para demonstrar padrões de resiliência
- ✅ **Global Exception Handler** - Tratamento padronizado de erros
- ✅ **Segurança** - Vulnerabilidade CVE-2025-48924 corrigida
- ✅ **Spring Security + JWT** - Autenticação com microserviços
- ✅ **Swagger com JWT** - Cadeado de autenticação integrado

## 🔒 Interceptor de Segurança

### 🛡️ **Spring Security + JWT Authentication**

O projeto implementa Spring Security com filtro JWT que valida tokens em todas as requisições HTTP, comunicando-se com um microserviço de autenticação externo.

#### **⚠️ IMPORTANTE: Ativar Segurança**

Por padrão, a segurança está **DESABILITADA** em desenvolvimento. Para ativar:

1. **Editar `.env`**:
   ```bash
   SECURITY_INTERCEPTOR_ENABLED=true
   ```

2. **Reiniciar a aplicação**:
   ```bash
   # Parar: Ctrl+C
   # Iniciar: mvn spring-boot:run
   ```

3. **Verificar logs**:
   ```
   ✅ Segurança HABILITADA - Endpoints protegidos requerem token JWT
   ```

📖 **Guia completo de ativação**: `ATIVAR_SEGURANCA.md`

#### **🎯 Características:**
- **🔐 Spring Security Filter**: Integração nativa com SecurityContext
- **🔒 Swagger com Cadeado**: Botão "Authorize" para configurar JWT
- **🌍 Multi-ambiente**: URL do serviço de auth configurável por ambiente
- **🛡️ Circuit Breaker**: Proteção contra falhas do serviço de autenticação
- **🔄 Retry**: Tentativas automáticas em caso de erro temporário
- **⏱️ Timeout**: Evita requisições que demoram muito
- **🚫 Fallback**: Nega acesso em caso de falha por segurança
- **🔓 Stateless**: Sem sessões no servidor (JWT only)

#### **⚙️ Configuração por Ambiente:**

```bash
# Desenvolvimento (desabilitado por padrão)
AUTH_SERVICE_URL=http://localhost:8080
SECURITY_INTERCEPTOR_ENABLED=false  # Altere para true para ativar

# Staging
AUTH_SERVICE_URL=https://auth-staging.example.com
SECURITY_INTERCEPTOR_ENABLED=true

# Produção
AUTH_SERVICE_URL=https://auth.example.com
SECURITY_INTERCEPTOR_ENABLED=true
```

#### **🔒 Usando o Swagger com JWT:**

1. **Abra o Swagger UI**: http://localhost:8081/api/swagger
2. **Clique no botão "Authorize" (🔒)** no topo da página
3. **Insira seu token JWT** (sem o prefixo "Bearer")
4. **Clique em "Authorize"** e depois em "Close"
5. **Teste qualquer endpoint** - o token será incluído automaticamente

#### **🧪 Testando via cURL:**

```bash
# Opção 1: Apenas o token (mais simples)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: seu-token-aqui"

# Opção 2: Com prefixo Bearer (padrão OAuth2)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: Bearer seu-token-aqui"

# Ambos os formatos funcionam!

# Requisição sem token (retorna 401 se segurança habilitada)
curl -X GET http://localhost:8081/api/v1/clientes/all

# Requisição com token inválido (retorna 401)
curl -X GET http://localhost:8081/api/v1/clientes/all \
  -H "Authorization: token-invalido"
```

**💡 Dica**: O cliente pode enviar apenas o token sem "Bearer". O sistema aceita ambos os formatos!

📋 **Formatos aceitos**: Veja `TOKEN_FORMATS.md` para todos os exemplos

#### **📋 Endpoints Públicos (Não Requerem Token):**
- `/api/swagger/**` - Swagger UI
- `/api/api-docs/**` - Documentação OpenAPI
- `/api/actuator/**` - Monitoramento
- `/api/health/**` - Health checks

#### **🧪 Script de Teste:**
```bash
# Windows
test-security.bat

# Linux/Mac
./test-security.sh
```

📋 **Guia completo**: Veja `SECURITY_INTERCEPTOR_GUIDE.md`
📖 **Tutorial Swagger**: Veja `SWAGGER_JWT_TUTORIAL.md` para passo a passo visual
🔧 **Troubleshooting**: Veja `SECURITY_TROUBLESHOOTING.md` se tiver problemas

## 🛡️ Global Exception Handler

### 🎯 **Tratamento Centralizado de Erros**

O projeto implementa um sistema completo de tratamento de exceções que garante respostas consistentes e informativas para todos os tipos de erro.

#### **📋 Tipos de Erro Tratados:**
- **📝 Validação**: Campos obrigatórios, formatos inválidos, constraints
- **🔄 Conversão**: Tipos de dados incorretos, formatos incompatíveis
- **🌐 HTTP**: Endpoints não encontrados, métodos não suportados
- **🗄️ Banco de Dados**: Violações de integridade, chaves duplicadas
- **💼 Negócio**: Regras específicas da aplicação
- **💥 Genéricos**: Erros internos não tratados especificamente

#### **🧪 Testando o Exception Handler:**

```bash
# Lista todos os tipos de erro disponíveis
curl http://localhost:8081/api/demo/exceptions/error-types

# Teste de validação (envie dados inválidos)
curl -X POST http://localhost:8081/api/demo/exceptions/validation \
  -H "Content-Type: application/json" \
  -d '{"name": "", "email": "inválido"}'

# Teste de recurso não encontrado
curl http://localhost:8081/api/demo/exceptions/not-found/123

# Teste de erro de tipo (use texto onde deveria ser número)
curl "http://localhost:8081/api/demo/exceptions/type-mismatch?number=abc"
```

#### **📊 Estrutura de Resposta Padronizada:**
```json
{
  "timestamp": "2025-12-26 10:30:45",
  "status": 400,
  "error": "Validation Failed",
  "message": "Dados inválidos fornecidos",
  "path": "/api/demo/exceptions/validation",
  "method": "POST",
  "details": {
    "name": "Nome é obrigatório",
    "email": "Email deve ter formato válido"
  }
}
```

📋 **Guia completo**: Veja `EXCEPTION_HANDLER_GUIDE.md`

## 🔒 Segurança

### 🛡️ Vulnerabilidades Corrigidas

#### ✅ CVE-2025-48924 - Apache Commons Lang 3
- **Status**: **RESOLVIDO** 
- **Componente**: commons-lang3
- **Versão Vulnerável**: 3.17.0 ❌
- **Versão Segura**: 3.18.0 ✅
- **Severidade**: 5.3 (Medium)
- **Tipo**: Uncontrolled Recursion / DoS

**Solução**: Exclusão da dependência transitiva vulnerável e adição explícita da versão segura.

📋 **Detalhes completos**: Veja `SECURITY_FIX.md`

### 🔍 **Verificação de Segurança**
```bash
# Verificar versão atual do commons-lang3
mvn dependency:tree | findstr commons-lang3
# Deve mostrar: commons-lang3:jar:3.18.0:compile ✅

# Scan de vulnerabilidades (opcional)
mvn org.owasp:dependency-check-maven:check
```

## 🔧 Circuit Breaker e Resiliência

### 🛡️ Padrões Implementados

#### **Circuit Breaker**
- **🟢 CLOSED**: Funcionando normalmente, todas as chamadas passam
- **🔴 OPEN**: Muitas falhas detectadas, chamadas são rejeitadas imediatamente
- **🟡 HALF_OPEN**: Testando recuperação, permite algumas chamadas

#### **Retry**
- Tenta novamente automaticamente em caso de falha temporária
- Backoff exponencial para evitar sobrecarga
- Configurável por tipo de serviço

#### **Timeout**
- Evita chamadas que "ficam penduradas"
- Configuração específica por tipo de operação
- Cancela futures em execução

### 🧪 Testando o Circuit Breaker

#### **Endpoints Disponíveis**
```bash
# Status de todos os circuit breakers
GET /api/resilience/status

# Testar API externa (use URLs reais)
GET /api/resilience/external-api?url=https://httpbin.org/delay/2

# Testar operação de banco (com falhas simuladas)
GET /api/resilience/database?query=SELECT * FROM users

# Testar cenários específicos
GET /api/resilience/test/success     # Sempre funciona
GET /api/resilience/test/failure     # Sempre falha
GET /api/resilience/test/timeout     # Demora muito (timeout)
GET /api/resilience/test/intermittent # Falha esporadicamente

# Reset de circuit breaker
POST /api/resilience/reset/external-api
```

#### **Cenários de Teste Recomendados**

1. **🟢 Teste de Sucesso**
   ```bash
   curl "http://localhost:8081/api/resilience/test/success"
   ```

2. **🔴 Forçar Abertura do Circuito**
   ```bash
   # Execute várias vezes para acumular falhas
   for i in {1..10}; do
     curl "http://localhost:8081/api/resilience/test/failure"
   done
   
   # Verifique o status - deve estar OPEN
   curl "http://localhost:8081/api/resilience/status"
   ```

3. **🟡 Teste de Recuperação**
   ```bash
   # Reset o circuit breaker
   curl -X POST "http://localhost:8081/api/resilience/reset/external-api"
   
   # Teste cenário intermitente
   curl "http://localhost:8081/api/resilience/test/intermittent"
   ```

4. **⏰ Teste de Timeout**
   ```bash
   curl "http://localhost:8081/api/resilience/test/timeout"
   ```

### 📊 Monitoramento

#### **Métricas Disponíveis**
- Taxa de falhas por circuit breaker
- Número de chamadas (total, sucesso, falha)
- Chamadas rejeitadas (quando circuito aberto)
- Estado atual de cada circuit breaker

#### **Logs Estruturados**
```
🟢 Circuit Breaker 'external-api': OPEN → CLOSED (Circuito FECHADO - Funcionando normalmente)
🔴 Circuit Breaker 'database': CLOSED → OPEN (Circuito ABERTO - Falhas detectadas)
🟡 Circuit Breaker 'external-api': OPEN → HALF_OPEN (Testando recuperação)
✅ Circuit Breaker 'external-api': Chamada bem-sucedida (duração: 245ms)
❌ Circuit Breaker 'database': Falha detectada - RuntimeException (duração: 1205ms)
🚫 Circuit Breaker 'external-api': Chamada rejeitada - Circuito ABERTO
```

### ⚙️ Configuração Personalizada

#### **Configurações por Ambiente**
```yaml
# Desenvolvimento - mais tolerante para testes
resilience4j:
  circuitbreaker:
    instances:
      external-api:
        failure-rate-threshold: 60        # 60% de falha
        wait-duration-in-open-state: 60s  # Aguarda 1 minuto

# Produção - menos tolerante
resilience4j:
  circuitbreaker:
    instances:
      external-api:
        failure-rate-threshold: 30        # 30% de falha
        wait-duration-in-open-state: 30s  # Aguarda 30 segundos
```

#### **Criando Novos Circuit Breakers**
```java
@CircuitBreaker(name = "payment-service", fallbackMethod = "fallbackPayment")
@Retry(name = "payment-service")
@TimeLimiter(name = "payment-service")
public CompletableFuture<PaymentResponse> processPayment(PaymentRequest request) {
    // Lógica de pagamento
}

public CompletableFuture<PaymentResponse> fallbackPayment(PaymentRequest request, Exception ex) {
    // Resposta alternativa
    return CompletableFuture.completedFuture(
        PaymentResponse.builder()
            .status("DEFERRED")
            .message("Pagamento será processado posteriormente")
            .build()
    );
}
```

## ⚙️ Configurações por Ambiente

### 🟢 Development (develop)
```yaml
# Configurações otimizadas para desenvolvimento local
- DDL: update (cria/atualiza tabelas automaticamente)
- SQL Logging: habilitado para debug
- Swagger: habilitado
- Pool de conexões: 10 conexões
- Log Level: DEBUG para análise detalhada
```

### 🟡 Staging (staging)
```yaml
# Configurações para ambiente de homologação
- DDL: validate (apenas valida o schema)
- SQL Logging: desabilitado
- Swagger: habilitado (configurável)
- Pool de conexões: 15 conexões
- Log Level: INFO para monitoramento
```

### 🔴 Production (master)
```yaml
# Configurações para ambiente de produção
- DDL: validate (segurança máxima)
- SQL Logging: desabilitado
- Swagger: desabilitado por segurança
- Pool de conexões: 20 conexões
- Log Level: WARN para performance
```

## 🔐 Variáveis de Ambiente

### 🔒 Segurança
```bash
# URL do serviço de autenticação (obrigatório em produção)
AUTH_SERVICE_URL=http://localhost:8080

# Habilitar/desabilitar interceptor de segurança
SECURITY_INTERCEPTOR_ENABLED=true
```

### 🌍 Configurações Gerais
```bash
SPRING_PROFILES_ACTIVE=develop    # Ambiente ativo
SERVER_PORT=8081                  # Porta da aplicação
CONTEXT_PATH=/api                 # Contexto da aplicação
APP_VERSION=1.0.0                 # Versão da aplicação
APP_TIMEZONE=America/Sao_Paulo    # Timezone
APP_DEBUG=true                    # Modo debug
```

### 🗄️ Banco de Dados
```bash
# Opção 1: Supabase (Cloud) - Configure suas próprias credenciais
DATABASE_URL=jdbc:postgresql://your-supabase-host:5432/your-database
DATABASE_USERNAME=your-username
DATABASE_PASSWORD=your-password

# Opção 2: Local com Docker
DATABASE_URL=jdbc:postgresql://localhost:5432/devmaster_dev
DATABASE_USERNAME=devmaster
DATABASE_PASSWORD=devmaster123

# Configurações de Pool
DATABASE_POOL_SIZE=10             # Tamanho do pool
DATABASE_MIN_IDLE=2               # Conexões mínimas ociosas
DATABASE_CONNECTION_TIMEOUT=30000 # Timeout de conexão
DATABASE_IDLE_TIMEOUT=600000      # Timeout de idle
DATABASE_MAX_LIFETIME=1800000     # Tempo máximo de vida da conexão
```

### 🗄️ JPA/Hibernate
```bash
JPA_DDL_AUTO=update              # Estratégia DDL (create, update, validate)
JPA_SHOW_SQL=true                # Mostrar queries SQL no log
```

### 📝 Logging e Debug
```bash
LOG_LEVEL=DEBUG                  # Nível geral de log
LOG_LEVEL_WEB=DEBUG             # Log para requisições web
LOG_LEVEL_SQL=DEBUG             # Log para queries SQL
LOG_LEVEL_SQL_PARAMS=TRACE      # Log para parâmetros SQL
SWAGGER_ENABLED=true            # Habilitar/desabilitar Swagger
```

## 🔗 Endpoints Disponíveis

### 📖 Documentação
| Endpoint | Descrição |
|----------|-----------|
| `GET /api/swagger` | Interface visual do Swagger UI |
| `GET /api/api-docs` | Especificação OpenAPI (JSON) |
| `GET /api/api-docs.yaml` | Especificação OpenAPI (YAML) |

### 📊 Monitoramento
| Endpoint | Descrição |
|----------|-----------|
| `GET /api/actuator/health` | Health check da aplicação |
| `GET /api/actuator/info` | Informações da aplicação |
| `GET /api/actuator/metrics` | Métricas da aplicação |

> **Nota**: Os controllers de negócio estão em desenvolvimento. A estrutura base está pronta para receber novos endpoints.

## 🎓 Roteiro de Estudos

### 📖 Nível Iniciante
1. **Configuração do Ambiente**
   - Instalar Java 21 LTS e Maven
   - Configurar IDE
   - Executar o projeto pela primeira vez

2. **Entendendo a Estrutura**
   - Explorar os arquivos de configuração YAML
   - Analisar as classes de configuração
   - Testar os endpoints básicos

3. **Conceitos Fundamentais**
   - Injeção de dependência
   - Annotations do Spring
   - Profiles e configurações

### 🚀 Nível Intermediário
1. **Banco de Dados**
   - Criar entidades JPA
   - Implementar repositories
   - Configurar migrations

2. **APIs REST**
   - Criar controllers completos
   - Implementar validações
   - Documentar com OpenAPI

3. **Testes**
   - Testes unitários
   - Testes de integração
   - Test containers

### 🏆 Nível Avançado
1. **Arquitetura**
   - Padrões de design
   - Clean Architecture
   - Microservices

2. **Performance**
   - Otimização de queries
   - Cache com Redis
   - Monitoramento avançado

3. **DevOps**
   - Docker e Kubernetes
   - CI/CD pipelines
   - Observabilidade

## 📁 Estrutura Detalhada do Projeto

```
devmaster/
├── 📁 src/main/java/com/devmaster/
│   ├── 📁 config/                    # 🔧 Configurações centralizadas
│   │   ├── LoggingAspect.java        # Monitoramento automático com AOP
│   │   ├── SwaggerConfig.java        # Documentação OpenAPI
│   │   └── WebConfig.java            # Configurações web
│   ├── 📁 controller/                # 🌐 Controllers REST (em desenvolvimento)
│   └── DevmasterApplication.java     # 🚀 Classe principal
├── 📁 src/main/resources/
│   ├── application.yaml              # ⚙️ Configurações gerais
│   ├── application-develop.yaml      # 🟢 Desenvolvimento
│   ├── application-staging.yaml      # 🟡 Homologação
│   └── application-master.yaml       # 🔴 Produção
├── 📁 src/test/java/
│   └── DevmasterApplicationTests.java # 🧪 Testes da aplicação
├── docker-compose.yml                # 🐳 PostgreSQL + PgAdmin local
├── .env.example                      # 📝 Exemplo de variáveis
├── .gitmessage                       # 📋 Template para commits
├── pom.xml                           # 📦 Dependências Maven
└── README.md                         # 📚 Esta documentação
```

## 🛣️ Próximos Passos (Roadmap)

### 🎯 Fase 1 - Fundação (Atual)
- ✅ Configuração multi-ambiente
- ✅ Documentação com OpenAPI
- ✅ Logging estruturado
- ✅ Health checks

### 🎯 Fase 2 - Persistência
- 🔄 Entidades JPA com relacionamentos
- 🔄 Repositories customizados
- 🔄 Migrations com Flyway
- 🔄 Auditoria automática

### 🎯 Fase 3 - APIs Completas
- 🔄 CRUD completo
- 🔄 Paginação e ordenação
- 🔄 Filtros dinâmicos
- 🔄 Validações avançadas

### 🎯 Fase 4 - Segurança
- 🔄 Spring Security
- 🔄 JWT Authentication
- 🔄 OAuth2 / OpenID Connect
- 🔄 Rate limiting

### 🎯 Fase 5 - Performance
- 🔄 Cache com Redis
- 🔄 Async processing
- 🔄 Database optimization
- 🔄 Monitoring avançado

### 🎯 Fase 6 - DevOps
- 🔄 Docker containers
- 🔄 Kubernetes deployment
- 🔄 CI/CD pipelines
- 🔄 Observabilidade completa

## 🔄 Fluxo de Desenvolvimento (Git Flow)

### 🌿 Estrutura de Branches

```
master (produção)
  ↑
staging (homologação) 
  ↑
develop (desenvolvimento)
  ↑
feature/*, fix/*, hotfix/* (trabalho)
```

### 📋 Tipos de Branches

#### 🚀 **feature/** - Novas funcionalidades
```bash
# Criar branch de feature
git checkout master
git pull origin master
git checkout -b feature/user-authentication

# Trabalhar na feature...
git add .
git commit -m "feat: add user authentication system"

# Push e criar PR para develop
git push origin feature/user-authentication
```

#### 🐛 **fix/** - Correções de bugs
```bash
# Criar branch de fix
git checkout master
git pull origin master
git checkout -b fix/login-validation

# Corrigir o bug...
git add .
git commit -m "fix: resolve login validation issue"

# Push e criar PR para develop
git push origin fix/login-validation
```

#### 🔥 **hotfix/** - Correções urgentes em produção
```bash
# Criar branch de hotfix
git checkout master
git pull origin master
git checkout -b hotfix/critical-security-patch

# Aplicar correção urgente...
git add .
git commit -m "fix: patch critical security vulnerability"

# Push e criar PR para develop
git push origin hotfix/critical-security-patch
```

### 🔀 Fluxo de Pull Requests

#### 📊 Aprovações Necessárias
| Origem | Destino | Aprovações | Descrição |
|--------|---------|------------|-----------|
| `feature/*`, `fix/*`, `hotfix/*` | `develop` | **0** | Desenvolvimento livre |
| `develop` | `staging` | **≥1** | Deploy para homologação |
| `staging` | `master` | **≥1** | Deploy para produção |

#### 🔄 Sequência de Deploy
```bash
# 1. Desenvolvimento
feature/nova-funcionalidade → develop (sem aprovação)

# 2. Homologação  
develop → staging (1+ aprovação)

# 3. Produção
staging → master (1+ aprovação)
```

## 📝 Padrão de Commits (Conventional Commits)

### 🎯 Estrutura do Commit
```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

### 📋 Tipos de Commit

#### 🆕 **feat** - Nova funcionalidade
```bash
feat: add user registration endpoint
feat(auth): implement JWT token validation
feat(api): add pagination to user list
```
> Relaciona-se com **MINOR** no versionamento semântico

#### 🐛 **fix** - Correção de bug
```bash
fix: resolve null pointer exception in user service
fix(database): correct connection pool configuration
fix(api): handle empty request body properly
```
> Relaciona-se com **PATCH** no versionamento semântico

#### 📚 **docs** - Documentação
```bash
docs: update API documentation
docs(readme): add installation instructions
docs: fix typos in contributing guide
```
> Não inclui alterações em código

#### 🧪 **test** - Testes
```bash
test: add unit tests for user service
test(integration): add database connection tests
test: update test data for authentication
```
> Não inclui alterações em código de produção

#### 🏗️ **build** - Build e dependências
```bash
build: update Spring Boot to version 3.5.9
build(maven): add new dependency for validation
build: configure Docker multi-stage build
```

#### ⚡ **perf** - Performance
```bash
perf: optimize database queries in user repository
perf(cache): implement Redis caching for frequent queries
perf: reduce memory usage in file processing
```

#### 🎨 **style** - Formatação
```bash
style: fix code formatting and indentation
style: remove trailing whitespaces
style(lint): apply ESLint fixes
```
> Não inclui alterações funcionais

#### ♻️ **refactor** - Refatoração
```bash
refactor: extract user validation logic to separate class
refactor(service): simplify authentication flow
refactor: improve code readability in controller layer
```
> Não altera funcionalidade

#### 🔧 **chore** - Tarefas de manutenção
```bash
chore: update .gitignore file
chore(deps): update development dependencies
chore: configure IDE settings
```
> Não inclui alterações em código

#### 🔄 **ci** - Integração contínua
```bash
ci: add GitHub Actions workflow
ci(docker): update container build process
ci: configure automated testing pipeline
```

#### 📄 **raw** - Arquivos de configuração
```bash
raw: update application.yaml configuration
raw(env): add new environment variables
raw: modify database migration scripts
```

#### 🧹 **cleanup** - Limpeza de código
```bash
cleanup: remove commented code blocks
cleanup(imports): remove unused import statements
cleanup: delete obsolete configuration files
```

#### 🗑️ **remove** - Remoção de código
```bash
remove: delete deprecated user endpoints
remove(feature): remove legacy authentication system
remove: clean up unused utility classes
```

### 💡 Exemplos Práticos

#### ✅ Commits Bem Formatados
```bash
feat(auth): add OAuth2 integration with Google
fix(database): resolve connection timeout issues
docs(api): update OpenAPI specification
test(user): add comprehensive user service tests
perf(query): optimize user search with database indexes
refactor(controller): extract validation logic to separate layer
```

#### ❌ Commits Mal Formatados
```bash
# Muito vago
fix: bug fix

# Sem tipo
add new feature for users

# Descrição muito longa
feat: add a new comprehensive user management system with full CRUD operations, validation, authentication, and authorization

# Tipo incorreto
feat: fix typo in documentation  # deveria ser 'docs'
```

### 🔍 Dicas para Bons Commits

1. **📏 Tamanho**: Máximo 50 caracteres no título
2. **🎯 Clareza**: Seja específico sobre o que foi alterado
3. **🌍 Idioma**: Use português ou inglês consistentemente
4. **⏰ Tempo**: Use imperativo ("add" não "added")
5. **🔗 Contexto**: Adicione escopo quando necessário
6. **📋 Corpo**: Use o corpo para explicar "por quê", não "o quê"
7. **🔒 Segurança**: Nunca inclua credenciais, senhas ou dados sensíveis

### 🛠️ Configuração do Git

```bash
# Configurar template de commit (recomendado)
git config --global commit.template .gitmessage

# Configurar editor padrão
git config --global core.editor "code --wait"

# Habilitar autosquash para rebase interativo
git config --global rebase.autosquash true

# Configurar push padrão
git config --global push.default current
```

> **💡 Dica**: O arquivo `.gitmessage` no projeto contém um template útil com todos os tipos de commit e regras.

## 🤝 Como Contribuir

Este é um projeto educacional aberto! Siga o fluxo estabelecido:

### 📋 Processo de Contribuição

1. **🍴 Fork** o repositório
2. **🌿 Crie uma branch** seguindo o padrão:
   - `feature/nome-da-funcionalidade`
   - `fix/nome-do-bug`
   - `hotfix/nome-da-correcao-urgente`
3. **💻 Desenvolva** seguindo as boas práticas
4. **📝 Commit** usando conventional commits
5. **🔄 Abra um PR** para a branch `develop`
6. **👥 Aguarde review** (se necessário)

### 🎯 Áreas de Contribuição

- **🐛 Reportar bugs** ou sugerir melhorias
- **📝 Melhorar documentação** e exemplos
- **💡 Propor novos casos de uso** educacionais
- **🧪 Adicionar testes** e validações
- **🔧 Implementar funcionalidades** seguindo o roadmap
- **⚡ Otimizar performance** e qualidade do código

## 📚 Recursos de Estudo

### 📖 Documentação Oficial
- [Spring Boot 3.5 Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Framework 6 Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Java 21 Features](https://openjdk.java.net/projects/jdk/21/)
- [SpringDoc OpenAPI](https://springdoc.org/)

### 🎥 Tutoriais Recomendados
- Spring Boot Fundamentals
- JPA e Hibernate Avançado
- Microservices com Spring Cloud
- Testing com Spring Boot

### 🛠️ Ferramentas Úteis
- **IntelliJ IDEA** - IDE recomendada
- **Postman** - Testes de API
- **DBeaver** - Cliente PostgreSQL
- **Docker Desktop** - Containers locais

## ⚠️ Observações Importantes

### 🔒 Segurança
- **⚠️ CRÍTICO**: Nunca commite senhas ou chaves no código
- **📁 Arquivo .env**: Está no `.gitignore` e contém dados sensíveis
- **🔐 Credenciais**: Use sempre variáveis de ambiente para dados sensíveis
- **📖 Swagger**: Desabilitado em produção por padrão
- **✅ Validação**: Sempre valide inputs do usuário
- **🔍 Code Review**: Verifique se não há credenciais expostas antes do commit

### 🚀 Performance
- O HikariCP está otimizado para cada ambiente
- Logs de SQL são desabilitados em produção
- Use profiles apropriados para cada ambiente
- Monitore métricas com Actuator

### 📱 Compatibilidade
- **Java 21 LTS** é obrigatório
- **Spring Boot 3.5+** para recursos mais recentes
- **PostgreSQL 15+** recomendado
- **Maven 3.9+** para build

---

## 🎉 Conclusão

Este projeto representa o **estado da arte** em desenvolvimento Spring Boot, utilizando as **versões LTS estáveis** e **melhores práticas** da indústria. É perfeito para:

- 📚 **Aprender** conceitos fundamentais e avançados do Spring Boot 3
- 🔄 **Atualizar** conhecimentos com tecnologias modernas e estáveis
- 🏗️ **Iniciar** novos projetos com base sólida e bem estruturada
- 🎯 **Praticar** padrões de desenvolvimento profissional

**Happy Coding!** 🚀✨