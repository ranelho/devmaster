package com.devmaster.application.api;

import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categoria", description = "API para gerenciamento de categorias de restaurantes")
@RequestMapping("/public/v1/categorias")
public interface CategoriaApi {

    @GetMapping("/{id}")
    @Operation(summary = "Busca todas as categorias ativas", description = "Busca todas as categorias ativas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    ResponseEntity<List<CategoriaResponse>> findAllAtivoByRestaurante(@PathVariable Long id);

    @GetMapping("/{id}/inativo")
    @Operation(summary = "Busca todas as categorias inativas", description = "Busca todas as categorias inativas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    ResponseEntity<List<CategoriaResponse>> findAllInativoByRestaurante(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Cria uma categoria", description = "Cria uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "409", description = "Categoria duplicada"),
    })
    ResponseEntity<CategoriaResponse> criar(@RequestBody @Valid CategoriaRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma categoria por ID", description = "Atualiza uma categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "409", description = "Categoria duplicada"),
    })
    ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id, @RequestBody @Valid CategoriaRequest request);

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Alterna a ativação de uma categoria por ID", description = "Alterna o status de ativo de uma categoria, não permitindo desativar categorias que hajam produtos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Desativado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "422", description = "Possui produtos cadastrados na categoria"),
    })
    ResponseEntity<CategoriaResponse> alternarAtivo(@PathVariable Long id);


}
