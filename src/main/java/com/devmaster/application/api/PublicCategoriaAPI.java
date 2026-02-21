package com.devmaster.application.api;

import com.devmaster.application.api.response.CategoriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST pública para consulta de Categorias (sem autenticação).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Categorias Públicas", description = "Endpoints públicos para consulta de categorias")
@RequestMapping("/public/v1/restaurantes/{restauranteId}/categorias")
public interface PublicCategoriaAPI {
    
    @Operation(summary = "Listar categorias do restaurante", description = "Lista categorias ativas do restaurante ordenadas (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping
    List<CategoriaResponse> listarCategorias(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Buscar categoria por ID", description = "Retorna os dados de uma categoria (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{categoriaId}")
    CategoriaResponse buscarCategoria(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID da categoria", required = true)
        @PathVariable Long categoriaId
    );
}
