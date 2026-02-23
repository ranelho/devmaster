package com.devmaster.application.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

@Builder
@Schema(description = "Resposta de login com token JWT")
public record LoginResponse(
    
    @Schema(description = "Token JWT de autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,
    
    @Schema(description = "Token de refresh", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String refreshToken,
    
    @Schema(description = "Tipo do token", example = "Bearer")
    String tokenType,
    
    @Schema(description = "Tempo de expiração em segundos", example = "3600")
    Long expiresIn,
    
    @Schema(description = "Informações do usuário autenticado")
    UserInfo user
) {
    
    @Builder
    public record UserInfo(
        @Schema(description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
        String id,
        
        @Schema(description = "Nome de usuário", example = "joaosilva")
        String username,
        
        @Schema(description = "Email do usuário", example = "joao@example.com")
        String email,
        
        @Schema(description = "Roles do usuário", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
        Set<String> roles,
        
        @Schema(description = "ID do restaurante associado", example = "1")
        Long restauranteId
    ) {}
}
