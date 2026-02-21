package com.devmaster.application.api.response;

import com.devmaster.domain.Categoria;

import java.time.LocalDateTime;

/**
 * DTO de resposta com dados da categoria.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record CategoriaResponse(
    Long id,
    Long restauranteId,
    String nome,
    String descricao,
    Integer ordemExibicao,
    Boolean ativo,
    LocalDateTime criadoEm
) {
    public static CategoriaResponse from(Categoria categoria) {
        return new CategoriaResponse(
            categoria.getId(),
            categoria.getRestaurante().getId(),
            categoria.getNome(),
            categoria.getDescricao(),
            categoria.getOrdemExibicao(),
            categoria.getAtivo(),
            categoria.getCriadoEm()
        );
    }
}
