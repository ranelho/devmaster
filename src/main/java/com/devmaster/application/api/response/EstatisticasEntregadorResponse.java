package com.devmaster.application.api.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record EstatisticasEntregadorResponse(
    Long entregadorId,
    String nomeCompleto,
    Integer totalEntregas,
    BigDecimal avaliacaoMedia,
    BigDecimal taxaSucesso,
    Integer tempoMedioEntregaMinutos,
    Integer entregasUltimos30Dias,
    Integer totalMudancasDisponibilidade,
    Integer documentosVerificados,
    Integer documentosPendentes,
    Integer documentosVencidos
) {}
