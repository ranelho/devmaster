package com.devmaster.domain.enums;

/**
 * Enum para status do pagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public enum StatusPagamento {
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    APROVADO("Aprovado"),
    RECUSADO("Recusado"),
    CANCELADO("Cancelado"),
    REEMBOLSADO("Reembolsado");
    
    private final String descricao;
    
    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
