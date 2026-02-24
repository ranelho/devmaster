package com.devmaster.application.api.response;

import com.devmaster.domain.GrupoOpcao;

import java.util.List;

public record GrupoOpcaoResponse(
    Long id,
    Long produtoId,
    String nome,
    String descricao,
    Integer minimoSelecoes,
    Integer maximoSelecoes,
    Boolean obrigatorio,
    Integer ordemExibicao,
    List<OpcaoResponse> opcoes
) {
    public static GrupoOpcaoResponse from(GrupoOpcao grupo, List<OpcaoResponse> opcoes) {
        return new GrupoOpcaoResponse(
            grupo.getId(),
            grupo.getProduto().getId(),
            grupo.getNome(),
            grupo.getDescricao(),
            grupo.getMinimoSelecoes(),
            grupo.getMaximoSelecoes(),
            grupo.getObrigatorio(),
            grupo.getOrdemExibicao(),
            opcoes
        );
    }
}
