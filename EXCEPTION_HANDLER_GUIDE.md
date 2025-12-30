# 🛡️ Guia do Global Exception Handler

## ✅ Implementação Concluída

Acabei de implementar um **GlobalExceptionHandler completo e profissional** que trata todos os tipos de exceções da aplicação de forma consistente e padronizada.

## 📁 Arquivos Criados

### 🛡️ **Handler Principal**
- `GlobalExceptionHandler.java` - Tratamento centralizado de exceções
- `ErrorResponse.java` - Resposta padronizada para erros
- `BusinessException.java` - Exceção customizada para regras de negócio
- `ExceptionDemoController.java` - Controller para testar os tratamentos

## 🎯 Tipos de Exceções Tratadas

### 1. **📝 Validação de Dados**
- `@Valid` - Validação de Bean Validation
- `@NotNull`, `@NotBlank`, `@Email`, etc.
- Constraint violations
- **Status**: 400 (Bad Request)

### 2. **🔄 Erros de Conversão**
- Tipo de dados incorreto
- Formato de data inválido
- Números em formato texto
- **Status**: 400 (Bad Request)

### 3. **🔗 Parâmetros de Requisição**
- Parâmetros obrigatórios ausentes
- Corpo da requisição malformado
- JSON/XML inválido
- **Status**: 400 (Bad Request)

### 4. **🌐 Erros HTTP**
- Endpoint não encontrado (404)
- Método não suportado (405)
- Tipo de mídia não suportado (415)
- **Status**: Específico de cada erro

### 5. **🗄️ Erros de Banco de Dados**
- Violação de integridade
- Chave duplicada
- Constraint de chave estrangeira
- **Status**: 409 (Conflict) ou 500

### 6. **💼 Regras de Negócio**
- Exceções customizadas (`BusinessException`)
- Recursos não encontrados
- Operações não permitidas
- **Status**: Configurável

### 7. **💥 Erros Genéricos**
- Exceções não tratadas especificamente
- Erros internos inesperados
- **Status**: 500 (Internal Server Error)

## 🧪 Como Testar

### 🚀 **Inicie a Aplicação**
```bash
mvn spring-boot:run
```

### 📖 **Acesse o Swagger UI**
```
http://localhost:9090/api/swagger
```

### 🧪 **Endpoints de Teste Disponíveis**

#### 1. **📝 Teste de Validação**
```bash
# Dados inválidos para gerar erro de validação
curl -X POST http://localhost:9090/api/demo/exceptions/validation \
  -H "Content-Type: application/json" \
  -d '{
    "name": "",
    "email": "email-inválido",
    "age": null,
    "password": "123"
  }'
```

**Resposta esperada (400)**:
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
    "email": "Email deve ter formato válido",
    "age": "Idade é obrigatória",
    "password": "Senha deve ter pelo menos 8 caracteres"
  }
}
```

#### 2. **🔍 Teste de Recurso Não Encontrado**
```bash
curl http://localhost:9090/api/demo/exceptions/not-found/123
```

**Resposta esperada (404)**:
```json
{
  "timestamp": "2025-12-26 10:30:45",
  "status": 404,
  "error": "Business Rule Violation",
  "message": "Usuário com ID 123 não encontrado",
  "path": "/api/demo/exceptions/not-found/123",
  "method": "GET",
  "details": {
    "errorCode": "RESOURCE_NOT_FOUND"
  }
}
```

#### 3. **🔄 Teste de Erro de Tipo**
```bash
# Use texto onde deveria ser número
curl "http://localhost:9090/api/demo/exceptions/type-mismatch?number=abc"
```

**Resposta esperada (400)**:
```json
{
  "timestamp": "2025-12-26 10:30:45",
  "status": 400,
  "error": "Type Mismatch",
  "message": "Tipo de dados inválido para o parâmetro",
  "path": "/api/demo/exceptions/type-mismatch",
  "method": "GET",
  "details": {
    "parameter": "number",
    "providedValue": "abc",
    "expectedType": "Integer",
    "description": "O valor fornecido não pode ser convertido para o tipo esperado"
  }
}
```

#### 4. **🔗 Teste de Parâmetro Ausente**
```bash
# Chame sem o parâmetro obrigatório 'name'
curl http://localhost:9090/api/demo/exceptions/missing-parameter
```

#### 5. **🚫 Teste de Operação Proibida**
```bash
curl -X POST http://localhost:9090/api/demo/exceptions/forbidden
```

#### 6. **⚠️ Teste de Conflito**
```bash
curl -X POST http://localhost:9090/api/demo/exceptions/conflict
```

#### 7. **🗄️ Teste de Erro de Banco**
```bash
curl -X POST http://localhost:9090/api/demo/exceptions/database-error
```

#### 8. **💥 Teste de Erro Genérico**
```bash
curl http://localhost:9090/api/demo/exceptions/generic-error
```

#### 9. **📊 Lista de Todos os Testes**
```bash
curl http://localhost:9090/api/demo/exceptions/error-types
```

## 🔧 Como Usar em Seus Controllers

### 1. **💼 Exceções de Negócio**
```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user == null) {
        throw BusinessException.notFound("Usuário");
    }
    return ResponseEntity.ok(user);
}

