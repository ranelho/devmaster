package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TipoPagamentoRequest(
        Boolean ativo,
        @NotBlank(message = "código é obrigatório")
        String codigo,
        @NotBlank(message = "descrição é obrigatório")
        String descricao,
        String iconeUrl,
        @NotNull(message = "Nome é obrigatório")
        String nome,
        Integer ordemExibicao,
        Boolean requerTroco
) {
}
