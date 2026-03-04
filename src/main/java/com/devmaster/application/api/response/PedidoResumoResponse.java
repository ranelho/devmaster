package com.devmaster.application.api.response;

import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPagamento;
import com.devmaster.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResumoResponse(
    Long id,
    String numeroPedido,
    String clienteNome,
    String restauranteNome,
    BigDecimal valorTotal,
    StatusPedido status,
    StatusPagamento statusPagamento,
    String statusDescricao,
    LocalDateTime dataPedido,
    LocalDateTime criadoEm,
    LocalDateTime previsaoEntrega
) {

    public static PedidoResumoResponse from(Pedido pedido) {
        return new PedidoResumoResponse(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getClienteNome(),
            pedido.getRestaurante() != null ? pedido.getRestaurante().getNome() : null,
            pedido.getValorTotal(),
            pedido.getStatus(),
            pedido.getStatusPagamento(),
            pedido.getStatus().getDescricao(),
            pedido.getDataPedido(),
            pedido.getCriadoEm(),
            pedido.getPrevisaoEntrega()
        );
    }
}