@PostMapping("/users")
public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
    if (userService.emailExists(request.getEmail())) {
        throw BusinessException.conflict("Email já está em uso");
    }
    
    User user = userService.create(request);
    return ResponseEntity.ok(user);
}
```

### 2. **📝 Validação Automática**
```java
public class CreateUserRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String name;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    // getters e setters...
}
```

### 3. **🎯 Exceções Customizadas**
```java
// Criar exceções específicas do domínio
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(Long id) {
        super("Usuário com ID " + id + " não encontrado", 
              HttpStatus.NOT_FOUND, 
              "USER_NOT_FOUND", 
              id);
    }
}

// Usar no controller
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id)
        .orElseThrow(() -> new UserNotFoundException(id));
    return ResponseEntity.ok(user);
}
```

## 📊 Estrutura da Resposta de Erro

### 🏗️ **Campos Padrão**
```json
{
  "timestamp": "2025-12-26 10:30:45",    // Momento do erro
  "status": 400,                         // Código HTTP
  "error": "Validation Failed",          // Tipo do erro
  "message": "Dados inválidos",          // Mensagem amigável
  "path": "/api/users",                  // Endpoint chamado
  "method": "POST",                      // Método HTTP
  "details": {                           // Detalhes específicos
    "field1": "erro específico",
    "field2": "outro erro"
  }
}
```

### 🎯 **Campos Opcionais**
```json
{
  "traceId": "ERR-2025-001234",          // ID para rastreamento
  "suggestion": "Verifique os dados",    // Sugestão de correção
  "documentationUrl": "https://..."      // Link para docs
}
```

## 🔍 Logs Estruturados

### 📋 **Exemplos de Logs**
```
❌ Erro de validação na requisição: POST /api/users
   📋 Campo 'name': Nome é obrigatório
   📋 Campo 'email': Email deve ter formato válido

💼 Exceção de negócio: GET /api/users/123 - Usuário não encontrado

🗄️ Violação de integridade de dados: POST /api/users
   💥 Detalhes: Duplicate entry 'user@example.com' for key 'users.email_unique'

💥 Erro interno não tratado: GET /api/some-endpoint
   🔍 Exceção: RuntimeException
```

## 🛠️ Configurações Avançadas

### 🎛️ **Personalizar Mensagens**
```java
// No application.yaml
spring:
  messages:
    basename: messages
    encoding: UTF-8

# Criar arquivo messages.properties
validation.name.required=Nome é obrigatório
validation.email.invalid=Email deve ter formato válido
```

### 🔧 **Adicionar Novos Handlers**
```java
@ExceptionHandler(CustomException.class)
public ResponseEntity<ErrorResponse> handleCustomException(
        CustomException ex, HttpServletRequest request) {
    
    // Sua lógica personalizada aqui
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(400)
            .error("Custom Error")
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .method(request.getMethod())
            .build());
}
```

### 📊 **Integrar com Monitoramento**
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex, HttpServletRequest request) {
    
    // Enviar métricas para monitoramento
    meterRegistry.counter("errors.internal", 
        "endpoint", request.getRequestURI(),
        "method", request.getMethod()).increment();
    
    // Enviar para sistema de alertas
    alertService.sendAlert("Internal Error", ex);
    
    // Resposta padrão...
}
```

## 🎯 Benefícios Implementados

### ✅ **Para Desenvolvedores**
- **Consistência**: Todas as respostas de erro seguem o mesmo padrão
- **Debug facilitado**: Logs estruturados com contexto completo
- **Manutenibilidade**: Tratamento centralizado e organizado
- **Extensibilidade**: Fácil adicionar novos tipos de erro

### ✅ **Para Usuários da API**
- **Clareza**: Mensagens de erro em português e descritivas
- **Detalhamento**: Informações específicas sobre o que corrigir
- **Padronização**: Estrutura consistente facilita integração
- **Documentação**: Swagger documenta todos os tipos de erro

### ✅ **Para Operações**
- **Monitoramento**: Logs estruturados facilitam alertas
- **Rastreabilidade**: Contexto completo para debugging
- **Métricas**: Fácil integração com sistemas de monitoramento
- **Suporte**: Informações suficientes para resolver problemas

## 🏆 Conclusão

Você agora tem um **sistema de tratamento de exceções profissional** que:

- ✅ Trata **todos os tipos de erro** de forma consistente
- ✅ Fornece **respostas padronizadas** e informativas
- ✅ Gera **logs estruturados** para debugging
- ✅ É **facilmente extensível** para novos cenários
- ✅ Segue **melhores práticas** da indústria
- ✅ Está **totalmente documentado** no Swagger

**Seu projeto está preparado para lidar com erros de forma elegante e profissional!** 🚀