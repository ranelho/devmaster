package com.devmaster.application.api;

import com.devmaster.application.api.response.DashboardMetricasResponse;
import com.devmaster.application.api.response.DashboardResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.api.response.ResumoStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Dashboard", description = "Endpoints para dashboard de gerenciamento de pedidos")
@RequestMapping("/v1/dashboard")
public interface DashboardAPI {
    
    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Dashboard completo", description = "Retorna todos os dados do dashboard em uma única requisição")
    DashboardResponse buscarDashboardCompleto(
        @PathVariable Long restauranteId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    );
    
    @GetMapping("/restaurante/{restauranteId}/pedidos-novos")
    @Operation(summary = "Pedidos novos", description = "Lista pedidos aguardando confirmação em tempo real")
    List<PedidoResumoResponse> buscarPedidosNovos(@PathVariable Long restauranteId);
    
    @GetMapping("/restaurante/{restauranteId}/pedidos-em-preparo")
    @Operation(summary = "Pedidos em preparo", description = "Lista pedidos sendo preparados na cozinha")
    List<PedidoResumoResponse> buscarPedidosEmPreparo(@PathVariable Long restauranteId);
    
    @GetMapping("/restaurante/{restauranteId}/pedidos-prontos")
    @Operation(summary = "Pedidos prontos", description = "Lista pedidos prontos para despacho")
    List<PedidoResumoResponse> buscarPedidosProntos(@PathVariable Long restauranteId);
    
    @GetMapping("/restaurante/{restauranteId}/pedidos-em-entrega")
    @Operation(summary = "Pedidos em entrega", description = "Lista pedidos despachados para entrega")
    List<PedidoResumoResponse> buscarPedidosEmEntrega(@PathVariable Long restauranteId);
    
    @GetMapping(value = "/pedido/{pedidoId}/comanda", produces = MediaType.TEXT_HTML_VALUE)
    @Operation(summary = "Gerar comanda HTML", description = "Gera comanda em HTML para impressão na cozinha")
    String gerarComandaHTML(@PathVariable Long pedidoId);
    
    @GetMapping(value = "/pedido/{pedidoId}/comanda/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Gerar comanda PDF", description = "Gera comanda em PDF para impressão")
    byte[] gerarComandaPDF(@PathVariable Long pedidoId);
    
    @GetMapping("/restaurante/{restauranteId}/metricas")
    @Operation(summary = "Métricas do dia", description = "Retorna métricas e KPIs do restaurante")
    DashboardMetricasResponse buscarMetricas(
        @PathVariable Long restauranteId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    );
    
    @GetMapping("/restaurante/{restauranteId}/resumo-status")
    @Operation(summary = "Resumo por status", description = "Contagem de pedidos agrupados por status")
    List<ResumoStatusResponse> buscarResumoStatus(@PathVariable Long restauranteId);
}
