package com.devmaster.application.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@Schema(description = "Resposta de logout")
public record LogoutResponse(
    
    @Schema(description = "Mensagem de confirmação", example = "Logout realizado com sucesso")
    String message,
    
    @Schema(description = "Data/hora do logout", example = "2025-01-26T10:30:00")
    LocalDateTime logoutAt
) {}
