package com.devmaster.application.api;

import com.devmaster.application.api.response.PedidoResponse;
import com.devmaster.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicPedidoAcompanhamentoController implements PublicPedidoAcompanhamentoAPI {
    
    private final PedidoService pedidoService;
    
    @Override
    public ResponseEntity<PedidoResponse> acompanharPedido(String numeroPedido) {
        PedidoResponse response = pedidoService.buscarPedidoPorNumero(null, numeroPedido);
        return ResponseEntity.ok(response);
    }
}
