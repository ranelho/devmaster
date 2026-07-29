package com.devmaster.application.api.response;

import com.devmaster.domain.Categoria;

import java.util.List;

public record CategoriaResponse(
        Long id,
        Long restauranteId,
        String nome,
        String descricao,
        Integer ordemExibicao,
        Boolean ativo
) {

    public CategoriaResponse(Categoria categoria) {
        this(
                categoria.getId(),
                categoria.getRestaurante().getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getOrdemExibicao(),
                categoria.getAtivo()
        );
    }

    public static List<CategoriaResponse> convert(List<Categoria> list) {
        return list.stream().map(CategoriaResponse::new).toList();
    }
}
