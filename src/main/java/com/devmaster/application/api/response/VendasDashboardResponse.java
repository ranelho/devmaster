package com.devmaster.application.api.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record VendasDashboardResponse(
    BigDecimal totalVendas,
    Long quantidadePedidos,
    BigDecimal ticketMedio,
    List<VendaDiariaDTO> vendasPorDia
) {}
