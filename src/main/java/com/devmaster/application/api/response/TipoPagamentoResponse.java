package com.devmaster.application.api.response;

import com.devmaster.domain.TipoPagamento;

public record TipoPagamentoResponse(
        Long id,
        Boolean ativo,
        String codigo,
        String descricao,
        String iconeUrl,
        String nome,
        Integer ordemExibicao,
        Boolean requerTroco
) {
    public TipoPagamentoResponse(TipoPagamento tipoPagamento) {
        this(
                tipoPagamento.getId(),
                tipoPagamento.getAtivo(),
                tipoPagamento.getCodigo(),
                tipoPagamento.getDescricao(),
                tipoPagamento.getIconeUrl(),
                tipoPagamento.getNome(),
                tipoPagamento.getOrdemExibicao(),
                tipoPagamento.getRequerTroco()
        );
    }
}
