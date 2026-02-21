package com.devmaster.application.api.response;

import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPagamento;
import com.devmaster.domain.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response completo para Pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record PedidoResponse(
    Long id,
    String numeroPedido,
    Long clienteId,
    String clienteNome,
    String clienteTelefone,
    Long restauranteId,
    String restauranteNome,
    EnderecoClienteResponse enderecoEntrega,
    String tipoPagamento,
    Boolean tipoPagamentoRequerTroco,
    BigDecimal valorTroco,
    StatusPedido status,
    String statusDescricao,
    StatusPagamento statusPagamento,
    String statusPagamentoDescricao,
    BigDecimal subtotal,
    BigDecimal taxaEntrega,
    BigDecimal desconto,
    BigDecimal total,
    String observacoes,
    LocalDateTime previsaoEntrega,
    LocalDateTime criadoEm,
    LocalDateTime confirmadoEm,
    LocalDateTime preparandoEm,
    LocalDateTime prontoEm,
    LocalDateTime despachadoEm,
    LocalDateTime entregueEm,
    LocalDateTime canceladoEm,
    String motivoCancelamento,
    String codigoCupom,
    List<ItemPedidoResponse> itens,
    List<HistoricoStatusPedidoResponse> historico
) {
    public static PedidoResponse from(
        Pedido pedido,
        String codigoCupom,
        List<ItemPedidoResponse> itens,
        List<HistoricoStatusPedidoResponse> historico
    ) {
        return new PedidoResponse(
            pedido.getId(),
            pedido.getNumeroPedido(),
            pedido.getCliente().getId(),
            pedido.getCliente().getNomeCompleto(),
            pedido.getCliente().getTelefone(),
            pedido.getRestaurante().getId(),
            pedido.getRestaurante().getNome(),
            EnderecoClienteResponse.from(pedido.getEnderecoEntrega()),
            pedido.getTipoPagamento().getNome(),
            pedido.getTipoPagamento().getRequerTroco(),
            pedido.getValorTroco(),
            pedido.getStatus(),
            pedido.getStatus().getDescricao(),
            pedido.getStatusPagamento(),
            pedido.getStatusPagamento().getDescricao(),
            pedido.getSubtotal(),
            pedido.getTaxaEntrega(),
            pedido.getDesconto(),
            pedido.getTotal(),
            pedido.getObservacoes(),
            pedido.getPrevisaoEntrega(),
            pedido.getCriadoEm(),
            pedido.getConfirmadoEm(),
            pedido.getPreparandoEm(),
            pedido.getProntoEm(),
            pedido.getDespachadoEm(),
            pedido.getEntregueEm(),
            pedido.getCanceladoEm(),
            pedido.getMotivoCancelamento(),
            codigoCupom,
            itens,
            historico
        );
    }
}
