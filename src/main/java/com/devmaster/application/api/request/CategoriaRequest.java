package com.devmaster.application.api.request;

import com.devmaster.handler.validator.RestauranteExistente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ser até 100 caracteres")
        String nome,
        @NotNull(message = "Restaurante é obrigatório")
        @RestauranteExistente
        Long restauranteId,
        String descricao,
        Integer ordemExibicao,
        Boolean ativo
) {
}
