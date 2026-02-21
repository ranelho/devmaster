package com.devmaster.application.api.request;

import com.devmaster.domain.enums.StatusPedido;
import jakarta.validation.constraints.*;

/**
 * Request para atualização de status do pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record AtualizarStatusPedidoRequest(
    @NotNull(message = "Status é obrigatório")
    StatusPedido status,
    
    @Size(max = 2000, message = "Observações devem ter no máximo 2000 caracteres")
    String observacoes
) {}
