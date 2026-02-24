package com.devmaster.application.api;

import com.devmaster.application.api.request.AlterarDisponibilidadeRequest;
import com.devmaster.application.api.request.AtualizarEntregadorRequest;
import com.devmaster.application.api.request.EntregadorRequest;
import com.devmaster.application.api.response.EntregadorResponse;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.EstatisticasEntregadorResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.domain.enums.TipoVeiculo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gestão de entregadores.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Entregadores", description = "Gestão centralizada de entregadores")
@RequestMapping("/entregadores")
public interface EntregadorAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Criar novo entregador",
        description = "Cria um novo entregador no sistema. Qualquer usuário autenticado pode criar."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entregador criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "409", description = "CPF, telefone, email ou CNH já cadastrado")
    })
    EntregadorResponse criarEntregador(@Valid @RequestBody EntregadorRequest request);
    
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Listar entregadores",
        description = "Lista todos os entregadores com filtros e paginação"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de entregadores"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    Page<EntregadorResumoResponse> listarEntregadores(
        @Parameter(description = "Filtrar por status ativo") @RequestParam(required = false) Boolean ativo,
        @Parameter(description = "Filtrar por disponibilidade") @RequestParam(required = false) Boolean disponivel,
        @Parameter(description = "Filtrar por tipo de veículo") @RequestParam(required = false) TipoVeiculo tipoVeiculo,
        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "20") int size
    );
    
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Buscar entregador por ID",
        description = "Retorna dados completos de um entregador específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    EntregadorResponse buscarEntregador(@Parameter(description = "ID do entregador") @PathVariable Long id);
    
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Atualizar entregador",
        description = "Atualiza dados de um entregador. Requer role SUPER_ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado"),
        @ApiResponse(responseCode = "409", description = "Telefone, email ou CNH já cadastrado")
    })
    EntregadorResponse atualizarEntregador(
        @Parameter(description = "ID do entregador") @PathVariable Long id,
        @Valid @RequestBody AtualizarEntregadorRequest request
    );
    
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Desativar entregador",
        description = "Desativa um entregador no sistema. Requer role SUPER_ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador desativado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    MessageResponse desativarEntregador(@Parameter(description = "ID do entregador") @PathVariable Long id);
    
    @PatchMapping("/{id}/reativar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Reativar entregador",
        description = "Reativa um entregador desativado. Requer role SUPER_ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador reativado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    MessageResponse reativarEntregador(@Parameter(description = "ID do entregador") @PathVariable Long id);
    
    @PatchMapping("/{id}/disponibilidade")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Alterar disponibilidade",
        description = "Altera disponibilidade do entregador. Requer role SUPER_ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidade alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou entregador inativo"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    MessageResponse alterarDisponibilidade(
        @Parameter(description = "ID do entregador") @PathVariable Long id,
        @Valid @RequestBody AlterarDisponibilidadeRequest request
    );
    
    @GetMapping("/disponiveis")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Buscar entregadores disponíveis próximos",
        description = "Busca entregadores disponíveis próximos a uma localização"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de entregadores próximos"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão")
    })
    List<EntregadorResumoResponse> buscarEntregadoresDisponiveisProximos(
        @Parameter(description = "Latitude de referência", required = true) @RequestParam Double latitude,
        @Parameter(description = "Longitude de referência", required = true) @RequestParam Double longitude,
        @Parameter(description = "Raio de busca em km") @RequestParam(defaultValue = "10.0") Double raioKm
    );
    
    @GetMapping("/{id}/estatisticas")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Obter estatísticas do entregador",
        description = "Retorna estatísticas detalhadas do entregador"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas do entregador"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Sem permissão"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    EstatisticasEntregadorResponse obterEstatisticas(@Parameter(description = "ID do entregador") @PathVariable Long id);
    
    @GetMapping("/{id}/validar")
    @Operation(
        summary = "Validar entregador",
        description = "Valida existência e disponibilidade do entregador. Usado para integração com módulo ENTREGA."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador validado"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    EntregadorResumoResponse validarEntregador(@Parameter(description = "ID do entregador") @PathVariable Long id);
}
