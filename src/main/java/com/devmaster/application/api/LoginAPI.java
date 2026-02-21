package com.devmaster.application.api;

import com.devmaster.application.api.request.LoginRequest;
import com.devmaster.application.api.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API para autenticação integrada com busca de restaurante.
 */
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
@RequestMapping("/v1/auth")
public interface LoginAPI {
    
    @Operation(summary = "Login com restaurante", 
               description = "Faz login no Auth Service e retorna o restauranteId do usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);
}
