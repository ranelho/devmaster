package com.devmaster.application.api.response;

import com.devmaster.domain.OpcaoItemPedido;

import java.math.BigDecimal;

public record OpcaoItemPedidoResponse(
    Long id,
    Long grupoOpcaoId,
    String grupoOpcaoNome,
    Long opcaoId,
    String opcaoNome,
    BigDecimal precoAdicional
) {
    public static OpcaoItemPedidoResponse from(OpcaoItemPedido opcaoItem) {
        return new OpcaoItemPedidoResponse(
            opcaoItem.getId(),
            opcaoItem.getGrupoOpcao().getId(),
            opcaoItem.getGrupoOpcao().getNome(),
            opcaoItem.getOpcao().getId(),
            opcaoItem.getOpcao().getNome(),
            opcaoItem.getPrecoAdicional()
        );
    }
}
