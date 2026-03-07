package com.devmaster.application.api.response;

import com.devmaster.domain.TipoPagamento;
import org.springframework.data.domain.Page;

import java.util.List;

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

    public static List<TipoPagamentoResponse> convert(List<TipoPagamento> response) {
        return response.stream().map(TipoPagamentoResponse::new).toList();
    }

    public static Page<TipoPagamentoResponse> convertPageble(Page<TipoPagamento> response) {
        return response.map(TipoPagamentoResponse::new);
    }
}
