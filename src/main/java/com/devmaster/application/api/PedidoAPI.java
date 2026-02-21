package com.devmaster.application.api;

import com.devmaster.application.api.request.*;
import com.devmaster.application.api.response.*;
import com.devmaster.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * API para gerenciamento de Pedidos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos e itens")
@RequestMapping("/v1/pedidos")
public interface PedidoAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido com itens e opções")
    PedidoResponse criarPedido(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @Valid @RequestBody PedidoRequest request
    );
    
    @GetMapping("/{pedidoId}")
    @Operation(summary = "Buscar pedido", description = "Busca um pedido por ID com todos os detalhes")
    PedidoResponse buscarPedido(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar por número", description = "Busca um pedido pelo número")
    PedidoResponse buscarPedidoPorNumero(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable String numeroPedido
    );
    
    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar pedidos do restaurante", description = "Lista pedidos de um restaurante com filtro opcional de status")
    List<PedidoResumoResponse> listarPedidosRestaurante(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @RequestParam(required = false) StatusPedido status
    );
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos do cliente", description = "Lista pedidos de um cliente com filtro opcional de status")
    List<PedidoResumoResponse> listarPedidosCliente(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long clienteId,
        @RequestParam(required = false) StatusPedido status
    );
    
    @GetMapping("/restaurante/{restauranteId}/paginado")
    @Operation(summary = "Listar pedidos paginado", description = "Lista pedidos do restaurante com paginação")
    Page<PedidoResumoResponse> listarPedidosRestauranteComPaginacao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @RequestParam(required = false) StatusPedido status,
        Pageable pageable
    );
    
    @GetMapping("/cliente/{clienteId}/paginado")
    @Operation(summary = "Listar pedidos do cliente paginado", description = "Lista pedidos do cliente com paginação")
    Page<PedidoResumoResponse> listarPedidosClienteComPaginacao(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long clienteId,
        Pageable pageable
    );
    
    @PutMapping("/{pedidoId}/status")
    @Operation(summary = "Atualizar status", description = "Atualiza o status do pedido")
    PedidoResponse atualizarStatus(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId,
        @Valid @RequestBody AtualizarStatusPedidoRequest request
    );
    
    @PatchMapping("/{pedidoId}/confirmar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Confirmar pedido", description = "Confirma o pedido")
    void confirmarPedido(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @PatchMapping("/{pedidoId}/iniciar-preparo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Iniciar preparo", description = "Inicia o preparo do pedido")
    void iniciarPreparo(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @PatchMapping("/{pedidoId}/marcar-pronto")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Marcar como pronto", description = "Marca o pedido como pronto")
    void marcarComoPronto(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @PatchMapping("/{pedidoId}/despachar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Despachar pedido", description = "Despacha o pedido para entrega")
    void despacharPedido(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @PatchMapping("/{pedidoId}/entregar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Entregar pedido", description = "Marca o pedido como entregue")
    void entregarPedido(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @PatchMapping("/{pedidoId}/cancelar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido")
    void cancelarPedido(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId,
        @Valid @RequestBody CancelarPedidoRequest request
    );
    
    @PatchMapping("/{pedidoId}/aprovar-pagamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Aprovar pagamento", description = "Aprova o pagamento do pedido")
    void aprovarPagamento(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @PatchMapping("/{pedidoId}/recusar-pagamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Recusar pagamento", description = "Recusa o pagamento do pedido")
    void recusarPagamento(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @GetMapping("/{pedidoId}/historico")
    @Operation(summary = "Buscar histórico", description = "Busca o histórico de status do pedido")
    List<HistoricoStatusPedidoResponse> buscarHistorico(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long pedidoId
    );
    
    @GetMapping("/restaurante/{restauranteId}/periodo")
    @Operation(summary = "Buscar por período", description = "Busca pedidos do restaurante em um período")
    List<PedidoResumoResponse> buscarPedidosPorPeriodo(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    );
    
    @GetMapping("/restaurante/{restauranteId}/ativos")
    @Operation(summary = "Buscar pedidos ativos", description = "Busca pedidos em andamento do restaurante")
    List<PedidoResumoResponse> buscarPedidosAtivos(
        @RequestHeader("X-User-Id") UUID usuarioId,
        @PathVariable Long restauranteId
    );
}
