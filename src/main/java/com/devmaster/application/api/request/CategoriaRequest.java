package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

public record CategoriaRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,
    
    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    String descricao,
    
    @Min(value = 0, message = "Ordem de exibição deve ser maior ou igual a 0")
    Integer ordemExibicao
) {}
