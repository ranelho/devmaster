package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AtualizarCupomRequest(
    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    String descricao,
    
    @DecimalMin(value = "0.01", message = "Valor do desconto deve ser maior que zero")
    BigDecimal valorDesconto,
    
    @DecimalMin(value = "0.0", message = "Valor mínimo do pedido deve ser maior ou igual a zero")
    BigDecimal valorMinimoPedido,
    
    @DecimalMin(value = "0.01", message = "Desconto máximo deve ser maior que zero")
    BigDecimal descontoMaximo,
    
    @Min(value = 1, message = "Limite de uso deve ser maior que zero")
    Integer limiteUso,
    
    LocalDateTime validoDe,
    
    LocalDateTime validoAte
) {}
