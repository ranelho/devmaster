package com.devmaster.application.api.request;

import com.devmaster.domain.enums.TipoDesconto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de requisição para criar cupom.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record CupomRequest(
    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    @Pattern(regexp = "^[A-Z0-9_-]+$", message = "Código deve conter apenas letras maiúsculas, números, hífen e underscore")
    String codigo,
    
    @Size(max = 5000, message = "Descrição deve ter no máximo 5000 caracteres")
    String descricao,
    
    @NotNull(message = "Tipo de desconto é obrigatório")
    TipoDesconto tipoDesconto,
    
    @NotNull(message = "Valor do desconto é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor do desconto deve ser maior que zero")
    BigDecimal valorDesconto,
    
    @DecimalMin(value = "0.0", message = "Valor mínimo do pedido deve ser maior ou igual a zero")
    BigDecimal valorMinimoPedido,
    
    @DecimalMin(value = "0.01", message = "Desconto máximo deve ser maior que zero")
    BigDecimal descontoMaximo,
    
    @Min(value = 1, message = "Limite de uso deve ser maior que zero")
    Integer limiteUso,
    
    @NotNull(message = "Data de início da validade é obrigatória")
    LocalDateTime validoDe,
    
    @NotNull(message = "Data de fim da validade é obrigatória")
    LocalDateTime validoAte
) {}
