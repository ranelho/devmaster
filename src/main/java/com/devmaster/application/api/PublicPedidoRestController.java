package com.devmaster.application.api;

import com.devmaster.application.api.request.PedidoRequest;
import com.devmaster.application.api.response.PedidoResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import com.devmaster.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<PedidoResponse> criarPedido(PedidoRequest request) {
        PedidoResponse response = pedidoService.criarPedido(null, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<PedidoResponse> buscarPedido(Long id) {
        PedidoResponse response = pedidoService.buscarPedido(null, id);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<PedidoResponse> buscarPorNumero(String numero) {
        PedidoResponse response = pedidoService.buscarPedidoPorNumero(null, numero);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<PedidoResumoResponse>> listarPedidosCliente(Long clienteId) {
        List<PedidoResumoResponse> response = pedidoService.listarPedidosCliente(null, clienteId, null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<PedidoResumoResponse>> listarPedidosPorTelefone(String telefone) {
        List<PedidoResumoResponse> response = pedidoService.listarPedidosPorTelefone(null, telefone);
        return ResponseEntity.ok(response);
    }
}
