package com.devmaster.application.api.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotNull(message = "Categoria é obrigatória")
        Long categoriaId,
        @NotNull(message = "Restaurante é obrigatório")
        Long restauranteId,
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao,
        @Digits(integer = 3, fraction = 2)
        @NotNull(message = "Preço é obrigatório")
        BigDecimal preco,
        @Digits(integer = 3, fraction = 2)
        BigDecimal precoPromocional,
        String tipo,
        Boolean disponivel,
        Boolean destaque
) {
}
