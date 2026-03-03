package com.devmaster.domain.enums;

import lombok.Getter;

@Getter
public enum TipoIntervaloDesconto {
    DIAS("Intervalo em dias"),
    HORAS("Intervalo em horas");
    
    private final String descricao;
    
    TipoIntervaloDesconto(String descricao) {
        this.descricao = descricao;
    }
}
