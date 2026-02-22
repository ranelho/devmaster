package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API para gerenciamento de Tipos de Pagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Tipos de Pagamento", description = "Gerenciamento de formas de pagamento")
@RequestMapping("/v1/tipos-pagamento")
public interface TipoPagamentoAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar tipo de pagamento", description = "Cria um novo tipo de pagamento")
    TipoPagamentoResponse criarTipoPagamento(
        @Valid @RequestBody TipoPagamentoRequest request
    );
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de pagamento", description = "Busca um tipo de pagamento por ID")
    TipoPagamentoResponse buscarTipoPagamento(
        @PathVariable Long id
    );
    
    @GetMapping
    @Operation(summary = "Listar tipos de pagamento", description = "Lista tipos de pagamento com filtro opcional")
    List<TipoPagamentoResponse> listarTiposPagamento(
        @RequestParam(required = false) Boolean ativo
    );
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de pagamento", description = "Atualiza um tipo de pagamento")
    TipoPagamentoResponse atualizarTipoPagamento(
        @PathVariable Long id,
        @Valid @RequestBody TipoPagamentoRequest request
    );
    
    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ativar tipo de pagamento", description = "Ativa um tipo de pagamento")
    void ativarTipoPagamento(
        @PathVariable Long id
    );
    
    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desativar tipo de pagamento", description = "Desativa um tipo de pagamento")
    void desativarTipoPagamento(
        @PathVariable Long id
    );
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover tipo de pagamento", description = "Remove um tipo de pagamento")
    void removerTipoPagamento(
        @PathVariable Long id
    );
}
