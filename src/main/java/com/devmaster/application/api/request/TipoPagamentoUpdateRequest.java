package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;

public record TipoPagamentoUpdateRequest(
        Boolean ativo,
        String descricao,
        String iconeUrl,
        @NotNull(message = "Nome é obrigatório")
        String nome,
        Integer ordemExibicao,
        Boolean requerTroco
) {
}
