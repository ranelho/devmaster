package com.devmaster.application.api.request;

import com.devmaster.handler.validator.ProdutoExistente;
import jakarta.validation.constraints.NotNull;

public record ImagemProdutoRequest (
        @NotNull @ProdutoExistente
        Long produtoId,
        Boolean principal,
        Integer ordemExibicao
) {
}
