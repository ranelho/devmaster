package com.devmaster.application.api;

import com.devmaster.application.api.request.PedidoRequest;
import com.devmaster.application.api.response.PedidoResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementação da API REST pública para pedidos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicPedidoRestController implements PublicPedidoAPI {
    
    private final PedidoService pedidoService;
    
    @Override
    public PedidoResponse criarPedido(PedidoRequest request) {
        log.info("POST /public/v1/pedidos - Criar pedido para restaurante ID: {}, cliente ID: {}", 
            request.restauranteId(), request.clienteId());
        return pedidoService.criarPedido(null, request);
    }
    
    @Override
    public PedidoResponse buscarPedido(Long id) {
        log.info("GET /public/v1/pedidos/{} - Buscar pedido", id);
        return pedidoService.buscarPedido(null, id);
    }
    
    @Override
    public PedidoResponse buscarPorNumero(String numero) {
        log.info("GET /public/v1/pedidos/numero/{} - Buscar pedido por número", numero);
        return pedidoService.buscarPedidoPorNumero(null, numero);
    }
    
    @Override
    public List<PedidoResumoResponse> listarPedidosCliente(Long clienteId) {
        log.info("GET /public/v1/pedidos/cliente/{} - Listar pedidos do cliente", clienteId);
        return pedidoService.listarPedidosCliente(null, clienteId, null);
    }
}
