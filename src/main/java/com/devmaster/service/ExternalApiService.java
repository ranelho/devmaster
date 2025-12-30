package com.devmaster.service;

import com.devmaster.handler.BusinessException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

/**
 * 🌐 Serviço de exemplo para demonstrar Circuit Breaker com APIs externas
 * 
 * Este serviço simula chamadas para APIs externas e demonstra como aplicar
 * os padrões de resiliência (Circuit Breaker, Retry, Timeout) de forma prática.
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Slf4j
@Service
public class ExternalApiService {

    private final RestTemplate restTemplate;

    public ExternalApiService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 🔄 Exemplo de chamada para API externa com Circuit Breaker
     * 
     * Aplica os padrões:
     * - Circuit Breaker: Protege contra falhas em cascata
     * - Retry: Tenta novamente em caso de falha temporária  
     * - Timeout: Evita chamadas que ficam "penduradas"
     * 
     * @param url URL da API externa
     * @return Resposta da API ou fallback em caso de falha
     */
    @CircuitBreaker(name = "external-api", fallbackMethod = "fallbackExternalApi")
    @Retry(name = "external-api")
    @TimeLimiter(name = "external-api")
    public CompletableFuture<String> callExternalApi(String url) {
        log.info("🌐 Chamando API externa: {}", url);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simula chamada real para API externa
                String response = restTemplate.getForObject(url, String.class);
                log.info("✅ Resposta recebida da API externa: {} caracteres", 
                    response != null ? response.length() : 0);
                return response;
            } catch (Exception e) {
                log.error("❌ Erro ao chamar API externa: {}", e.getMessage());
                throw new BusinessException("Falha na comunicação com API externa " + e.getMessage());
            }
        });
    }

    /**
     * 🛡️ Método de fallback para chamadas de API externa
     * 
     * Executado quando:
     * - Circuit breaker está aberto
     * - Todas as tentativas de retry falharam
     * - Timeout foi excedido
     */
    public CompletableFuture<String> fallbackExternalApi(String url, Exception ex) {
        log.warn("🛡️ Executando fallback para API externa. URL: {}, Erro: {}", 
            url, ex.getMessage());
        
        return CompletableFuture.completedFuture(
            "{ \"status\": \"fallback\", \"message\": \"Serviço temporariamente indisponível\" }"
        );
    }

    /**
     * 🗄️ Exemplo de operação de banco com Circuit Breaker
     * 
     * Demonstra como proteger operações de banco de dados com padrões de resiliência.
     * Útil para cenários onde o banco pode estar sobrecarregado ou instável.
     */
    @CircuitBreaker(name = "database", fallbackMethod = "fallbackDatabase")
    @Retry(name = "database")
    @TimeLimiter(name = "database")
    public CompletableFuture<String> performDatabaseOperation(String query) {
        log.info("🗄️ Executando operação no banco: {}", query);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simula operação no banco de dados
                Thread.sleep(100); // Simula latência
                
                // Simula falha ocasional para demonstrar circuit breaker
                if (Math.random() < 0.3) { // 30% de chance de falha
                    throw new BusinessException("Timeout na conexão com banco");
                }
                
                log.info("✅ Operação no banco executada com sucesso");
                return "Operação realizada com sucesso";
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException("Operação interrompida " + e);
            } catch (Exception e) {
                log.error("❌ Erro na operação do banco: {}", e.getMessage());
                throw new BusinessException("Falha na operação do banco " + e);
            }
        });
    }

    /**
     * 🛡️ Método de fallback para operações de banco
     */
    public CompletableFuture<String> fallbackDatabase(String query, Exception ex) {
        log.warn("🛡️ Executando fallback para banco. Query: {}, Erro: {}", 
            query, ex.getMessage());
        
        return CompletableFuture.completedFuture(
            "Operação adiada - banco temporariamente indisponível"
        );
    }

    /**
     * 📊 Método para simular diferentes cenários de teste
     * 
     * Útil para testar o comportamento do circuit breaker em diferentes situações:
     * - success: Sempre funciona
     * - failure: Sempre falha  
     * - timeout: Demora muito para responder
     * - intermittent: Falha esporadicamente
     */
    @CircuitBreaker(name = "external-api", fallbackMethod = "fallbackTestScenario")
    @Retry(name = "external-api")
    public CompletableFuture<String> testScenario(String scenario) {
        log.info("🧪 Testando cenário: {}", scenario);
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                return switch (scenario.toLowerCase()) {
                    case "success" -> "✅ Cenário de sucesso executado";
                    case "failure" -> throw new BusinessException("💥 Cenário de falha simulada");
                    case "timeout" -> {
                        Thread.sleep(20000); // 20 segundos - vai dar timeout
                        yield "⏰ Este cenário não deveria chegar aqui"; // 20 segundos - vai dar timeout
                    }
                    case "intermittent" -> {
                        if (Math.random() < 0.7) { // 70% de chance de falha
                            throw new BusinessException("🎲 Falha intermitente");
                        }
                        yield "🎯 Sucesso intermitente";
                    }
                    default -> "❓ Cenário desconhecido: " + scenario;
                };
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new BusinessException("Teste interrompido " + e);
            }
        });
    }

    /**
     * 🛡️ Fallback para cenários de teste
     */
    public CompletableFuture<String> fallbackTestScenario(String scenario, Exception ex) {
        log.warn("🛡️ Fallback do cenário '{}': {}", scenario, ex.getMessage());
        return CompletableFuture.completedFuture(
            "🛡️ Fallback executado para cenário: " + scenario
        );
    }
}