package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;

/**
 * Request para opção de item do pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record OpcaoItemRequest(
    @NotNull(message = "Grupo de opção é obrigatório")
    Long grupoOpcaoId,
    
    @NotNull(message = "Opção é obrigatória")
    Long opcaoId
) {}
