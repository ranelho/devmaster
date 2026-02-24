package com.devmaster.application.service.impl;

import com.devmaster.application.api.response.DashboardMetricasResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.api.response.ResumoStatusResponse;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.PedidoRepository;
import com.devmaster.application.service.DashboardService;
import com.devmaster.domain.Pedido;
import com.devmaster.domain.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    
    private final PedidoRepository pedidoRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> buscarPedidosNovos(Long restauranteId) {
        return pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.AGUARDANDO_CONFIRMACAO)
            .stream()
            .map(this::toResumoResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> buscarPedidosEmPreparo(Long restauranteId) {
        return pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.EM_PREPARO)
            .stream()
            .map(this::toResumoResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> buscarPedidosProntos(Long restauranteId) {
        return pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.PRONTO)
            .stream()
            .map(this::toResumoResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumoResponse> buscarPedidosEmEntrega(Long restauranteId) {
        return pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.EM_ENTREGA)
            .stream()
            .map(this::toResumoResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public String gerarComandaHTML(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST, "Pedido não encontrado"));
        
        return """
            <!DOCTYPE html>
            <html>
            <head><title>Comanda #%d</title></head>
            <body>
                <h1>Pedido #%d</h1>
                <p>Cliente: %s</p>
                <p>Total: R$ %.2f</p>
                <p>Status: %s</p>
            </body>
            </html>
            """.formatted(pedido.getId(), pedido.getId(), 
                pedido.getClienteNome(), pedido.getValorTotal(), pedido.getStatus());
    }
    
    @Override
    @Transactional(readOnly = true)
    public byte[] gerarComandaPDF(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST,"Pedido não encontrado"));
        
        String html = """
            <!DOCTYPE html><html><head><title>Comanda #%d</title></head>
            <body><h1>Pedido #%d</h1><p>Cliente: %s</p><p>Total: R$ %.2f</p><p>Status: %s</p></body></html>
            """.formatted(pedido.getId(), pedido.getId(), pedido.getClienteNome(), 
                pedido.getValorTotal(), pedido.getStatus());
        
        return html.getBytes();
    }
    
    @Override
    @Transactional(readOnly = true)
    public DashboardMetricasResponse buscarMetricas(Long restauranteId, LocalDate data) {
        LocalDate dataConsulta = data != null ? data : LocalDate.now();
        LocalDateTime inicio = dataConsulta.atStartOfDay();
        LocalDateTime fim = dataConsulta.plusDays(1).atStartOfDay();
        
        List<Pedido> pedidos = pedidoRepository.findByRestauranteIdAndCriadoEmBetween(restauranteId, inicio, fim);
        
        BigDecimal faturamento = pedidos.stream()
            .map(Pedido::getValorTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return DashboardMetricasResponse.builder()
            .totalPedidos(pedidos.size())
            .faturamentoDia(faturamento)
            .ticketMedio(pedidos.isEmpty() ? BigDecimal.ZERO : 
                faturamento.divide(BigDecimal.valueOf(pedidos.size()), 2, RoundingMode.HALF_UP))
            .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ResumoStatusResponse> buscarResumoStatus(Long restauranteId) {
        List<Pedido> pedidos = pedidoRepository.findByRestauranteIdOrderByCriadoEmDesc(restauranteId);
        
        return pedidos.stream()
            .collect(Collectors.groupingBy(
                Pedido::getStatus,
                Collectors.counting()
            ))
            .entrySet()
            .stream()
            .map(entry -> new ResumoStatusResponse(entry.getKey(), entry.getValue()))
            .toList();
    }
    
    private PedidoResumoResponse toResumoResponse(Pedido pedido) {
        return PedidoResumoResponse.builder()
            .id(pedido.getId())
            .clienteNome(pedido.getClienteNome())
            .valorTotal(pedido.getValorTotal())
            .status(pedido.getStatus())
            .dataPedido(pedido.getDataPedido())
            .build();
    }
}
