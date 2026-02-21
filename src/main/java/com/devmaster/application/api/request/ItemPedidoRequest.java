package com.devmaster.application.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * Request para item do pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record ItemPedidoRequest(
    @NotNull(message = "Produto é obrigatório")
    Long produtoId,
    
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    Integer quantidade,
    
    @Size(max = 2000, message = "Observações devem ter no máximo 2000 caracteres")
    String observacoes,
    
    @Valid
    List<OpcaoItemRequest> opcoes
) {}
