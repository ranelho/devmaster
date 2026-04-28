package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RestauranteRequest(
    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 14, message = "CNPJ deve conter até 14 caracteres")
    String cnpj,
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve conter até 255 caracteres")
    String nome,
    String descricao,
    @NotNull(message = "Aberto é obrigatório")
    Boolean aberto,
    @Size(max = 20, message = "Telefone deve conter até 20 caracteres")
    String telefone,
    @Size(max = 255, message = "Email deve conter até 255 caracteres")
    String email,
    @NotBlank(message = "Slug é obrigatório")
    @Size(max = 255, message = "Slug deve conter até 255 caracteres")
    String slug,
    Long empresaId,
    @Size(max = 500, message = "Banner Url deve conter até 500 caracteres")
    String bannerUrl,
    @Size(max = 500, message = "Logo Url deve conter até 500 caracteres")
    String logoUrl,
    BigDecimal avaliacao,
    BigDecimal taxaEntrega
){}
