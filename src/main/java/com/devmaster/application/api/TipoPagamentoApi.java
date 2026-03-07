package com.devmaster.application.api;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "Tipos de Pagamento", description = "API para gerenciamento de tipos de pagamento")
@RequestMapping("/public/v1/tipos-pagamento")
interface TipoPagamentoApi {

    @PostMapping
    @Operation(summary = "Cria um novo tipo de pagamento", description = "Cria um novo tipo de pagamento")
    ResponseEntity<TipoPagamentoResponse> criar(@Valid @RequestBody TipoPagamentoRequest request);

    @GetMapping("/{id}")
    @Operation(summary = "Busca um tipo de pagamento por ID", description = "Busca um tipo de pagamento por ID")
    ResponseEntity<TipoPagamentoResponse> buscarPorId(@PathVariable Long id);

    @GetMapping("/all")
    @Operation(summary = "Busca todos os tipos de pagamento", description = "Busca todos os tipos de pagamento")
    ResponseEntity<List<TipoPagamentoResponse>> buscarTodos();

    @GetMapping("/all-paginado")
    @Operation(summary = "Busca todos os tipos de pagamento paginados", description = "Busca todos os tipos de pagamento paginados")
    ResponseEntity<Page<TipoPagamentoResponse>> buscarTodosPaginado(Pageable pageable);
}
