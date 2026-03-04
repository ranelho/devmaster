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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Pedidos", description = "Gerenciamento de pedidos e itens")
@RequestMapping("/v1/pedidos")
public interface PedidoAPI {
    
    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido com itens e opções")
    ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody PedidoRequest request);
    
    @GetMapping("/{pedidoId}")
    @Operation(summary = "Buscar pedido", description = "Busca um pedido por ID com todos os detalhes")
    ResponseEntity<PedidoResponse> buscarPedido(@PathVariable Long pedidoId);
    
    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar por número", description = "Busca um pedido pelo número")
    ResponseEntity<PedidoResponse> buscarPedidoPorNumero(@PathVariable String numeroPedido);
    
    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Listar pedidos do restaurante", description = "Lista pedidos de um restaurante com filtro opcional de status")
    ResponseEntity<List<PedidoResumoResponse>> listarPedidosRestaurante(
        @PathVariable Long restauranteId,
        @RequestParam(required = false) StatusPedido status
    );
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos do cliente", description = "Lista pedidos de um cliente com filtro opcional de status")
    ResponseEntity<List<PedidoResumoResponse>> listarPedidosCliente(
        @PathVariable Long clienteId,
        @RequestParam(required = false) StatusPedido status
    );
    
    @GetMapping("/restaurante/{restauranteId}/paginado")
    @Operation(summary = "Listar pedidos paginado", description = "Lista pedidos do restaurante com paginação")
    ResponseEntity<Page<PedidoResumoResponse>> listarPedidosRestauranteComPaginacao(
        @PathVariable Long restauranteId,
        @RequestParam(required = false) StatusPedido status,
        Pageable pageable
    );
    
    @GetMapping("/cliente/{clienteId}/paginado")
    @Operation(summary = "Listar pedidos do cliente paginado", description = "Lista pedidos do cliente com paginação")
    ResponseEntity<Page<PedidoResumoResponse>> listarPedidosClienteComPaginacao(
        @PathVariable Long clienteId,
        Pageable pageable
    );
    
    @PatchMapping("/{pedidoId}/status")
    @Operation(summary = "Atualizar status", description = "Atualiza o status do pedido")
    ResponseEntity<PedidoResponse> atualizarStatus(
        @PathVariable Long pedidoId,
        @Valid @RequestBody AtualizarStatusPedidoRequest request
    );
    
    @PatchMapping("/{pedidoId}/confirmar")
    @Operation(summary = "Confirmar pedido", description = "Confirma o pedido")
    ResponseEntity<Void> confirmarPedido(@PathVariable Long pedidoId);
    
    @PatchMapping("/{pedidoId}/iniciar-preparo")
    @Operation(summary = "Iniciar preparo", description = "Inicia o preparo do pedido")
    ResponseEntity<Void> iniciarPreparo(@PathVariable Long pedidoId);
    
    @PatchMapping("/{pedidoId}/marcar-pronto")
    @Operation(summary = "Marcar como pronto", description = "Marca o pedido como pronto")
    ResponseEntity<Void> marcarComoPronto(@PathVariable Long pedidoId);
    
    @PatchMapping("/{pedidoId}/despachar")
    @Operation(summary = "Despachar pedido", description = "Despacha o pedido para entrega vinculando um entregador")
    ResponseEntity<Void> despacharPedido(
        @PathVariable Long pedidoId,
        @RequestParam Long entregadorId
    );
    
    @PatchMapping("/{pedidoId}/entregar")
    @Operation(summary = "Entregar pedido", description = "Marca o pedido como entregue")
    ResponseEntity<Void> entregarPedido(@PathVariable Long pedidoId);
    
    @PatchMapping("/{pedidoId}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela o pedido")
    ResponseEntity<Void> cancelarPedido(
        @PathVariable Long pedidoId,
        @Valid @RequestBody CancelarPedidoRequest request
    );
    
    @PatchMapping("/{pedidoId}/aprovar-pagamento")
    @Operation(summary = "Aprovar pagamento", description = "Aprova o pagamento do pedido")
    ResponseEntity<Void> aprovarPagamento(@PathVariable Long pedidoId);
    
    @PatchMapping("/{pedidoId}/recusar-pagamento")
    @Operation(summary = "Recusar pagamento", description = "Recusa o pagamento do pedido")
    ResponseEntity<Void> recusarPagamento(@PathVariable Long pedidoId);
    
    @GetMapping("/{pedidoId}/historico")
    @Operation(summary = "Buscar histórico", description = "Busca o histórico de status do pedido")
    ResponseEntity<List<HistoricoStatusPedidoResponse>> buscarHistorico(@PathVariable Long pedidoId);
    
    @GetMapping("/restaurante/{restauranteId}/periodo")
    @Operation(summary = "Buscar por período", description = "Busca pedidos do restaurante em um período")
    ResponseEntity<List<PedidoResumoResponse>> buscarPedidosPorPeriodo(
        @PathVariable Long restauranteId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim
    );
    
    @GetMapping("/restaurante/{restauranteId}/ativos")
    @Operation(summary = "Buscar pedidos ativos", description = "Busca pedidos em andamento do restaurante")
    ResponseEntity<List<PedidoResumoResponse>> buscarPedidosAtivos(@PathVariable Long restauranteId);
}
