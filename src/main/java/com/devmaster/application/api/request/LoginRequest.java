package com.devmaster.application.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Requisição de login")
public record LoginRequest(
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(description = "Email do usuário", example = "usuario@example.com")
    String email,
    
    @NotBlank(message = "Password é obrigatório")
    @Schema(description = "Senha do usuário", example = "senha123")
    String password
) {}
