package com.devmaster.security.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class TokenValidationService {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public TokenValidationService(
            RestTemplate restTemplate,
            @Value("${security.auth.service.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    @CircuitBreaker(name = "auth-service", fallbackMethod = "validateTokenFallback")
    @Retry(name = "auth-service")
    public boolean validateToken(String token) {
        try {
            String url = authServiceUrl + "/api/auth/validate-token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            
            log.debug("Validando token no serviço de autenticação: {}", url);
            
            ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
            );
            
            boolean isValid = response.getStatusCode() == HttpStatus.OK;
            log.debug("Resultado da validação do token: {}", isValid);
            
            return isValid;
            
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            throw e;
        }
    }

    private boolean validateTokenFallback(String token, Exception e) {
        log.error("Fallback ativado para validação de token. Erro: {}", e.getMessage());
        return false;
    }
}
