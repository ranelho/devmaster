package com.devmaster.application.api.response;

import java.math.BigDecimal;

public record ValidacaoCupomResponse(
    Boolean valido,
    String mensagem,
    BigDecimal descontoCalculado,
    BigDecimal valorFinal,
    CupomResponse cupom
) {
    public static ValidacaoCupomResponse sucesso(
        BigDecimal descontoCalculado,
        BigDecimal valorFinal,
        CupomResponse cupom
    ) {
        return new ValidacaoCupomResponse(
            true,
            "Cupom válido e aplicado com sucesso",
            descontoCalculado,
            valorFinal,
            cupom
        );
    }
    
    public static ValidacaoCupomResponse erro(String mensagem) {
        return new ValidacaoCupomResponse(
            false,
            mensagem,
            null,
            null,
            null
        );
    }
}
