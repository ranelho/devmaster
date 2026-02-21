package com.devmaster.application.api;

import com.devmaster.application.api.response.ProdutoResponse;
import com.devmaster.application.api.response.ProdutoResumoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST pública para consulta de Produtos (sem autenticação).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Produtos Públicos", description = "Endpoints públicos para consulta de produtos")
@RequestMapping("/public/v1/restaurantes/{restauranteId}/produtos")
public interface PublicProdutoAPI {
    
    @Operation(summary = "Listar produtos do restaurante", description = "Lista produtos disponíveis do restaurante (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping
    List<ProdutoResumoResponse> listarProdutos(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Filtrar por categoria")
        @RequestParam(required = false) Long categoriaId,
        
        @Parameter(description = "Filtrar apenas disponíveis")
        @RequestParam(required = false, defaultValue = "true") Boolean disponivel,
        
        @Parameter(description = "Filtrar apenas destaques")
        @RequestParam(required = false) Boolean destaque
    );
    
    @Operation(summary = "Buscar produto por ID", description = "Retorna os dados completos de um produto com imagens e opções (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{produtoId}")
    ProdutoResponse buscarProduto(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "ID do produto", required = true)
        @PathVariable Long produtoId
    );
    
    @Operation(summary = "Listar produtos em destaque", description = "Lista produtos em destaque do restaurante (acesso público)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    })
    @GetMapping("/destaques")
    List<ProdutoResumoResponse> listarProdutosDestaque(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Limite de resultados")
        @RequestParam(defaultValue = "10") int limite
    );
}
