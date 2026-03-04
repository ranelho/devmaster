package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API para gerenciamento de Tipos de Pagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Tipos de Pagamento", description = "Gerenciamento de formas de pagamento")
@RequestMapping({"/v1/tipos-pagamento", "/v2/tipos-pagamento"})
public interface TipoPagamentoAPI {
    
    @PostMapping
    @Operation(summary = "Criar tipo de pagamento", description = "Cria um novo tipo de pagamento")
    ResponseEntity<TipoPagamentoResponse> criarTipoPagamento(
        @Valid @RequestBody TipoPagamentoRequest request
    );
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de pagamento", description = "Busca um tipo de pagamento por ID")
    ResponseEntity<TipoPagamentoResponse> buscarTipoPagamento(
        @PathVariable Long id
    );
    
    @GetMapping
    @Operation(summary = "Listar tipos de pagamento", description = "Lista tipos de pagamento com filtro opcional")
    ResponseEntity<List<TipoPagamentoResponse>> listarTiposPagamento(
        @RequestParam(required = false) Boolean ativo
    );
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de pagamento", description = "Atualiza um tipo de pagamento")
    ResponseEntity<TipoPagamentoResponse> atualizarTipoPagamento(
        @PathVariable Long id,
        @Valid @RequestBody TipoPagamentoRequest request
    );
    
    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar tipo de pagamento", description = "Ativa um tipo de pagamento")
    ResponseEntity<Void> ativarTipoPagamento(
        @PathVariable Long id
    );
    
    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar tipo de pagamento", description = "Desativa um tipo de pagamento")
    ResponseEntity<Void> desativarTipoPagamento(
        @PathVariable Long id
    );
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover tipo de pagamento", description = "Remove um tipo de pagamento")
    ResponseEntity<Void> removerTipoPagamento(
        @PathVariable Long id
    );
}
