package com.devmaster.domain.enums;

/**
 * Enum que representa os tipos de veículos disponíveis para entregadores.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public enum TipoVeiculo {
    
    BICICLETA("Bicicleta"),
    MOTO("Moto"),
    CARRO("Carro");
    
    private final String descricao;
    
    TipoVeiculo(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
