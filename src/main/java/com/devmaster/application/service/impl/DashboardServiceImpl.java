package com.devmaster.application.service.impl;

import com.devmaster.application.api.response.DashboardMetricasResponse;
import com.devmaster.application.api.response.DashboardResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    
    private static final DateTimeFormatter FORMATTER_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final PedidoRepository pedidoRepository;
    
    @Override
    @Transactional(readOnly = true)
    public DashboardResponse buscarDashboardCompleto(Long restauranteId, LocalDate data) {
        return DashboardResponse.builder()
            .metricas(buscarMetricas(restauranteId, data))
            .resumo(buscarResumoStatus(restauranteId))
            .novos(buscarPedidosNovos(restauranteId))
            .preparo(buscarPedidosEmPreparo(restauranteId))
            .prontos(buscarPedidosProntos(restauranteId))
            .entrega(buscarPedidosEmEntrega(restauranteId))
            .build();
    }
    
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
        List<Pedido> confirmados = pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.CONFIRMADO);
        List<Pedido> emPreparo = pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.EM_PREPARO);
        List<Pedido> preparando = pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.PREPARANDO);
        
        return Stream.of(confirmados, emPreparo, preparando)
            .flatMap(List::stream)
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
        List<Pedido> despachados = pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.DESPACHADO);
        List<Pedido> emEntrega = pedidoRepository.findByRestauranteIdAndStatusOrderByCriadoEmDesc(restauranteId, StatusPedido.EM_ENTREGA);
        
        return Stream.of(despachados, emEntrega)
            .flatMap(List::stream)
            .map(this::toResumoResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public String gerarComandaHTML(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> APIException.build(HttpStatus.BAD_REQUEST, "Pedido não encontrado"));
        
        var endereco = pedido.getEnderecoEntrega();
        var restaurante = pedido.getRestaurante();
        
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Pedido #%s</title>
                <style>
                    body { font-family: Arial; padding: 20px; max-width: 800px; margin: 0 auto; }
                    .header { text-align: center; border-bottom: 2px solid #333; padding-bottom: 10px; margin-bottom: 20px; }
                    .section { margin: 15px 0; padding: 10px; background: #f5f5f5; border-radius: 5px; }
                    .section h3 { margin: 0 0 10px 0; color: #333; }
                    .info-line { margin: 5px 0; }
                    .label { font-weight: bold; }
                    .total { font-size: 20px; font-weight: bold; color: #2c5; margin-top: 20px; text-align: right; }
                    @media print { body { padding: 0; } }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>%s</h1>
                    <h2>Pedido #%s</h2>
                    <p>%s</p>
                </div>
                
                <div class="section">
                    <h3>📋 Dados do Pedido</h3>
                    <div class="info-line"><span class="label">Número:</span> %s</div>
                    <div class="info-line"><span class="label">Data/Hora:</span> %s</div>
                    <div class="info-line"><span class="label">Status:</span> %s</div>
                    <div class="info-line"><span class="label">Previsão Entrega:</span> %s</div>
                </div>
                
                <div class="section">
                    <h3>👤 Cliente</h3>
                    <div class="info-line"><span class="label">Nome:</span> %s</div>
                    <div class="info-line"><span class="label">Telefone:</span> %s</div>
                </div>
                
                <div class="section">
                    <h3>📍 Endereço de Entrega</h3>
                    <div class="info-line">%s, %s</div>
                    <div class="info-line">%s - %s</div>
                    <div class="info-line">%s - %s</div>
                    <div class="info-line"><span class="label">Complemento:</span> %s</div>
                    <div class="info-line"><span class="label">Referência:</span> %s</div>
                </div>
                
                <div class="section">
                    <h3>💳 Pagamento</h3>
                    <div class="info-line"><span class="label">Forma:</span> %s</div>
                    <div class="info-line"><span class="label">Status:</span> %s</div>
                    %s
                </div>
                
                <div class="section">
                    <h3>💰 Valores</h3>
                    <div class="info-line"><span class="label">Subtotal:</span> R$ %.2f</div>
                    <div class="info-line"><span class="label">Taxa de Entrega:</span> R$ %.2f</div>
                    <div class="info-line"><span class="label">Desconto:</span> R$ %.2f</div>
                    <div class="total">TOTAL: R$ %.2f</div>
                </div>
                
                %s
                
                <div style="margin-top: 30px; text-align: center; color: #666; font-size: 12px;">
                    Impresso em: %s
                </div>
            </body>
            </html>
            """.formatted(
                pedido.getNumeroPedido(),
                restaurante.getNome(),
                pedido.getNumeroPedido(),
                restaurante.getTelefone(),
                pedido.getNumeroPedido(),
                pedido.getDataPedido().format(FORMATTER_BR),
                pedido.getStatus(),
                pedido.getPrevisaoEntrega().format(FORMATTER_BR),
                pedido.getClienteNome(),
                pedido.getCliente().getTelefone(),
                endereco.getLogradouro(), endereco.getNumero(),
                endereco.getBairro(), endereco.getCidade(),
                endereco.getEstado(), endereco.getCep(),
                endereco.getComplemento() != null ? endereco.getComplemento() : "-",
                "-",
                pedido.getTipoPagamento().getNome(),
                pedido.getStatusPagamento(),
                pedido.getValorTroco() != null ? 
                    "<div class=\"info-line\"><span class=\"label\">Troco para:</span> R$ " + pedido.getValorTroco() + "</div>" : "",
                pedido.getSubtotal(),
                pedido.getTaxaEntrega(),
                pedido.getDesconto(),
                pedido.getValorTotal(),
                pedido.getObservacoes() != null ? 
                    "<div class=\"section\"><h3>📝 Observações</h3><p>" + pedido.getObservacoes() + "</p></div>" : "",
                LocalDateTime.now().format(FORMATTER_BR)
            );
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
        return PedidoResumoResponse.from(pedido);
    }
}
