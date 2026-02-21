package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarCategoriaRequest;
import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * API REST para gerenciamento de Categorias.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias de produtos")
@RequestMapping("/v1/restaurantes/{restauranteId}/categorias")
public interface CategoriaAPI {
    
    @Operation(summary = "Criar nova categoria", description = "Cria uma nova categoria para o restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Já existe categoria com este nome")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    CategoriaResponse criarCategoria(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Dados da categoria", required = true)
        @Valid @RequestBody CategoriaRequest request
    );
    
    @Operation(summary = "Buscar categoria", description = "Retorna os dados de uma categoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria ou restaurante não encontrado")
    })
    @GetMapping("/{categoriaId}")
    CategoriaResponse buscarCategoria(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoriaId
    );
    
    @Operation(summary = "Listar categorias", description = "Lista todas as categorias do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    })
    @GetMapping
    List<CategoriaResponse> listarCategorias(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Filtrar por status ativo")
        @RequestParam(required = false) Boolean ativo
    );
    
    @Operation(summary = "Atualizar categoria", description = "Atualiza os dados de uma categoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Categoria ou restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Já existe categoria com este nome")
    })
    @PutMapping("/{categoriaId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    CategoriaResponse atualizarCategoria(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoriaId,
        
        @Parameter(description = "Dados para atualização", required = true)
        @Valid @RequestBody AtualizarCategoriaRequest request
    );
    
    @Operation(summary = "Ativar categoria", description = "Ativa uma categoria desativada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria ativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria ou restaurante não encontrado")
    })
    @PatchMapping("/{categoriaId}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void ativarCategoria(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoriaId
    );
    
    @Operation(summary = "Desativar categoria", description = "Desativa uma categoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria desativada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Categoria ou restaurante não encontrado")
    })
    @PatchMapping("/{categoriaId}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desativarCategoria(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoriaId
    );
    
    @Operation(summary = "Remover categoria", description = "Remove uma categoria do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria removida com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Categoria ou restaurante não encontrado")
    })
    @DeleteMapping("/{categoriaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE')")
    void removerCategoria(
        @Parameter(description = "ID do usuário autenticado", required = true)
        @RequestHeader("X-User-Id") UUID usuarioId,
        
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoriaId
    );
}
