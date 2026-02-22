package com.devmaster.application.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para calcular taxa e tempo de entrega.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para calcular taxa e tempo de entrega")
public class CalcularEntregaRequest {

    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante", example = "1")
    private Long restauranteId;

    @NotNull(message = "Latitude é obrigatória")
    @Schema(description = "Latitude do endereço de entrega", example = "-23.550520")
    private Double latitude;

    @NotNull(message = "Longitude é obrigatória")
    @Schema(description = "Longitude do endereço de entrega", example = "-46.633308")
    private Double longitude;
}
