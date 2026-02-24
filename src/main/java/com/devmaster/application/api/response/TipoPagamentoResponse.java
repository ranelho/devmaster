package com.devmaster.application.api.response;

import com.devmaster.domain.TipoPagamento;

public record TipoPagamentoResponse(
    Long id,
    String nome,
    String codigo,
    String descricao,
    String iconeUrl,
    Boolean ativo,
    Boolean requerTroco,
    Integer ordemExibicao
) {
    public static TipoPagamentoResponse from(TipoPagamento tipo) {
        return new TipoPagamentoResponse(
            tipo.getId(),
            tipo.getNome(),
            tipo.getCodigo(),
            tipo.getDescricao(),
            tipo.getIconeUrl(),
            tipo.getAtivo(),
            tipo.getRequerTroco(),
            tipo.getOrdemExibicao()
        );
    }
}
