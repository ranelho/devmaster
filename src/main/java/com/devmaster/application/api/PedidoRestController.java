package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.application.service.impl.PedidoApplicationService;
import com.devmaster.domain.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoRestController implements PedidoAPI {
    
    private final PedidoApplicationService pedidoService;
    
    @Override
    public ResponseEntity<PedidoResponse> criarPedido(PedidoRequest request) {
        PedidoResponse response = pedidoService.criarPedido(null, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<PedidoResponse> buscarPedido(Long pedidoId) {
        PedidoResponse response = pedidoService.buscarPedido(null, pedidoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<PedidoResponse> buscarPedidoPorNumero(String numeroPedido) {
        PedidoResponse response = pedidoService.buscarPedidoPorNumero(null, numeroPedido);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> listarPedidosRestaurante(Long restauranteId, StatusPedido status) {
        List<PedidoResumoResponse> response = pedidoService.listarPedidosRestaurante(null, restauranteId, status);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> listarPedidosCliente(Long clienteId, StatusPedido status) {
        List<PedidoResumoResponse> response = pedidoService.listarPedidosCliente(null, clienteId, status);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Page<PedidoResumoResponse>> listarPedidosRestauranteComPaginacao(
        Long restauranteId,
        StatusPedido status,
        Pageable pageable
    ) {
        Page<PedidoResumoResponse> response = pedidoService.listarPedidosRestauranteComPaginacao(null, restauranteId, status, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Page<PedidoResumoResponse>> listarPedidosClienteComPaginacao(Long clienteId, Pageable pageable) {
        Page<PedidoResumoResponse> response = pedidoService.listarPedidosClienteComPaginacao(null, clienteId, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<PedidoResponse> atualizarStatus(Long pedidoId, AtualizarStatusPedidoRequest request) {
        PedidoResponse response = pedidoService.atualizarStatus(null, pedidoId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> confirmarPedido(Long pedidoId) {
        pedidoService.confirmarPedido(null, pedidoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> iniciarPreparo(Long pedidoId) {
        pedidoService.iniciarPreparo(null, pedidoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> marcarComoPronto(Long pedidoId) {
        pedidoService.marcarComoPronto(null, pedidoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> despacharPedido(Long pedidoId, Long entregadorId) {
        pedidoService.despacharPedido(null, pedidoId, entregadorId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> entregarPedido(Long pedidoId) {
        pedidoService.entregarPedido(null, pedidoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> cancelarPedido(Long pedidoId, CancelarPedidoRequest request) {
        pedidoService.cancelarPedido(null, pedidoId, request);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> aprovarPagamento(Long pedidoId) {
        pedidoService.aprovarPagamento(null, pedidoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> recusarPagamento(Long pedidoId) {
        pedidoService.recusarPagamento(null, pedidoId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<List<HistoricoStatusPedidoResponse>> buscarHistorico(Long pedidoId) {
        List<HistoricoStatusPedidoResponse> response = pedidoService.buscarHistorico(null, pedidoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> buscarPedidosPorPeriodo(
        Long restauranteId,
        LocalDateTime inicio,
        LocalDateTime fim
    ) {
        List<PedidoResumoResponse> response = pedidoService.buscarPedidosPorPeriodo(null, restauranteId, inicio, fim);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> buscarPedidosAtivos(Long restauranteId) {
        List<PedidoResumoResponse> response = pedidoService.buscarPedidosAtivos(null, restauranteId);
        return ResponseEntity.ok(response);
    }
}
