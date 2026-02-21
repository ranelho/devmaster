package com.devmaster.application.api.response;

import com.devmaster.domain.HistoricoDisponibilidadeEntregador;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta com dados do histórico de disponibilidade.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
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




