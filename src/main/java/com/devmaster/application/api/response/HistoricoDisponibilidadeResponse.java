package com.devmaster.application.api.response;

import com.devmaster.domain.HistoricoDisponibilidadeEntregador;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoricoDisponibilidadeResponse(
    Long id,
    Long entregadorId,
    Boolean disponivel,
    BigDecimal latitude,
    BigDecimal longitude,
    LocalDateTime criadoEm
) {
    public static HistoricoDisponibilidadeResponse from(HistoricoDisponibilidadeEntregador historico) {
        return new HistoricoDisponibilidadeResponse(
            historico.getId(),
            historico.getEntregador().getId(),
            historico.getDisponivel(),
            historico.getLatitude(),
            historico.getLongitude(),
            historico.getCriadoEm()
        );
    }
}




