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

@RestController
@RequiredArgsConstructor
public class PedidoRestController implements PedidoAPI {
    
    private final PedidoService pedidoService;
    
    @Override
    public PedidoResponse criarPedido(PedidoRequest request) {
        return pedidoService.criarPedido(null, request);
    }
    
    @Override
    public PedidoResponse buscarPedido(Long pedidoId) {
        return pedidoService.buscarPedido(null, pedidoId);
    }
    
    @Override
    public PedidoResponse buscarPedidoPorNumero(String numeroPedido) {
        return pedidoService.buscarPedidoPorNumero(null, numeroPedido);
    }
    
    @Override
    public List<PedidoResumoResponse> listarPedidosRestaurante(Long restauranteId, StatusPedido status) {
        return pedidoService.listarPedidosRestaurante(null, restauranteId, status);
    }
    
    @Override
    public List<PedidoResumoResponse> listarPedidosCliente(Long clienteId, StatusPedido status) {
        return pedidoService.listarPedidosCliente(null, clienteId, status);
    }
    
    @Override
    public Page<PedidoResumoResponse> listarPedidosRestauranteComPaginacao(
        Long restauranteId,
        StatusPedido status,
        Pageable pageable
    ) {
        return pedidoService.listarPedidosRestauranteComPaginacao(null, restauranteId, status, pageable);
    }
    
    @Override
    public Page<PedidoResumoResponse> listarPedidosClienteComPaginacao(Long clienteId, Pageable pageable) {
        return pedidoService.listarPedidosClienteComPaginacao(null, clienteId, pageable);
    }
    
    @Override
    public PedidoResponse atualizarStatus(Long pedidoId, AtualizarStatusPedidoRequest request) {
        return pedidoService.atualizarStatus(null, pedidoId, request);
    }
    
    @Override
    public void confirmarPedido(Long pedidoId) {
        pedidoService.confirmarPedido(null, pedidoId);
    }
    
    @Override
    public void iniciarPreparo(Long pedidoId) {
        pedidoService.iniciarPreparo(null, pedidoId);
    }
    
    @Override
    public void marcarComoPronto(Long pedidoId) {
        pedidoService.marcarComoPronto(null, pedidoId);
    }
    
    @Override
    public void despacharPedido(Long pedidoId) {
        pedidoService.despacharPedido(null, pedidoId);
    }
    
    @Override
    public void entregarPedido(Long pedidoId) {
        pedidoService.entregarPedido(null, pedidoId);
    }
    
    @Override
    public void cancelarPedido(Long pedidoId, CancelarPedidoRequest request) {
        pedidoService.cancelarPedido(null, pedidoId, request);
    }
    
    @Override
    public void aprovarPagamento(Long pedidoId) {
        pedidoService.aprovarPagamento(null, pedidoId);
    }
    
    @Override
    public void recusarPagamento(Long pedidoId) {
        pedidoService.recusarPagamento(null, pedidoId);
    }
    
    @Override
    public List<HistoricoStatusPedidoResponse> buscarHistorico(Long pedidoId) {
        return pedidoService.buscarHistorico(null, pedidoId);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosPorPeriodo(
        Long restauranteId,
        LocalDateTime inicio,
        LocalDateTime fim
    ) {
        return pedidoService.buscarPedidosPorPeriodo(null, restauranteId, inicio, fim);
    }
    
    @Override
    public List<PedidoResumoResponse> buscarPedidosAtivos(Long restauranteId) {
        return pedidoService.buscarPedidosAtivos(null, restauranteId);
    }
}
