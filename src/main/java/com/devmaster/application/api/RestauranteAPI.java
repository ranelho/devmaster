package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarRestauranteRequest;
import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gerenciamento de Restaurantes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Restaurantes", description = "Endpoints para gerenciamento de restaurantes")
@RequestMapping("/v1/restaurantes")
public interface RestauranteAPI {
    
    @Operation(summary = "Criar novo restaurante", description = "Cria um novo restaurante no sistema (apenas SUPER_ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas SUPER_ADMIN"),
        @ApiResponse(responseCode = "409", description = "Slug, CNPJ ou email já cadastrado")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    RestauranteResponse criarRestaurante(
        @Parameter(description = "Dados do restaurante", required = true)
        @Valid @RequestBody RestauranteRequest request
    );
    
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados completos de um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{restauranteId}")
    RestauranteResponse buscarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Buscar restaurante por slug", description = "Retorna os dados completos de um restaurante pelo slug")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/slug/{slug}")
    RestauranteResponse buscarRestaurantePorSlug(
        @Parameter(description = "Slug do restaurante", required = true)
        @PathVariable String slug
    );
    
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com filtros e paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    @GetMapping
    Page<RestauranteResumoResponse> listarRestaurantes(
        @Parameter(description = "Filtrar por status ativo")
        @RequestParam(required = false) Boolean ativo,
        
        @Parameter(description = "Filtrar por status aberto")
        @RequestParam(required = false) Boolean aberto,
        
        @Parameter(description = "Filtrar por nome (busca parcial)")
        @RequestParam(required = false) String nome,
        
        @PageableDefault(size = 20) Pageable pageable
    );
    
    @Operation(summary = "Listar restaurantes abertos", description = "Lista restaurantes abertos ordenados por avaliação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    @GetMapping("/abertos")
    List<RestauranteResumoResponse> listarRestaurantesAbertos(
        @Parameter(description = "Limite de resultados")
        @RequestParam(defaultValue = "10") int limite
    );
    
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante (apenas SUPER_ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - apenas SUPER_ADMIN"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PutMapping("/{restauranteId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    RestauranteResponse atualizarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Dados para atualização", required = true)
        @Valid @RequestBody AtualizarRestauranteRequest request
    );
    
    @Operation(summary = "Ativar restaurante", description = "Ativa um restaurante desativado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante ativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PatchMapping("/{restauranteId}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void ativarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Desativar restaurante", description = "Desativa um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PatchMapping("/{restauranteId}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desativarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Abrir restaurante", description = "Marca o restaurante como aberto para pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante aberto com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Restaurante inativo não pode ser aberto")
    })
    @PatchMapping("/{restauranteId}/abrir")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void abrirRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Fechar restaurante", description = "Marca o restaurante como fechado para pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante fechado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PatchMapping("/{restauranteId}/fechar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void fecharRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Adicionar endereço", description = "Adiciona endereço ao restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço adicionado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "409", description = "Restaurante já possui endereço")
    })
    @PostMapping("/{restauranteId}/endereco")
    @ResponseStatus(HttpStatus.CREATED)
    EnderecoRestauranteResponse adicionarEndereco(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Dados do endereço", required = true)
        @Valid @RequestBody EnderecoRestauranteRequest request
    );
    
    @Operation(summary = "Atualizar endereço", description = "Atualiza o endereço do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou endereço não encontrado")
    })
    @PutMapping("/{restauranteId}/endereco")
    EnderecoRestauranteResponse atualizarEndereco(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Dados do endereço", required = true)
        @Valid @RequestBody EnderecoRestauranteRequest request
    );
    
    @Operation(summary = "Buscar endereço", description = "Retorna o endereço do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou endereço não encontrado")
    })
    @GetMapping("/{restauranteId}/endereco")
    EnderecoRestauranteResponse buscarEndereco(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
}
