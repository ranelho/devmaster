package com.devmaster.application.service.impl;

import com.devmaster.application.service.AuthIntegrationService;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

/**
 * Implementação do serviço de integração com o Auth Service.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthIntegrationApplicationService implements AuthIntegrationService {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${auth.service.url:http://localhost:8080}")
    private String authServiceUrl;
    
    @Override
    public UUID criarUsuario(String nome, String email, String password, boolean isAdmin) {
        log.info("Criando usuário no Auth Service: email={}, isAdmin={}", email, isAdmin);
        log.debug("Dados enviados: nome={}, email={}, password length={}", nome, email, password != null ? password.length() : 0);
        
        try {
            WebClient webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
            
            Map<String, Object> request = Map.of(
                "nomeCompleto", nome,
                "email", email,
                "password", password
            );
            
            log.debug("Request body: {}", request);
            
            String endpoint = isAdmin ? "/api/auth/register/admin" : "/api/auth/register";
            
            log.info("Chamando endpoint: {}{}", authServiceUrl, endpoint);
            
            // Auth Service retorna 201 com {message: "..."} em caso de sucesso
            // Não retorna o userId na resposta, então precisamos buscar por email
            webClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                    status -> status.is4xxClientError() || status.is5xxServerError(),
                    clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            log.error("Erro ao criar usuário: status={}, body={}", 
                                clientResponse.statusCode(), errorBody);
                            return Mono.error(new WebClientResponseException(
                                clientResponse.statusCode().value(),
                                "Erro ao criar usuário",
                                null, errorBody.getBytes(), null));
                        })
                )
                .bodyToMono(Map.class)
                .block();
            
            log.info("Usuário criado com sucesso no Auth Service: email={}", email);
            
            // Buscar o usuário por email para obter o UUID real
            log.info("Buscando usuário por email para obter UUID: {}", email);
            
            Map<String, Object> userInfo = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/auth/user/by-email")
                    .queryParam("email", email)
                    .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            
            if (userInfo != null && userInfo.containsKey("id")) {
                String userId = (String) userInfo.get("id");
                log.info("UUID do usuário obtido: {}", userId);
                return UUID.fromString(userId);
            }
            
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao obter ID do usuário criado");
            
        } catch (WebClientResponseException e) {
            log.error("Erro ao criar usuário no Auth Service: status={}, body={}", 
                e.getStatusCode(), e.getResponseBodyAsString());
            
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw APIException.build(HttpStatus.CONFLICT, 
                    "Email ou nome de usuário já cadastrado");
            }
            
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao comunicar com serviço de autenticação: " + e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao criar usuário no Auth Service", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, 
                "Erro ao criar usuário: " + e.getMessage());
        }
    }
    
    @Override
    public boolean usuarioExiste(UUID usuarioId) {
        try {
            UsuarioAuthInfo usuario = buscarUsuario(usuarioId);
            return usuario != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public UsuarioAuthInfo buscarUsuario(UUID usuarioId) {
        log.info("Buscando usuário no Auth Service: id={}", usuarioId);
        
        try {
            // TODO: Implementar endpoint no Auth Service para buscar usuário por ID
            // Por enquanto, retorna informações mockadas
            
            log.warn("Endpoint de busca de usuário não implementado no Auth Service. Retornando mock.");
            
            return new UsuarioAuthInfo(
                usuarioId,
                "Usuário",
                "usuario@example.com",
                true
            );
            
        } catch (Exception e) {
            log.error("Erro ao buscar usuário no Auth Service", e);
            throw APIException.build(HttpStatus.NOT_FOUND, 
                "Usuário não encontrado no serviço de autenticação");
        }
    }
}
