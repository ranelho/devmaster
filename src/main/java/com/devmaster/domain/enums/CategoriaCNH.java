package com.devmaster.domain.enums;

/**
 * Enum que representa as categorias de CNH (Carteira Nacional de Habilitação).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public enum CategoriaCNH {
    
    A("Categoria A - Veículos motorizados de duas ou três rodas"),
    B("Categoria B - Veículos de quatro rodas até 3.500kg"),
    AB("Categoria AB - Combinação das categorias A e B"),
    C("Categoria C - Veículos de carga acima de 3.500kg"),
    D("Categoria D - Veículos de transporte de passageiros"),
    E("Categoria E - Combinação de veículos com reboque");
    
    private final String descricao;
    
    CategoriaCNH(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
