package com.devmaster.application.api.request;

import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VincularUsuarioRestauranteRequest(
    @NotNull(message = "ID do usuário é obrigatório")
    UUID usuarioId,
    
    @NotNull(message = "Role é obrigatória")
    RoleRestaurante role
) {}
