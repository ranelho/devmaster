package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.PedidoService;
import com.devmaster.domain.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementação da API de Pedidos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class PedidoRestController implements PedidoAPI {
    
    private final PedidoService pedidoService;
    
    @Override
    public PedidoResponse criarPedido(UUID usuarioId, PedidoRequest request) {
        return pedidoService.criarPedido(usuarioId, request);
    }
    
    @Override
    public PedidoResponse buscarPedido(UUID usuarioId, Long pedidoId) {
        return pedidoService.buscarPedido(usuarioId, pedidoId);
    }
    
    @Override
    public PedidoResponse buscarPedidoPorNumero(UUID usuarioId, String numeroPedido) {
        return pedidoService.buscarPedidoPorNumero(usuarioId, numeroPedido);
    }
    
    @Override
    public List<PedidoResumoResponse> listarPedidosRestaurante(UUID usuarioId, Long restauranteId, StatusPedido status) {
        return pedidoService.listarPedidosRestaurante(usuarioId, restauranteId, status);
    }
    
    @Override
    public List<PedidoResumoResponse> listarPedidosCliente(UUID usuarioId, Long clienteId, StatusPedido status) {
        return pedidoService.listarPedidosCliente(usuarioId, clienteId, status);
    }
    
    @Override
    public Page<PedidoResumoResponse> listarPedidosRestauranteComPaginacao(
        UUID usuarioId,
        Long restauranteId,
        StatusPedido status,
        Pageable pageable
    ) {
        return pedidoService.listarPedidosRestauranteComPaginacao(usuarioId, restauranteId, status, pageable);
    }
    
    @Override
    public Page<PedidoResumoResponse> listarPedidosClienteComPaginacao(UUID usuarioId, Long clienteId, Pageable pageable) {
        return pedidoService.listarPedidosClienteComPaginacao(usuarioId, clienteId, pageable);
    }
    
    @Override
    public PedidoResponse atualizarStatus(UUID usuarioId, Long pedidoId, AtualizarStatusPedidoRequest request) {
        return pedidoService.atualizarStatus(usuarioId, pedidoId, request);
    }
    
    @Override
    public void confirmarPedido(UUID usuarioId, Long pedidoId) {
        pedidoService.confirmarPedido(usuarioId, pedidoId);
    }
    
    @Override
    public void iniciarPreparo(UUID usuarioId, Long pedidoId) {
        pedidoService.iniciarPreparo(usuarioId, pedidoId);
    }
    
    @Override
    public void marcarComoPronto(UUID usuarioId, Long pedidoId) {
        pedidoService.marcarComoPronto(usuarioId, pedidoId);
    }
    
    @Override
    public void despacharPedido(UUID usuarioId, Long pedidoId) {
        pedidoService.despacharPedido(usuarioId, pedidoId);
    }
    
    @Override
    public void entregarPedido(UUID usuarioId, Long pedidoId) {
        pedidoService.entregarPedido(usuarioId, pedidoId);
    }
    
    @Override
    public void cancelarPedido(UUID usuarioId, Long pedidoId, CancelarPedidoRequest request) {
        pedidoService.cancelarPedido(usuarioId, pedidoId, request);
    }
    
    @Override
    public void aprovarPagamento(UUID usuarioId, Long pedidoId) {
        pedidoService.aprovarPagamento(usuarioId, pedidoId);
    }
    
    @Override
    public void recusarPagamento(UUID usuarioId, Long pedidoId) {
        pedidoService.recusarPagamento(usuarioId, pedidoId);
    }
    
    @Override
    public List<HistoricoStatusPedidoResponse> buscarHistorico(UUID usuarioId, Long pedidoId) {
        return pedidoService.buscarHistorico(usuarioId, pedidoId);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosPorPeriodo(
        UUID usuarioId,
        Long restauranteId,
        LocalDateTime inicio,
        LocalDateTime fim
    ) {
        return pedidoService.buscarPedidosPorPeriodo(usuarioId, restauranteId, inicio, fim);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosAtivos(UUID usuarioId, Long restauranteId) {
        return pedidoService.buscarPedidosAtivos(usuarioId, restauranteId);
    }
}
