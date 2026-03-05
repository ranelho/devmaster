package com.devmaster.application.api.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface VendaDiariaDTO {
    LocalDate getData();
    BigDecimal getTotalVendas();
    Long getQuantidadePedidos();
}
