package com.devmaster.exemplo.controller;

import com.devmaster.exemplo.controller.service.ExternalApiService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 🔧 Controller para demonstrar e testar padrões de resiliência
 * 
 * Este controller fornece endpoints para:
 * - Testar circuit breakers em diferentes cenários
 * - Monitorar o estado dos circuit breakers
 * - Simular falhas para fins educacionais
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/resilience")
@RequiredArgsConstructor
@Tag(name = "Resilience", description = "🔧 Endpoints para testar padrões de resiliência (Circuit Breaker, Retry, Timeout)")
public class ResilienceController {

    private final ExternalApiService externalApiService;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * 🌐 Testa chamada para API externa com circuit breaker
     */
    @GetMapping("/external-api")
    @Operation(
        summary = "🌐 Testar API Externa",
        description = "Simula chamada para API externa com circuit breaker, retry e timeout. " +
                     "Use URLs como 'https://httpbin.org/delay/2' para testar diferentes cenários."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Chamada realizada com sucesso"),
        @ApiResponse(responseCode = "500", description = "❌ Falha na chamada (fallback executado)")
    })
    public CompletableFuture<ResponseEntity<Map<String, Object>>> testExternalApi(
            @Parameter(description = "URL da API externa para testar", example = "https://httpbin.org/get")
            @RequestParam String url) {
        
        log.info("🌐 Iniciando teste de API externa: {}", url);
        
        return externalApiService.callExternalApi(url)
            .thenApply(response -> {
                Map<String, Object> result = new HashMap<>();
                result.put("status", "success");
                result.put("url", url);
                result.put("response", response);
                result.put("timestamp", System.currentTimeMillis());
                
                return ResponseEntity.ok(result);
            })
            .exceptionally(ex -> {
                log.error("❌ Erro no teste de API externa: {}", ex.getMessage());
                
                Map<String, Object> result = new HashMap<>();
                result.put("status", "error");
                result.put("url", url);
                result.put("error", ex.getMessage());
                result.put("timestamp", System.currentTimeMillis());
                
                return ResponseEntity.status(500).body(result);
            });
    }

    /**
     * 🗄️ Testa operação de banco com circuit breaker
     */
    @GetMapping("/database")
    @Operation(
        summary = "🗄️ Testar Operação de Banco",
        description = "Simula operação no banco de dados com circuit breaker. " +
                     "Inclui falhas aleatórias (30%) para demonstrar o funcionamento."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Operação realizada com sucesso"),
        @ApiResponse(responseCode = "500", description = "❌ Falha na operação (fallback executado)")
    })
    public CompletableFuture<ResponseEntity<Map<String, Object>>> testDatabase(
            @Parameter(description = "Query SQL simulada", example = "SELECT * FROM users")
            @RequestParam(defaultValue = "SELECT * FROM test_table") String query) {
        
        log.info("🗄️ Iniciando teste de banco: {}", query);
        
        return externalApiService.performDatabaseOperation(query)
            .thenApply(response -> {
                Map<String, Object> result = new HashMap<>();
                result.put("status", "success");
                result.put("query", query);
                result.put("result", response);
                result.put("timestamp", System.currentTimeMillis());
                
                return ResponseEntity.ok(result);
            })
            .exceptionally(ex -> {
                log.error("❌ Erro no teste de banco: {}", ex.getMessage());
                
                Map<String, Object> result = new HashMap<>();
                result.put("status", "error");
                result.put("query", query);
                result.put("error", ex.getMessage());
                result.put("timestamp", System.currentTimeMillis());
                
                return ResponseEntity.status(500).body(result);
            });
    }

    /**
     * 🧪 Testa diferentes cenários de falha
     */
    @GetMapping("/test/{scenario}")
    @Operation(
        summary = "🧪 Testar Cenários",
        description = "Testa diferentes cenários para demonstrar o circuit breaker:\n" +
                     "- **success**: Sempre funciona\n" +
                     "- **failure**: Sempre falha\n" +
                     "- **timeout**: Demora muito (timeout)\n" +
                     "- **intermittent**: Falha esporadicamente (70%)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Cenário executado"),
        @ApiResponse(responseCode = "500", description = "❌ Cenário falhou (fallback executado)")
    })
    public CompletableFuture<ResponseEntity<Map<String, Object>>> testScenario(
            @Parameter(description = "Tipo de cenário", example = "success")
            @PathVariable String scenario) {
        
        log.info("🧪 Iniciando teste de cenário: {}", scenario);
        
        return externalApiService.testScenario(scenario)
            .thenApply(response -> {
                Map<String, Object> result = new HashMap<>();
                result.put("status", "success");
                result.put("scenario", scenario);
                result.put("result", response);
                result.put("timestamp", System.currentTimeMillis());
                
                return ResponseEntity.ok(result);
            })
            .exceptionally(ex -> {
                log.error("❌ Erro no cenário '{}': {}", scenario, ex.getMessage());
                
                Map<String, Object> result = new HashMap<>();
                result.put("status", "error");
                result.put("scenario", scenario);
                result.put("error", ex.getMessage());
                result.put("timestamp", System.currentTimeMillis());
                
                return ResponseEntity.status(500).body(result);
            });
    }

    /**
     * 📊 Monitora o estado dos circuit breakers
     */
    @GetMapping("/status")
    @Operation(
        summary = "📊 Status dos Circuit Breakers",
        description = "Retorna o estado atual de todos os circuit breakers configurados, " +
                     "incluindo métricas de falhas, sucessos e estado atual."
    )
    @ApiResponse(responseCode = "200", description = "✅ Status retornado com sucesso")
    public ResponseEntity<Map<String, Object>> getCircuitBreakerStatus() {
        log.info("📊 Consultando status dos circuit breakers");
        
        Map<String, Object> status = new HashMap<>();
        
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(circuitBreaker -> {
            String name = circuitBreaker.getName();
            CircuitBreaker.State state = circuitBreaker.getState();
            CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();
            
            Map<String, Object> cbStatus = new HashMap<>();
            cbStatus.put("state", state.toString());
            cbStatus.put("failureRate", String.format("%.2f%%", metrics.getFailureRate()));
            cbStatus.put("numberOfSuccessfulCalls", metrics.getNumberOfSuccessfulCalls());
            cbStatus.put("numberOfFailedCalls", metrics.getNumberOfFailedCalls());
            cbStatus.put("numberOfNotPermittedCalls", metrics.getNumberOfNotPermittedCalls());
            
            // Emoji baseado no estado
            String emoji = switch (state) {
                case CLOSED -> "🟢";
                case OPEN -> "🔴";
                case HALF_OPEN -> "🟡";
                default -> "⚪";
            };
            
            cbStatus.put("emoji", emoji);
            cbStatus.put("description", getStateDescription(state));
            
            status.put(name, cbStatus);
        });
        
        Map<String, Object> result = new HashMap<>();
        result.put("circuitBreakers", status);
        result.put("timestamp", System.currentTimeMillis());
        result.put("totalCircuitBreakers", status.size());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 🔄 Reset de um circuit breaker específico
     */
    @PostMapping("/reset/{name}")
    @Operation(
        summary = "🔄 Reset Circuit Breaker",
        description = "Força o reset de um circuit breaker específico, " +
                     "retornando-o para o estado CLOSED (fechado)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "✅ Circuit breaker resetado"),
        @ApiResponse(responseCode = "404", description = "❌ Circuit breaker não encontrado")
    })
    public ResponseEntity<Map<String, Object>> resetCircuitBreaker(
            @Parameter(description = "Nome do circuit breaker", example = "external-api")
            @PathVariable String name) {
        
        log.info("🔄 Resetando circuit breaker: {}", name);
        
        try {
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
            circuitBreaker.reset();
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Circuit breaker '" + name + "' foi resetado");
            result.put("circuitBreaker", name);
            result.put("newState", circuitBreaker.getState().toString());
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("❌ Erro ao resetar circuit breaker '{}': {}", name, e.getMessage());
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "error");
            result.put("message", "Circuit breaker '" + name + "' não encontrado");
            result.put("error", e.getMessage());
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(404).body(result);
        }
    }

    /**
     * 📋 Lista todos os circuit breakers disponíveis
     */
    @GetMapping("/list")
    @Operation(
        summary = "📋 Listar Circuit Breakers",
        description = "Lista todos os circuit breakers registrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "✅ Lista retornada com sucesso")
    public ResponseEntity<Map<String, Object>> listCircuitBreakers() {
        log.info("📋 Listando circuit breakers disponíveis");
        
        Map<String, Object> result = new HashMap<>();
        result.put("circuitBreakers", circuitBreakerRegistry.getAllCircuitBreakers()
            .stream()
            .map(CircuitBreaker::getName)
            .toList());
        result.put("count", circuitBreakerRegistry.getAllCircuitBreakers().size());
        result.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(result);
    }

    /**
     * 📝 Descrição amigável do estado do circuit breaker
     */
    private String getStateDescription(CircuitBreaker.State state) {
        return switch (state) {
            case CLOSED -> "Funcionando normalmente - todas as chamadas são permitidas";
            case OPEN -> "Circuito aberto - chamadas são rejeitadas (muitas falhas detectadas)";
            case HALF_OPEN -> "Testando recuperação - permitindo algumas chamadas para verificar se o serviço voltou";
            default -> "Estado desconhecido";
        };
    }
}