package com.devmaster.domain.enums;

/**
 * Enum para status do pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public enum StatusPedido {
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    PREPARANDO("Preparando"),
    PRONTO("Pronto"),
    DESPACHADO("Despachado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");
    
    private final String descricao;
    
    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
