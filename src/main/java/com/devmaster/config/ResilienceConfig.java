package com.devmaster.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ResilienceConfig {

    @Bean
    public RegistryEventConsumer<CircuitBreaker> circuitBreakerRegistryEventConsumer() {
        return new RegistryEventConsumer<>() {

            @Override
            public void onEntryAddedEvent(@NonNull EntryAddedEvent<CircuitBreaker> entryAddedEvent) {
                CircuitBreaker circuitBreaker = entryAddedEvent.getAddedEntry();
                String name = circuitBreaker.getName();

                log.info("🔧 Circuit Breaker '{}' foi registrado", name);

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

                circuitBreaker.getEventPublisher()
                        .onSuccess(event ->
                                log.debug("✅ Circuit Breaker '{}': Chamada bem-sucedida (duração: {}ms)",
                                        name, event.getElapsedDuration().toMillis()));

                circuitBreaker.getEventPublisher()
                        .onError(event ->
                                log.warn("❌ Circuit Breaker '{}': Falha detectada - {} (duração: {}ms)",
                                        name, event.getThrowable().getClass().getSimpleName(),
                                        event.getElapsedDuration().toMillis()));

                circuitBreaker.getEventPublisher()
                        .onCallNotPermitted(event ->
                                log.warn("🚫 Circuit Breaker '{}': Chamada rejeitada - Circuito ABERTO", name));
            }

            @Override
            public void onEntryRemovedEvent(@NonNull EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
                log.info("🗑️ Circuit Breaker '{}' foi removido", entryRemoveEvent.getRemovedEntry().getName());
            }

            @Override
            public void onEntryReplacedEvent(@NonNull EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
                log.info("🔄 Circuit Breaker '{}' foi substituído", entryReplacedEvent.getNewEntry().getName());
            }
        };
    }
}