package com.devmaster.application.api;

import com.devmaster.application.api.response.DashboardMetricasResponse;
import com.devmaster.application.api.response.DashboardResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.api.response.ResumoStatusResponse;
import com.devmaster.application.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DashboardController implements DashboardAPI {
    
    private final DashboardService service;
    
    @Override
    public ResponseEntity<DashboardResponse> buscarDashboardCompleto(Long restauranteId, LocalDate data) {
        DashboardResponse response = service.buscarDashboardCompleto(restauranteId, data);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> buscarPedidosNovos(Long restauranteId) {
        List<PedidoResumoResponse> response = service.buscarPedidosNovos(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> buscarPedidosEmPreparo(Long restauranteId) {
        List<PedidoResumoResponse> response = service.buscarPedidosEmPreparo(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> buscarPedidosProntos(Long restauranteId) {
        List<PedidoResumoResponse> response = service.buscarPedidosProntos(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> buscarPedidosEmEntrega(Long restauranteId) {
        List<PedidoResumoResponse> response = service.buscarPedidosEmEntrega(restauranteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<String> gerarComandaHTML(Long pedidoId) {
        String html = service.gerarComandaHTML(pedidoId);
        return ResponseEntity.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(html);
    }
    
    @Override
    public ResponseEntity<byte[]> gerarComandaPDF(Long pedidoId) {
        byte[] pdf = service.gerarComandaPDF(pedidoId);
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header("Content-Disposition", "attachment; filename=comanda_" + pedidoId + ".pdf")
            .body(pdf);
    }
    
    @Override
    public ResponseEntity<DashboardMetricasResponse> buscarMetricas(Long restauranteId, LocalDate data) {
        DashboardMetricasResponse response = service.buscarMetricas(restauranteId, data);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<ResumoStatusResponse>> buscarResumoStatus(Long restauranteId) {
        List<ResumoStatusResponse> response = service.buscarResumoStatus(restauranteId);
        return ResponseEntity.ok(response);
    }
}
