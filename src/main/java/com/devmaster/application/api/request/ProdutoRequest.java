package com.devmaster.application.api.request;

import com.devmaster.handler.validator.CategoriaExistente;
import com.devmaster.handler.validator.RestauranteExistente;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoRequest(
        @NotNull(message = "Categoria é obrigatória")
        @CategoriaExistente
        Long categoriaId,
        @NotNull(message = "Restaurante é obrigatório")
        @RestauranteExistente
        Long restauranteId,
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao,
        @Digits(integer = 3, fraction = 2)
        @NotNull(message = "Preço é obrigatório")
        @PositiveOrZero(message = "Preço não pode ser menor que zero")
        BigDecimal preco,
        @Digits(integer = 3, fraction = 2)
        @PositiveOrZero(message = "Preço promocional não pode ser menor que zero")
        BigDecimal precoPromocional,
        @Positive
        Integer tempoPreparo,
        String tipo,
        Boolean disponivel,
        Boolean destaque,
        @PositiveOrZero
        Integer ordemExibicao
) {
}
