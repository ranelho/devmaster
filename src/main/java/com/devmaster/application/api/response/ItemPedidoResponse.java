package com.devmaster.application.api.response;

import com.devmaster.domain.ItemPedido;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response para ItemPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record ItemPedidoResponse(
    Long id,
    Long produtoId,
    String produtoNome,
    Integer quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal,
    String observacoes,
    List<OpcaoItemPedidoResponse> opcoes
) {
    public static ItemPedidoResponse from(ItemPedido item, List<OpcaoItemPedidoResponse> opcoes) {
        return new ItemPedidoResponse(
            item.getId(),
            item.getProduto().getId(),
            item.getProduto().getNome(),
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getSubtotal(),
            item.getObservacoes(),
            opcoes
        );
    }
}
