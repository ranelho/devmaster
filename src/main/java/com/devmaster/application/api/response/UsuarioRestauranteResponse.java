package com.devmaster.application.api.response;

import com.devmaster.domain.UsuarioRestaurante;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response com dados do vínculo usuário-restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record UsuarioRestauranteResponse(
    Long id,
    UUID usuarioId,
    Long restauranteId,
    String restauranteNome,
    RoleRestaurante role,
    Boolean ativo,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime criadoEm,
    
    UUID criadoPor
) {
    public static UsuarioRestauranteResponse from(UsuarioRestaurante vinculo) {
        return new UsuarioRestauranteResponse(
            vinculo.getId(),
            vinculo.getUsuarioId(),
            vinculo.getRestaurante().getId(),
            vinculo.getRestaurante().getNome(),
            vinculo.getRole(),
            vinculo.getAtivo(),
            vinculo.getCriadoEm(),
            vinculo.getCriadoPor()
        );
    }
}
