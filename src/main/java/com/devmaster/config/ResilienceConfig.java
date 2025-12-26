package com.devmaster.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 🔧 Configuração do Resilience4j para Circuit Breaker, Retry e Timeout
 * 
 * Esta configuração centraliza o monitoramento e logging dos padrões de resiliência,
 * fornecendo observabilidade completa sobre o comportamento dos circuit breakers.
 * 
 * @author DevMaster
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class ResilienceConfig {

    /**
     * 📊 Registra eventos do Circuit Breaker Registry para monitoramento
     * 
     * Monitora quando novos circuit breakers são criados, removidos ou substituídos,
     * e automaticamente adiciona listeners para eventos de estado.
     */
    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerRegistryEventConsumer() {
        return new RegistryEventConsumer<CircuitBreaker>() {
            
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                CircuitBreaker circuitBreaker = entryAddedEvent.getAddedEntry();
                String name = circuitBreaker.getName();
                
                log.info("🔧 Circuit Breaker '{}' foi registrado", name);
                
                // Adiciona listeners para monitorar mudanças de estado
                circuitBreaker.getEventPublisher()
                    .onStateTransition(event -> {
                        String fromState = event.getStateTransition().getFromState().toString();
                        String toState = event.getStateTransition().getToState().toString();
                        
                        switch (toState) {
                            case "OPEN" -> log.warn("🔴 Circuit Breaker '{}': {} → {} (Circuito ABERTO - Falhas detectadas)", 
                                name, fromState, toState);
                            case "HALF_OPEN" -> log.info("🟡 Circuit Breaker '{}': {} → {} (Testando recuperação)", 
                                name, fromState, toState);
                            case "CLOSED" -> log.info("🟢 Circuit Breaker '{}': {} → {} (Circuito FECHADO - Funcionando normalmente)", 
                                name, fromState, toState);
                            default -> log.debug("🔄 Circuit Breaker '{}': {} → {}", name, fromState, toState);
                        }
                    });
                
                // Monitora chamadas bem-sucedidas
                circuitBreaker.getEventPublisher()
                    .onSuccess(event -> 
                        log.debug("✅ Circuit Breaker '{}': Chamada bem-sucedida (duração: {}ms)", 
                            name, event.getElapsedDuration().toMillis()));
                
                // Monitora falhas
                circuitBreaker.getEventPublisher()
                    .onError(event -> 
                        log.warn("❌ Circuit Breaker '{}': Falha detectada - {} (duração: {}ms)", 
                            name, event.getThrowable().getClass().getSimpleName(), 
                            event.getElapsedDuration().toMillis()));
                
                // Monitora chamadas rejeitadas (quando circuito está aberto)
                circuitBreaker.getEventPublisher()
                    .onCallNotPermitted(event -> 
                        log.warn("🚫 Circuit Breaker '{}': Chamada rejeitada - Circuito ABERTO", name));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("🗑️ Circuit Breaker '{}' foi removido", entryRemoveEvent.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("🔄 Circuit Breaker '{}' foi substituído", entryReplacedEvent.getNewEntry().getName());
            }
        };
    }
}