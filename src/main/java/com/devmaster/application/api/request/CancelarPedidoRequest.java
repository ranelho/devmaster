package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

/**
 * Request para cancelamento de pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record CancelarPedidoRequest(
    @NotBlank(message = "Motivo do cancelamento é obrigatório")
    @Size(max = 2000, message = "Motivo deve ter no máximo 2000 caracteres")
    String motivo
) {}
