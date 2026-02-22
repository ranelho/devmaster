package com.devmaster.application.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response com dados de cálculo de entrega.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados calculados de entrega")
public class CalcularEntregaResponse {

    @Schema(description = "Distância em quilômetros", example = "5.2")
    private Double distanciaKm;

    @Schema(description = "Tempo estimado em minutos", example = "35")
    private Integer tempoEstimadoMinutos;

    @Schema(description = "Taxa de entrega", example = "8.50")
    private BigDecimal taxaEntrega;

    @Schema(description = "Endereço de origem (restaurante)")
    private String enderecoOrigem;

    @Schema(description = "Endereço de destino (cliente)")
    private String enderecoDestino;
}
