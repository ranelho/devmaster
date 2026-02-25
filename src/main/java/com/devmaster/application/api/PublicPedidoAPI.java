package com.devmaster.application.api;

import com.devmaster.application.api.request.PedidoRequest;
import com.devmaster.application.api.response.PedidoResponse;
import com.devmaster.application.api.response.PedidoResumoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST pública para pedidos (sem autenticação).
 * Usada no fluxo de checkout do delivery.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Pedidos Público", description = "APIs públicas para pedidos (checkout - sem autenticação)")
@RequestMapping("/public/v1/pedidos")
public interface PublicPedidoAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Criar pedido",
        description = """
            Cria um novo pedido no sistema.
            Usado no fluxo de checkout do delivery.
            Endpoint público - não requer autenticação.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou cliente não encontrado")
    })
    PedidoResponse criarPedido(@Valid @RequestBody PedidoRequest request);
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar pedido por ID",
        description = "Retorna dados completos de um pedido. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    PedidoResponse buscarPedido(@Parameter(description = "ID do pedido") @PathVariable Long id);
    
    @GetMapping("/numero/{numero}")
    @Operation(
        summary = "Buscar pedido por número",
        description = "Retorna dados do pedido pelo número. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    PedidoResponse buscarPorNumero(
        @Parameter(description = "Número do pedido") @PathVariable String numero
    );
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(
        summary = "Listar pedidos do cliente",
        description = "Lista todos os pedidos de um cliente. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    List<PedidoResumoResponse> listarPedidosCliente(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId
    );
    
    @GetMapping("/cliente/telefone/{telefone}")
    @Operation(
        summary = "Listar pedidos por telefone",
        description = "Lista todos os pedidos de um cliente pelo telefone. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    List<PedidoResumoResponse> listarPedidosPorTelefone(
        @Parameter(description = "Telefone do cliente") @PathVariable String telefone
    );
}
