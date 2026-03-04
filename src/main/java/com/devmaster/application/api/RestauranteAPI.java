package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarRestauranteRequest;
import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.request.HorarioRestauranteRequest;
import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.application.api.response.HorarioRestauranteResponse;
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
import org.springframework.http.ResponseEntity;
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
    
    // ========================================
    // GESTÃO DO RESTAURANTE
    // ========================================
    
    @Operation(summary = "Criar novo restaurante", description = "Cria um novo restaurante no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado")
    })
    @PostMapping
    ResponseEntity<RestauranteResponse> criarRestaurante(
        @Parameter(description = "Dados do restaurante", required = true)
        @Valid @RequestBody RestauranteRequest request
    );
    
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados detalhados de um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{restauranteId}")
    ResponseEntity<RestauranteResponse> buscarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Listar restaurantes", description = "Lista restaurantes com filtros e paginação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<Page<RestauranteResumoResponse>> listarRestaurantes(
        @Parameter(description = "Filtrar por nome")
        @RequestParam(required = false) String nome,
        
        @Parameter(description = "Filtrar por status ativo")
        @RequestParam(required = false) Boolean ativo,
        
        @Parameter(description = "Filtrar por status aberto")
        @RequestParam(required = false) Boolean aberto,
        
        @PageableDefault(size = 20) Pageable pageable
    );
    
    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PutMapping("/{restauranteId}")
    ResponseEntity<RestauranteResponse> atualizarRestaurante(
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
    ResponseEntity<Void> ativarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Desativar restaurante", description = "Desativa um restaurante ativo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PatchMapping("/{restauranteId}/desativar")
    ResponseEntity<Void> desativarRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Abrir restaurante", description = "Abre o restaurante para receber pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante aberto com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Restaurante já está aberto ou inativo")
    })
    @PatchMapping("/{restauranteId}/abrir")
    ResponseEntity<Void> abrirRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Fechar restaurante", description = "Fecha o restaurante para pedidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante fechado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Restaurante já está fechado")
    })
    @PatchMapping("/{restauranteId}/fechar")
    ResponseEntity<Void> fecharRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    // ========================================
    // ENDEREÇO
    // ========================================
    
    @Operation(summary = "Buscar endereço", description = "Retorna o endereço do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou endereço não encontrado")
    })
    @GetMapping("/{restauranteId}/endereco")
    ResponseEntity<EnderecoRestauranteResponse> buscarEndereco(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Adicionar endereço", description = "Adiciona ou atualiza o endereço do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço adicionado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PostMapping("/{restauranteId}/endereco")
    ResponseEntity<EnderecoRestauranteResponse> adicionarEndereco(
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
    ResponseEntity<EnderecoRestauranteResponse> atualizarEndereco(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Dados para atualização", required = true)
        @Valid @RequestBody EnderecoRestauranteRequest request
    );
    
    // ========================================
    // HORÁRIOS DE FUNCIONAMENTO
    // ========================================
    
    @Operation(summary = "Listar horários", description = "Lista os horários de funcionamento do restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de horários retornada com sucesso")
    })
    @GetMapping("/{restauranteId}/horarios")
    ResponseEntity<List<HorarioRestauranteResponse>> listarHorarios(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
    
    @Operation(summary = "Atualizar horários", description = "Atualiza a lista de horários de funcionamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horários atualizados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PutMapping("/{restauranteId}/horarios")
    ResponseEntity<List<HorarioRestauranteResponse>> atualizarHorarios(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId,
        
        @Parameter(description = "Lista de horários", required = true)
        @Valid @RequestBody List<HorarioRestauranteRequest> horarios
    );
}
