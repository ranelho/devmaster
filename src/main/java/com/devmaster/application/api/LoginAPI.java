package com.devmaster.application.api;

import com.devmaster.application.api.request.ChangePasswordRequest;
import com.devmaster.application.api.request.LoginRequest;
import com.devmaster.application.api.response.LoginResponse;
import com.devmaster.application.api.response.LogoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API para autenticação integrada com busca de restaurante.
 */
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
public interface LoginAPI {
    
    @Operation(summary = "Login com restaurante", 
               description = "Faz login no Auth Service e retorna o restauranteId do usuário")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @PostMapping("/public/auth/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);
    
    @Operation(
        summary = "Logout", 
        description = "Invalida o token JWT no Auth Service",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Token inválido ou expirado")
    @PostMapping("/public/auth/logout")
    ResponseEntity<LogoutResponse> logout(
        @Parameter(description = "Token JWT", required = true)
        @RequestHeader("Authorization") String authorization
    );

    @Operation(
            summary = "Alterar senha",
            description = "Altera a senha do usuário autenticado",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @PostMapping("/api/auth/change-password")
    ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request);
}
