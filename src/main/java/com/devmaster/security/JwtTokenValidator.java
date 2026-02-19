package com.devmaster.security;

import com.devmaster.handler.APIException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class JwtTokenValidator {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;
    private final boolean interceptorEnabled;

    public JwtTokenValidator(
            RestTemplate restTemplate,
            @Value("${security.auth-service.url}") String authServiceUrl,
            @Value("${security.interceptor.enabled:true}") boolean interceptorEnabled
    ) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
        this.interceptorEnabled = interceptorEnabled;
        log.info("JwtTokenValidator inicializado - URL: {}, Enabled: {}", authServiceUrl, interceptorEnabled);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> validateToken(String token) {
        if (!interceptorEnabled) {
            log.warn("Interceptor de segurança está desabilitado!");
            return Map.of("sub", "anonymous", "roles", java.util.List.of("ROLE_USER"));
        }

        try {
            String validationUrl = authServiceUrl + "/auth/validate";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            
            HttpEntity<Void> request = new HttpEntity<>(headers);
            
            log.debug("Validando token no serviço: {}", validationUrl);
            
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    request,
                    Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.debug("Token validado com sucesso");
                return response.getBody();
            }
            
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
            
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Falha na validação do token", e);
        }
    }

    public Map<String, Object> extractTokenInfo(String token) {
        try {
            // Decodifica o payload do JWT (parte do meio)
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Token JWT inválido");
            }
            
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            
            // Converte JSON para Map (simplificado)
            log.debug("Token payload extraído: {}", payload);
            
            return Map.of("payload", payload);
            
        } catch (Exception e) {
            log.error("Erro ao extrair informações do token: {}", e.getMessage());
            return Map.of();
        }
    }
}
