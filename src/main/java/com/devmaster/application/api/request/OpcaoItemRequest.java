package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;

public record OpcaoItemRequest(
    @NotNull(message = "Grupo de opção é obrigatório")
    Long grupoOpcaoId,
    
    @NotNull(message = "Opção é obrigatória")
    Long opcaoId
) {}
