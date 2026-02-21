package com.devmaster.application.api;

import com.devmaster.application.api.request.LoginRequest;
import com.devmaster.application.api.response.LoginResponse;
import com.devmaster.application.service.UsuarioRestauranteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginRestController implements LoginAPI {
    
    private final UsuarioRestauranteService usuarioRestauranteService;
    private final WebClient.Builder webClientBuilder;
    
    @Value("${auth.service.url:http://localhost:8082}")
    private String authServiceUrl;
    
    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        log.info("[LOGIN] Email: {}", request.email());
        
        try {
            // 1. Fazer login no Auth Service
            WebClient webClient = webClientBuilder
                .baseUrl(authServiceUrl)
                .build();
            
            Map<String, Object> authRequest = Map.of(
                "email", request.email(),
                "password", request.password()
            );
            
            Map<String, Object> authResponse = webClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequest)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            
            if (authResponse == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // 2. Extrair dados do usuário
            Map<String, Object> userMap = (Map<String, Object>) authResponse.get("user");
            String userId = (String) userMap.get("id");
            String username = (String) userMap.get("username");
            String email = (String) userMap.get("email");
            Set<String> roles = Set.copyOf((java.util.List<String>) userMap.get("roles"));
            
            // 3. Buscar restauranteId do usuário
            Long restauranteId = usuarioRestauranteService.buscarRestauranteIdDoUsuario(UUID.fromString(userId));
            
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
            
            log.info("[LOGIN] Sucesso - Usuario: {}, RestauranteId: {}", userId, restauranteId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("[LOGIN] Erro ao fazer login", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
