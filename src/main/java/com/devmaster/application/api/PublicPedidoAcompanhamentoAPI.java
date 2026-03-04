package com.devmaster.application.api;

import com.devmaster.application.api.response.PedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Acompanhamento de Pedido", description = "Endpoint público para cliente acompanhar pedido")
@RequestMapping({"/public/v1/pedidos", "/public/v2/pedidos"})
public interface PublicPedidoAcompanhamentoAPI {
    
    @GetMapping("/acompanhar/{numeroPedido}")
    @Operation(summary = "Acompanhar pedido", description = "Cliente acompanha status do pedido pelo número")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    ResponseEntity<PedidoResponse> acompanharPedido(
        @Parameter(description = "Número do pedido", example = "123456789")
        @PathVariable String numeroPedido
    );
}
