package com.devmaster.application.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request para vincular entregador a restaurante")
public record VincularEntregadorRequest(
    
    @NotNull(message = "ID do entregador é obrigatório")
    @Schema(description = "ID do entregador", example = "1")
    Long entregadorId,
    
    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante", example = "1")
    Long restauranteId
) {}
