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
- **📖 SpringDoc OpenAPI 2.6.0** - Documentação OpenAPI nativa
- **🎨 Swagger UI** - Interface visual para testar APIs
- **📋 Spring Boot Actuator** - Monitoramento e métricas

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
│   ├── � LoglgingAspect.java        # Logging automático com AOP
│   ├── � SwaggerCoCnfig.java        # Configuração do OpenAPI
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
- **📋 API Docs**: http://localhost:8081/api/api-docs
- **📊 Actuator**: http://localhost:8081/api/actuator

## 📚 Conceitos Abordados

### 🔧 Configuração e Setup
- ✅ **Multi-ambiente** com profiles do Spring
- ✅ **Variáveis de ambiente** com suporte nativo .env
- ✅ **Properties externalizadas** para flexibilidade
- ✅ **Docker Compose** para dependências locais (PostgreSQL + PgAdmin)

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
# Opção 1: Supabase (Cloud) - Configuração padrão no .env.example
DATABASE_URL=jdbc:postgresql://aws-1-us-east-2.pooler.supabase.com:5432/postgres
DATABASE_USERNAME=postgres.xsjkjiuixzowswxwszhd
DATABASE_PASSWORD=m9oAnUTEgnxzB54H

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

### � Document ação
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
   - Instalar Java 25 e Maven
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
│   ├── application-develop.yaml      # � CDesenvolvimento
│   ├── application-staging.yaml      # 🟡 Homologação
│   └── application-master.yaml       # 🔴 Produção
├── 📁 src/test/java/
│   └── DevmasterApplicationTests.java # 🧪 Testes da aplicação
├── docker-compose.yml                # � PPostgreSQL + PgAdmin local
├── .env.example                      # 📝 Exemplo de variáveis
├── pom.xml                           # � Deptendências Maven
└── README.md                         # � Estma documentação
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

## 🤝 Como Contribuir

Este é um projeto educacional aberto! Você pode contribuir:

1. **🐛 Reportando bugs** ou sugerindo melhorias
2. **📝 Melhorando a documentação**
3. **💡 Propondo novos exemplos** ou casos de uso
4. **🧪 Adicionando testes** e validações
5. **🔧 Implementando novas funcionalidades**

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
- **Nunca** commite senhas ou chaves no código
- Use variáveis de ambiente para dados sensíveis
- O Swagger está desabilitado em produção por padrão
- Sempre valide inputs do usuário

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