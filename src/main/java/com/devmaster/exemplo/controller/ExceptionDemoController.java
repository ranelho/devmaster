package com.devmaster.exemplo.controller;

import com.devmaster.handler.APIException;
import com.devmaster.handler.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 🧪 Controller para demonstrar o GlobalExceptionHandler
 * 
 * Este controller contém endpoints que geram diferentes tipos de exceções
 * para demonstrar como o GlobalExceptionHandler trata cada uma delas.
 * 
 * Útil para:
 * - Testar tratamento de erros
 * - Documentar comportamento da API
 * - Validar respostas de erro
 * - Demonstrar boas práticas
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/demo/exceptions")
@Tag(name = "Exception Demo", description = "🧪 Endpoints para demonstrar tratamento de exceções")
public class ExceptionDemoController {

    /**
     * 📝 Testa validação de campos (@Valid)
     */
    @PostMapping("/validation")
    @Operation(
        summary = "🧪 Testar Validação de Campos",
        description = "Endpoint para testar validação de Bean Validation. " +
                     "Envie dados inválidos para ver como os erros são tratados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Dados válidos processados"),
        @ApiResponse(responseCode = "400", description = "❌ Erro de validação", 
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Map<String, Object>> testValidation(@Valid @RequestBody UserRequest request) {
        log.info("📝 Dados válidos recebidos: {}", request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Dados válidos processados com sucesso");
        response.put("data", request);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 🔍 Testa exceção de recurso não encontrado
     */
    @GetMapping("/not-found/{id}")
    @Operation(
        summary = "🔍 Testar Recurso Não Encontrado",
        description = "Simula busca por recurso inexistente. Use qualquer ID para gerar erro 404."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "404", description = "❌ Recurso não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> testNotFound(
            @Parameter(description = "ID do recurso", example = "123")
            @PathVariable String id) {
        
        log.info("🔍 Buscando recurso com ID: {}", id);
        
        // Simula busca que sempre falha
        throw APIException.build(HttpStatus.NOT_FOUND, "Usuário com ID " + id);
    }

    /**
     * 🚫 Testa operação não permitida
     */
    @PostMapping("/forbidden")
    @Operation(
        summary = "🚫 Testar Operação Proibida",
        description = "Simula operação que o usuário não tem permissão para executar."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "403", description = "❌ Operação não permitida",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> testForbidden() {
        log.info("🚫 Tentativa de operação não permitida");
        
        throw APIException.build(HttpStatus.FORBIDDEN, "deletar usuário administrador");
    }

    /**
     * ⚠️ Testa conflito de dados
     */
    @PostMapping("/conflict")
    @Operation(
        summary = "⚠️ Testar Conflito de Dados",
        description = "Simula tentativa de criar recurso que já existe."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "409", description = "❌ Conflito de dados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> testConflict() {
        log.info("⚠️ Tentativa de criar recurso duplicado");
        
        throw APIException.build(HttpStatus.CONFLICT,"Email já está em uso por outro usuário");
    }

    /**
     * 🔄 Testa erro de conversão de tipo
     */
    @GetMapping("/type-mismatch")
    @Operation(
        summary = "🔄 Testar Erro de Tipo",
        description = "Teste erro de conversão de tipo. Use texto no parâmetro 'number' para gerar erro."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Conversão bem-sucedida"),
        @ApiResponse(responseCode = "400", description = "❌ Erro de conversão de tipo",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Map<String, Object>> testTypeMismatch(
            @Parameter(description = "Número inteiro", example = "123")
            @RequestParam Integer number) {
        
        log.info("🔄 Número recebido: {}", number);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Número processado com sucesso");
        response.put("number", number);
        response.put("doubled", number * 2);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 🔗 Testa parâmetro obrigatório ausente
     */
    @GetMapping("/missing-parameter")
    @Operation(
        summary = "🔗 Testar Parâmetro Ausente",
        description = "Teste parâmetro obrigatório. Chame sem o parâmetro 'name' para gerar erro."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Parâmetro fornecido"),
        @ApiResponse(responseCode = "400", description = "❌ Parâmetro obrigatório ausente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Map<String, Object>> testMissingParameter(
            @Parameter(description = "Nome obrigatório", example = "João")
            @RequestParam(required = true) String name) {
        
        log.info("🔗 Nome recebido: {}", name);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Olá, " + name + "!");
        response.put("name", name);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 💥 Testa exceção genérica
     */
    @GetMapping("/generic-error")
    @Operation(
        summary = "💥 Testar Erro Genérico",
        description = "Simula erro interno não tratado especificamente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "500", description = "❌ Erro interno do servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> testGenericError() {
        log.info("💥 Simulando erro genérico");
        
        // Simula erro inesperado
        throw new RuntimeException("Erro simulado para demonstração");
    }

    /**
     * 🗄️ Testa erro de banco de dados
     */
    @PostMapping("/database-error")
    @Operation(
        summary = "🗄️ Testar Erro de Banco",
        description = "Simula erro de violação de integridade do banco de dados."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "409", description = "❌ Violação de integridade",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> testDatabaseError() {
        log.info("🗄️ Simulando erro de banco de dados");
        
        // Simula violação de constraint única
        throw new DataIntegrityViolationException(
            "Duplicate entry 'user@example.com' for key 'users.email_unique'"
        );
    }

    /**
     * 📊 Lista todos os tipos de erro disponíveis
     */
    @GetMapping("/error-types")
    @Operation(
        summary = "📊 Listar Tipos de Erro",
        description = "Lista todos os tipos de erro que podem ser testados neste controller."
    )
    @ApiResponse(responseCode = "200", description = "✅ Lista de tipos de erro")
    public ResponseEntity<Map<String, Object>> listErrorTypes() {
        Map<String, Object> errorTypes = new HashMap<>();
        
        errorTypes.put("validation", Map.of(
            "endpoint", "POST /demo/exceptions/validation",
            "description", "Testa validação de campos com @Valid",
            "example", "Envie JSON com campos inválidos"
        ));
        
        errorTypes.put("notFound", Map.of(
            "endpoint", "GET /demo/exceptions/not-found/{id}",
            "description", "Testa recurso não encontrado (404)",
            "example", "GET /demo/exceptions/not-found/123"
        ));
        
        errorTypes.put("forbidden", Map.of(
            "endpoint", "POST /demo/exceptions/forbidden",
            "description", "Testa operação não permitida (403)",
            "example", "POST /demo/exceptions/forbidden"
        ));
        
        errorTypes.put("conflict", Map.of(
            "endpoint", "POST /demo/exceptions/conflict",
            "description", "Testa conflito de dados (409)",
            "example", "POST /demo/exceptions/conflict"
        ));
        
        errorTypes.put("typeMismatch", Map.of(
            "endpoint", "GET /demo/exceptions/type-mismatch?number={value}",
            "description", "Testa erro de conversão de tipo (400)",
            "example", "GET /demo/exceptions/type-mismatch?number=abc"
        ));
        
        errorTypes.put("missingParameter", Map.of(
            "endpoint", "GET /demo/exceptions/missing-parameter",
            "description", "Testa parâmetro obrigatório ausente (400)",
            "example", "GET /demo/exceptions/missing-parameter (sem ?name=)"
        ));
        
        errorTypes.put("genericError", Map.of(
            "endpoint", "GET /demo/exceptions/generic-error",
            "description", "Testa erro genérico (500)",
            "example", "GET /demo/exceptions/generic-error"
        ));
        
        errorTypes.put("databaseError", Map.of(
            "endpoint", "POST /demo/exceptions/database-error",
            "description", "Testa erro de banco de dados (409)",
            "example", "POST /demo/exceptions/database-error"
        ));
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Tipos de erro disponíveis para teste");
        response.put("errorTypes", errorTypes);
        response.put("totalTypes", errorTypes.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 📝 DTO para teste de validação
     */
    @Data
    @Schema(description = "Dados de usuário para teste de validação")
    public static class UserRequest {
        
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
        @Schema(description = "Nome do usuário", example = "João Silva")
        private String name;
        
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        @Schema(description = "Email do usuário", example = "joao@example.com")
        private String email;
        
        @NotNull(message = "Idade é obrigatória")
        @Schema(description = "Idade do usuário", example = "25")
        private Integer age;
        
        @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
        @Schema(description = "Senha do usuário", example = "senha123")
        private String password;
    }
}