package com.devmaster.application.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Request para calcular taxa e tempo de entrega.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@Schema(description = "Dados para calcular taxa e tempo de entrega")
public record CalcularEntregaRequest(
    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante", example = "1")
    Long restauranteId,

    @NotNull(message = "Latitude é obrigatória")
    @Schema(description = "Latitude do endereço de entrega", example = "-23.550520")
    Double latitude,

    @NotNull(message = "Longitude é obrigatória")
    @Schema(description = "Longitude do endereço de entrega", example = "-46.633308")
    Double longitude
) {}
