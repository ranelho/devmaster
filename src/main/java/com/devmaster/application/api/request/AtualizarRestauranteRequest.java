package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AtualizarRestauranteRequest(
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    String nome,
    
    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    String descricao,
    
    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    String telefone,
    
    @Email(message = "Email inválido")
    String email,
    
    String logoUrl,
    
    String bannerUrl,
    
    @DecimalMin(value = "0.0", message = "Taxa de entrega deve ser maior ou igual a 0")
    BigDecimal taxaEntrega,
    
    @DecimalMin(value = "0.0", message = "Valor mínimo do pedido deve ser maior ou igual a 0")
    BigDecimal valorMinimoPedido,
    
    @Min(value = 0, message = "Tempo médio de entrega deve ser maior ou igual a 0")
    Integer tempoMedioEntrega
) {}
