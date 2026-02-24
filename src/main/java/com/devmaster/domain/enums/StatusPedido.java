package com.devmaster.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusPedido {
    
    AGUARDANDO_CONFIRMACAO("Aguardando Confirmação"),
    PENDENTE("Pendente"),
    CONFIRMADO("Confirmado"),
    EM_PREPARO("Em Preparo"),
    PREPARANDO("Preparando"),
    PRONTO("Pronto"),
    EM_ENTREGA("Em Entrega"),
    DESPACHADO("Despachado"),
    ENTREGUE("Entregue"),
    CANCELADO("Cancelado");
    
    private final String descricao;
}
