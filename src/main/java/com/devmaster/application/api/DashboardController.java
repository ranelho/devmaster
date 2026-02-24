package com.devmaster.application.api;

import com.devmaster.application.api.response.DashboardMetricasResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.api.response.ResumoStatusResponse;
import com.devmaster.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DashboardController implements DashboardAPI {
    
    private final DashboardService service;
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosNovos(Long restauranteId) {
        return service.buscarPedidosNovos(restauranteId);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosEmPreparo(Long restauranteId) {
        return service.buscarPedidosEmPreparo(restauranteId);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosProntos(Long restauranteId) {
        return service.buscarPedidosProntos(restauranteId);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosEmEntrega(Long restauranteId) {
        return service.buscarPedidosEmEntrega(restauranteId);
    }
    
    @Override
    public String gerarComandaHTML(Long pedidoId) {
        return service.gerarComandaHTML(pedidoId);
    }
    
    @Override
    public byte[] gerarComandaPDF(Long pedidoId) {
        return service.gerarComandaPDF(pedidoId);
    }
    
    @Override
    public DashboardMetricasResponse buscarMetricas(Long restauranteId, LocalDate data) {
        return service.buscarMetricas(restauranteId, data);
    }
    
    @Override
    public List<ResumoStatusResponse> buscarResumoStatus(Long restauranteId) {
        return service.buscarResumoStatus(restauranteId);
    }
}
