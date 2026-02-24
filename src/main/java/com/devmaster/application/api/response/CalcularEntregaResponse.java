package com.devmaster.application.api.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Dados calculados de entrega")
public record CalcularEntregaResponse(
    @Schema(description = "Distância em quilômetros", example = "5.2")
    Double distanciaKm,

    @Schema(description = "Tempo estimado em minutos", example = "35")
    Integer tempoEstimadoMinutos,

    @Schema(description = "Taxa de entrega", example = "8.50")
    BigDecimal taxaEntrega,

    @Schema(description = "Endereço de origem (restaurante)")
    String enderecoOrigem,

    @Schema(description = "Endereço de destino (cliente)")
    String enderecoDestino
) {}
