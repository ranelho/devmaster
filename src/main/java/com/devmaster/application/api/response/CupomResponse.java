package com.devmaster.application.api.response;

import com.devmaster.domain.Cupom;
import com.devmaster.domain.enums.TipoDesconto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CupomResponse(
    Long id,
    String codigo,
    String descricao,
    TipoDesconto tipoDesconto,
    BigDecimal valorDesconto,
    BigDecimal valorMinimoPedido,
    BigDecimal descontoMaximo,
    Integer limiteUso,
    Integer quantidadeUsada,
    LocalDateTime validoDe,
    LocalDateTime validoAte,
    Boolean ativo,
    Boolean valido,
    Boolean expirado,
    Boolean limiteAtingido,
    LocalDateTime criadoEm
) {
    public static CupomResponse from(Cupom cupom) {
        return new CupomResponse(
            cupom.getId(),
            cupom.getCodigo(),
            cupom.getDescricao(),
            cupom.getTipoDesconto(),
            cupom.getValorDesconto(),
            cupom.getValorMinimoPedido(),
            cupom.getDescontoMaximo(),
            cupom.getLimiteUso(),
            cupom.getQuantidadeUsada(),
            cupom.getValidoDe(),
            cupom.getValidoAte(),
            cupom.getAtivo(),
            cupom.isValido(),
            cupom.isExpirado(),
            cupom.atingiuLimiteUso(),
            cupom.getCriadoEm()
        );
    }
}
