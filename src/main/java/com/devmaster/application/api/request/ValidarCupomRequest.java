package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ValidarCupomRequest(
    @NotBlank(message = "Código do cupom é obrigatório")
    String codigo,
    
    @NotNull(message = "Valor do pedido é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do pedido deve ser maior que zero")
    BigDecimal valorPedido
) {}
