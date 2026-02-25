package com.devmaster.application.service;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.domain.enums.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Pedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface PedidoService {
    
    PedidoResponse criarPedido(UUID usuarioId, PedidoRequest request);
    PedidoResponse buscarPedido(UUID usuarioId, Long pedidoId);
    PedidoResponse buscarPedidoPorNumero(UUID usuarioId, String numeroPedido);
    List<PedidoResumoResponse> listarPedidosRestaurante(UUID usuarioId, Long restauranteId, StatusPedido status);
    List<PedidoResumoResponse> listarPedidosCliente(UUID usuarioId, Long clienteId, StatusPedido status);
    List<PedidoResumoResponse> listarPedidosPorTelefone(UUID usuarioId, String telefone);
    Page<PedidoResumoResponse> listarPedidosRestauranteComPaginacao(UUID usuarioId, Long restauranteId, StatusPedido status, Pageable pageable);
    Page<PedidoResumoResponse> listarPedidosClienteComPaginacao(UUID usuarioId, Long clienteId, Pageable pageable);
    PedidoResponse atualizarStatus(UUID usuarioId, Long pedidoId, AtualizarStatusPedidoRequest request);
    void confirmarPedido(UUID usuarioId, Long pedidoId);
    void iniciarPreparo(UUID usuarioId, Long pedidoId);
    void marcarComoPronto(UUID usuarioId, Long pedidoId);
    void despacharPedido(UUID usuarioId, Long pedidoId);
    void entregarPedido(UUID usuarioId, Long pedidoId);
    void cancelarPedido(UUID usuarioId, Long pedidoId, CancelarPedidoRequest request);
    void aprovarPagamento(UUID usuarioId, Long pedidoId);
    void recusarPagamento(UUID usuarioId, Long pedidoId);
    List<HistoricoStatusPedidoResponse> buscarHistorico(UUID usuarioId, Long pedidoId);
    List<PedidoResumoResponse> buscarPedidosPorPeriodo(UUID usuarioId, Long restauranteId, LocalDateTime inicio, LocalDateTime fim);
    List<PedidoResumoResponse> buscarPedidosAtivos(UUID usuarioId, Long restauranteId);
}
