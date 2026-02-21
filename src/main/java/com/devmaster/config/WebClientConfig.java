package com.devmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuração do WebClient para chamadas HTTP.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
