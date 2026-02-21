package com.devmaster.application.api.response;

import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPagamento;
import com.devmaster.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response resumido para Pedido (listagens).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record PedidoResumoResponse(
    Long id,
    String numeroPedido,
    Long clienteId,
    String clienteNome,
    Long restauranteId,
    String restauranteNome,
    StatusPedido status,
    String statusDescricao,
    StatusPagamento statusPagamento,
    String statusPagamentoDescricao,
    BigDecimal total,
    LocalDateTime criadoEm,
    LocalDateTime previsaoEntrega
) {
    public static PedidoResumoResponse from(Pedido pedido) {
        return new PedidoResumoResponse(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getCliente().getId(),
            pedido.getCliente().getNomeCompleto(),
            pedido.getRestaurante().getId(),
            pedido.getRestaurante().getNome(),
            pedido.getStatus(),
            pedido.getStatus().getDescricao(),
            pedido.getStatusPagamento(),
            pedido.getStatusPagamento().getDescricao(),
            pedido.getTotal(),
            pedido.getCriadoEm(),
            pedido.getPrevisaoEntrega()
        );
    }
}
