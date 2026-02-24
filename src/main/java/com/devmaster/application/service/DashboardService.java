package com.devmaster.application.service;

import com.devmaster.application.api.response.DashboardMetricasResponse;
import com.devmaster.application.api.response.DashboardResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.api.response.ResumoStatusResponse;

import java.time.LocalDate;
import java.util.List;

public interface DashboardService {
    
    DashboardResponse buscarDashboardCompleto(Long restauranteId, LocalDate data);
    
    List<PedidoResumoResponse> buscarPedidosNovos(Long restauranteId);
    
    List<PedidoResumoResponse> buscarPedidosEmPreparo(Long restauranteId);
    
    List<PedidoResumoResponse> buscarPedidosProntos(Long restauranteId);
    
    List<PedidoResumoResponse> buscarPedidosEmEntrega(Long restauranteId);
    
    String gerarComandaHTML(Long pedidoId);
    
    byte[] gerarComandaPDF(Long pedidoId);
    
    DashboardMetricasResponse buscarMetricas(Long restauranteId, LocalDate data);
    
    List<ResumoStatusResponse> buscarResumoStatus(Long restauranteId);
}
