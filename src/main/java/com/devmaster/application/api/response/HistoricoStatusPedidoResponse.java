package com.devmaster.application.api.response;

import com.devmaster.domain.HistoricoStatusPedido;
import com.devmaster.domain.enums.StatusPedido;

import java.time.LocalDateTime;

/**
 * Response para HistoricoStatusPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record HistoricoStatusPedidoResponse(
    Long id,
    Long pedidoId,
    StatusPedido status,
    String statusDescricao,
    String observacoes,
    LocalDateTime criadoEm,
    String criadoPor
) {
    public static HistoricoStatusPedidoResponse from(HistoricoStatusPedido historico) {
        return new HistoricoStatusPedidoResponse(
            historico.getId(),
            historico.getPedido().getId(),
            historico.getStatus(),
            historico.getStatus().getDescricao(),
            historico.getObservacoes(),
            historico.getCriadoEm(),
            historico.getCriadoPor()
        );
    }
}
