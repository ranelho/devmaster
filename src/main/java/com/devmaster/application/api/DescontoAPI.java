package com.devmaster.application.api;

import com.devmaster.application.api.request.DescontoRequest;
import com.devmaster.application.api.response.DescontoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gerenciamento de Descontos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Descontos", description = "Endpoints para gerenciamento de descontos")
@RequestMapping({"/v1/descontos", "/v2/descontos"})
public interface DescontoAPI {
    
    @Operation(summary = "Criar desconto", description = "Cria um novo desconto para um produto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Desconto criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PostMapping
    ResponseEntity<DescontoResponse> criar(
        @Parameter(description = "Dados do desconto", required = true)
        @Valid @RequestBody DescontoRequest request
    );
    
    @Operation(summary = "Atualizar desconto", description = "Atualiza um desconto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Desconto atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Desconto não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<DescontoResponse> atualizar(
        @Parameter(description = "ID do desconto", required = true)
        @PathVariable Long id,
        
        @Parameter(description = "Dados do desconto", required = true)
        @Valid @RequestBody DescontoRequest request
    );
    
    @Operation(summary = "Buscar desconto por ID", description = "Retorna os dados de um desconto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Desconto encontrado"),
        @ApiResponse(responseCode = "404", description = "Desconto não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<DescontoResponse> buscarPorId(
        @Parameter(description = "ID do desconto", required = true)
        @PathVariable Long id
    );
    
    @Operation(summary = "Listar todos os descontos", description = "Lista todos os descontos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de descontos retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<List<DescontoResponse>> listarTodos();
    
    @Operation(summary = "Listar descontos por produto", description = "Lista todos os descontos de um produto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de descontos retornada com sucesso")
    })
    @GetMapping("/produto/{produtoId}")
    ResponseEntity<List<DescontoResponse>> listarPorProduto(
        @Parameter(description = "ID do produto", required = true)
        @PathVariable Long produtoId
    );
    
    @Operation(summary = "Listar descontos vigentes", description = "Lista apenas os descontos ativos e vigentes no momento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de descontos vigentes retornada com sucesso")
    })
    @GetMapping("/vigentes")
    ResponseEntity<List<DescontoResponse>> listarVigentes();
    
    @Operation(summary = "Deletar desconto", description = "Remove um desconto do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Desconto deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Desconto não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(
        @Parameter(description = "ID do desconto", required = true)
        @PathVariable Long id
    );
}
