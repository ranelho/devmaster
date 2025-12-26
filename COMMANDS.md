# 🛠️ Comandos e Scripts Úteis

Este documento contém comandos essenciais para trabalhar com o projeto Devamaster.

## 🚀 Comandos Maven

### Compilação e Build
```bash
# Compilação básica
mvn clean compile

# Build completo com testes
mvn clean package

# Build sem testes (desenvolvimento rápido)
mvn clean package -DskipTests

# Build com profile específico
mvn clean package -Pstaging

# Verificar dependências
mvn dependency:tree
```

### Execução da Aplicação
```bash
# Desenvolvimento (profile padrão)
mvn spring-boot:run

# Com profile específico
mvn spring-boot:run -Dspring-boot.run.profiles=develop
mvn spring-boot:run -Dspring-boot.run.profiles=staging
mvn spring-boot:run -Dspring-boot.run.profiles=master

# Com JVM arguments otimizados para Java 25
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-XX:+UseZGC -XX:+UnlockExperimentalVMOptions --enable-preview"

# Com variáveis de ambiente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081 --logging.level.com.devamaster=DEBUG"
```

### Testes
```bash
# Todos os testes
mvn test

# Testes específicos
mvn test -Dtest=HealthControllerTest

# Testes de integração
mvn verify

# Testes com coverage
mvn clean test jacoco:report
```

## 🐳 Comandos Docker

### Banco de Dados
```bash
# Iniciar PostgreSQL
docker-compose up -d postgres

# Iniciar PostgreSQL + PgAdmin
docker-compose up -d

# Ver logs do banco
docker-compose logs -f postgres

# Parar serviços
docker-compose down

# Limpar volumes (CUIDADO: apaga dados)
docker-compose down -v
```

### Aplicação
```bash
# Build da imagem Docker (futuro)
docker build -t devamaster:latest .

# Executar com Docker
docker run -p 8080:8080 --env-file .env devamaster:latest
```

## 🔧 Comandos de Desenvolvimento

### Análise de Código
```bash
# SpotBugs (análise estática)
mvn spotbugs:check

# Checkstyle (estilo de código)
mvn checkstyle:check

# PMD (análise de código)
mvn pmd:check

# Dependency check (vulnerabilidades)
mvn org.owasp:dependency-check-maven:check
```

### Documentação
```bash
# Gerar documentação JavaDoc
mvn javadoc:javadoc

# Site do projeto
mvn site

# OpenAPI spec generation
curl http://localhost:8080/api/api-docs > openapi.json
curl http://localhost:8080/api/api-docs.yaml > openapi.yaml
```

## 🎯 Scripts de Automação

### setup.sh (Linux/Mac)
```bash
#!/bin/bash
echo "🚀 Configurando ambiente Devamaster..."

# Verificar Java 25
java -version | grep "25" || {
    echo "❌ Java 25 não encontrado!"
    exit 1
}

# Copiar arquivo de ambiente
cp .env.example .env
echo "📝 Arquivo .env criado"

# Iniciar banco de dados
docker-compose up -d postgres
echo "🐘 PostgreSQL iniciado"

# Aguardar banco ficar pronto
sleep 10

# Executar aplicação
mvn spring-boot:run
```

### setup.bat (Windows)
```batch
@echo off
echo 🚀 Configurando ambiente Devamaster...

REM Verificar Java 25
java -version | findstr "25" >nul
if errorlevel 1 (
    echo ❌ Java 25 não encontrado!
    exit /b 1
)

REM Copiar arquivo de ambiente
copy .env.example .env
echo 📝 Arquivo .env criado

REM Iniciar banco de dados
docker-compose up -d postgres
echo 🐘 PostgreSQL iniciado

REM Aguardar banco ficar pronto
timeout /t 10 /nobreak >nul

REM Executar aplicação
mvn spring-boot:run
```

## 📊 Comandos de Monitoramento

### Health Checks
```bash
# Status básico
curl http://localhost:8080/api/health

# Informações detalhadas
curl http://localhost:8080/api/health/info

# Métricas do Actuator
curl http://localhost:8080/api/actuator/metrics

# Health check do Actuator
curl http://localhost:8080/api/actuator/health
```

### Logs
```bash
# Seguir logs em tempo real
tail -f logs/application.log

# Filtrar logs por nível
grep "ERROR" logs/application.log

# Logs do último minuto
find logs/ -name "*.log" -newermt "1 minute ago" -exec tail -f {} +
```

## 🔍 Comandos de Debug

### JVM Debugging
```bash
# Executar com debug remoto
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

# Profiling com JFR
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=app-profile.jfr"

# Memory dump
jcmd <PID> GC.run_finalization
jcmd <PID> VM.memory_dump heap.hprof
```

### Database
```bash
# Conectar ao PostgreSQL
docker exec -it devamaster-postgres psql -U devamaster -d devamaster_dev

# Backup do banco
docker exec devamaster-postgres pg_dump -U devamaster devamaster_dev > backup.sql

# Restore do banco
docker exec -i devamaster-postgres psql -U devamaster devamaster_dev < backup.sql
```

## 🚀 Comandos de Deploy

### Build para Produção
```bash
# Build otimizado
mvn clean package -Pproduction -DskipTests

# Build com profile específico
mvn clean package -Dspring.profiles.active=master

# Criar JAR executável
mvn clean package spring-boot:repackage
```

### Variáveis de Ambiente para Deploy
```bash
# Staging
export SPRING_PROFILES_ACTIVE=staging
export DATABASE_URL=jdbc:postgresql://staging-db:5432/devamaster_staging
export DATABASE_USERNAME=devamaster_staging
export DATABASE_PASSWORD=${STAGING_DB_PASSWORD}

# Produção
export SPRING_PROFILES_ACTIVE=master
export DATABASE_URL=jdbc:postgresql://prod-db:5432/devamaster_prod
export DATABASE_USERNAME=devamaster_prod
export DATABASE_PASSWORD=${PROD_DB_PASSWORD}
export SWAGGER_ENABLED=false
```

## 🧪 Comandos de Teste

### Testes de Carga
```bash
# Apache Bench
ab -n 1000 -c 10 http://localhost:8080/api/health

# curl em loop
for i in {1..100}; do curl -s http://localhost:8080/api/health > /dev/null; done
```

### Testes de API
```bash
# Swagger UI
open http://localhost:8080/api/swagger-ui.html

# Postman collection export
curl http://localhost:8080/api/api-docs | jq . > postman-collection.json
```

## 📋 Checklist de Deploy

### Pré-Deploy
- [ ] Testes passando: `mvn test`
- [ ] Build sem erros: `mvn clean package`
- [ ] Variáveis de ambiente configuradas
- [ ] Banco de dados acessível
- [ ] Swagger desabilitado em produção

### Pós-Deploy
- [ ] Health check: `curl /api/health`
- [ ] Logs sem erros
- [ ] Métricas funcionando
- [ ] Performance aceitável

---

**Use estes comandos para maximizar sua produtividade com o projeto Devamaster!** 🚀