package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;

public record TipoPagamentoRequest(
        Boolean ativo,
        String codigo,
        @NotNull(message = "descrição é obrigatório")
        String descricao,
        String iconeUrl,
        @NotNull(message = "Nome é obrigatório")
        String nome,
        Integer ordemExibicao,
        Boolean requerTroco
) {
}
