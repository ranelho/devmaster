package com.devmaster.application.api;

import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST pública para consulta de Restaurantes (sem autenticação).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Restaurantes Públicos", description = "Endpoints públicos para consulta de restaurantes")
@RequestMapping("/public/v1/restaurantes")
public interface PublicRestauranteAPI {
    
    @Operation(summary = "Buscar restaurante por slug", description = "Retorna os dados completos de um restaurante pelo slug (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/slug/{slug}")
    RestauranteResponse buscarRestaurantePorSlug(
        @Parameter(description = "Slug do restaurante", required = true)
        @PathVariable String slug
    );
    
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados completos de um restaurante (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{restauranteId}")
    RestauranteResponse buscarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Listar restaurantes abertos", description = "Lista restaurantes abertos e ativos ordenados por avaliação (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    @GetMapping("/abertos")
    List<RestauranteResumoResponse> listarRestaurantesAbertos(
        @Parameter(description = "Limite de resultados")
        @RequestParam(defaultValue = "50") int limite
    );
    
    @Operation(summary = "Listar todos restaurantes ativos", description = "Lista todos os restaurantes ativos (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    @GetMapping("/ativos")
    List<RestauranteResumoResponse> listarRestaurantesAtivos();
    
    @Operation(summary = "Buscar restaurantes por nome", description = "Busca restaurantes ativos por nome (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    @GetMapping("/buscar")
    List<RestauranteResumoResponse> buscarPorNome(
        @Parameter(description = "Nome do restaurante (busca parcial)")
        @RequestParam String nome
    );
}
