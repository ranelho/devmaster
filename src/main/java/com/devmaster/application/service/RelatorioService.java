package com.devmaster.application.service;

import com.devmaster.application.api.response.VendasDashboardResponse;

import java.time.LocalDate;

public interface RelatorioService {
    VendasDashboardResponse buscarDadosDashboard(Long restauranteId, LocalDate inicio, LocalDate fim);
    byte[] gerarRelatorioVendasExcel(Long restauranteId, LocalDate inicio, LocalDate fim);
    byte[] gerarRelatorioProdutosMaisVendidosExcel(Long restauranteId, LocalDate inicio, LocalDate fim);
}
