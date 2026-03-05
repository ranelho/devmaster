package com.devmaster.application.api.response;

import java.math.BigDecimal;

public interface ProdutoMaisVendidoDTO {
    String getProdutoNome();
    Long getQuantidadeVendida();
    BigDecimal getValorTotal();
}
