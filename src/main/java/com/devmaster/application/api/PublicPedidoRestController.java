package com.devmaster.application.api;

import com.devmaster.application.api.request.PedidoRequest;
import com.devmaster.application.api.response.PedidoResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Implementação da API REST pública para pedidos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class PublicPedidoRestController implements PublicPedidoAPI {
    
    private final PedidoService pedidoService;
    
    @Override
    public PedidoResponse criarPedido(PedidoRequest request) {
        return pedidoService.criarPedido(null, request);
    }
    
    @Override
    public PedidoResponse buscarPedido(Long id) {
        return pedidoService.buscarPedido(null, id);
    }
    
    @Override
    public PedidoResponse buscarPorNumero(String numero) {
        return pedidoService.buscarPedidoPorNumero(null, numero);
    }
    
    @Override
    public List<PedidoResumoResponse> listarPedidosCliente(Long clienteId) {
        return pedidoService.listarPedidosCliente(null, clienteId, null);
    }

    @Override
    public List<PedidoResumoResponse> listarPedidosPorTelefone(String telefone) {
        return pedidoService.listarPedidosPorTelefone(null, telefone);
    }
}
