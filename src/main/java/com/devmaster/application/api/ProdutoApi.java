package com.devmaster.application.api;

import com.devmaster.application.api.request.ProdutoRequest;
import com.devmaster.application.api.response.ProdutoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Produto", description = "API para gerenciamento de produtos dos restaurantes")
@RequestMapping("/public/v1/produtos")
public interface ProdutoApi {

    @GetMapping
    @Operation(summary = "Busca todos os produtos ativos", description = "Busca todos os produtos ativos por id de categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    ResponseEntity<Page<ProdutoResponse>> findAllByCategoriaId(@RequestParam(name = "categoria_id", required = true) Long categoriaId,
                                                  @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size);

    @GetMapping("/{id}")
    @Operation(summary = "Busca todos os produtos ativos", description = "Busca todos os produtos ativos por id de categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    ResponseEntity<ProdutoResponse> findById(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Cria um produto", description = "Cria um produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "409", description = "Produto duplicado"),
    })
    ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto", description = "Atualiza um produto por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Produto duplicado"),
    })
    ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest request);

    @PatchMapping("/{id}/disponibilidade")
    @Operation(summary = "Alterna a disponibilidade de um produto por ID", description = "Alternar visibilidade de um produto")
    ResponseEntity<ProdutoResponse> alternarDisponibilidade(@PathVariable Long id);
}
