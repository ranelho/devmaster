package com.devmaster.domain.enums;

/**
 * Enum para tipo de desconto do cupom.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public enum TipoDesconto {
    PERCENTUAL("Percentual"),
    VALOR_FIXO("Valor Fixo");
    
    private final String descricao;
    
    TipoDesconto(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
