package com.devmaster.application.api;

import com.devmaster.application.api.response.PedidoResponse;
import com.devmaster.application.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicPedidoAcompanhamentoController implements PublicPedidoAcompanhamentoAPI {
    
    private final PedidoService pedidoService;
    
    @Override
    public PedidoResponse acompanharPedido(String numeroPedido) {
        return pedidoService.buscarPedidoPorNumero(null, numeroPedido);
    }
}
