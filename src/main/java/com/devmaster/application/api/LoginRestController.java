package com.devmaster.application.api;

import com.devmaster.application.api.request.ChangePasswordRequest;
import com.devmaster.application.api.request.LoginRequest;
import com.devmaster.application.api.response.LoginResponse;
import com.devmaster.application.api.response.LogoutResponse;
import com.devmaster.application.service.AuthIntegrationService;
import com.devmaster.application.service.UsuarioRestauranteService;
import com.devmaster.handler.APIException;
import com.devmaster.security.UserContext;
import com.devmaster.security.UserContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginRestController implements LoginAPI {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    private final WebClient.Builder webClientBuilder;
    private final AuthIntegrationService authIntegrationService;
    private final UserContextService userContextService;
    
    @Value("${auth.service.url:http://localhost:8082}")
    private String authServiceUrl;
    
    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        log.info("🔐 [LOGIN] Tentativa de login - Email: {}", request.email());
        
        try {
            // 1. Fazer login no Auth Service
            WebClient webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
            
            Map<String, String> authRequest = Map.of(
                "email", request.email(),
                "password", request.password()
            );
            
            @SuppressWarnings("unchecked")
            Map<String, Object> authResponse = webClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            
            if (authResponse == null) {
                log.warn("⚠️ [LOGIN] Resposta vazia do Auth Service");
                throw APIException.build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
            }
            
            // 2. Extrair dados do usuário
            @SuppressWarnings("unchecked")
            Map<String, Object> userMap = (Map<String, Object>) authResponse.get("user");
            
            if (userMap == null) {
                log.error("❌ [LOGIN] Dados do usuário não encontrados na resposta");
                throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar dados do usuário");
            }
            
            String userId = (String) userMap.get("id");
            String username = (String) userMap.get("username");
            String email = (String) userMap.get("email");
            
            @SuppressWarnings("unchecked")
            List<String> rolesList = (List<String>) userMap.get("roles");
            Set<String> roles = rolesList != null ? Set.copyOf(rolesList) : Set.of();
            
            // 3. Buscar restauranteId do usuário (pode ser null)
            Long restauranteId = null;
            try {
                restauranteId = usuarioRestauranteService.buscarRestauranteIdDoUsuario(UUID.fromString(userId));
                log.info("🏪 [LOGIN] RestauranteId encontrado: {}", restauranteId);
            } catch (Exception e) {
                log.warn("⚠️ [LOGIN] Usuário sem restaurante vinculado: {}", userId);
            }
            
            // 4. Montar resposta
            LoginResponse response = LoginResponse.builder()
                .accessToken((String) authResponse.get("accessToken"))
                .refreshToken((String) authResponse.get("refreshToken"))
                .tokenType((String) authResponse.get("tokenType"))
                .expiresIn(((Number) authResponse.get("expiresIn")).longValue())
                .user(LoginResponse.UserInfo.builder()
                    .id(userId)
                    .username(username)
                    .email(email)
                    .roles(roles)
                    .restauranteId(restauranteId)
                    .build())
                .build();
            
            log.info("✅ [LOGIN] Sucesso - Usuario: {}, RestauranteId: {}", userId, restauranteId);
            
            return ResponseEntity.ok(response);
            
        } catch (WebClientResponseException.Unauthorized e) {
            log.warn("🚫 [LOGIN] Credenciais inválidas - Email: {}", request.email());
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos");
        } catch (WebClientResponseException e) {
            log.error("❌ [LOGIN] Erro na comunicação com Auth Service - Status: {}", e.getStatusCode());
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Erro ao comunicar com serviço de autenticação");
        } catch (APIException e) {
            throw e;
        } catch (Exception e) {
            log.error("💥 [LOGIN] Erro inesperado ao fazer login", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar login");
        }
    }
    
    @Override
    public ResponseEntity<LogoutResponse> logout(String authorization) {
        log.info("🚪 [LOGOUT] Iniciando logout");
        
        try {
            WebClient webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
            
            webClient.post()
                .uri("/api/auth/logout")
                .header("Authorization", authorization)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
            
            log.info("✅ [LOGOUT] Logout realizado com sucesso");
            
            return ResponseEntity.ok(
                LogoutResponse.builder()
                    .message("Logout realizado com sucesso")
                    .logoutAt(LocalDateTime.now())
                    .build()
            );
            
        } catch (WebClientResponseException.Unauthorized e) {
            log.warn("🚫 [LOGOUT] Token inválido ou expirado");
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
        } catch (WebClientResponseException e) {
            log.error("❌ [LOGOUT] Erro na comunicação com Auth Service - Status: {}", e.getStatusCode());
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Erro ao comunicar com serviço de autenticação");
        } catch (Exception e) {
            log.error("💥 [LOGOUT] Erro inesperado ao fazer logout", e);
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar logout");
        }
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest request) {
        UserContext user = userContextService.getCurrentUser();
        authIntegrationService.changePassword(user.getUserId(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
