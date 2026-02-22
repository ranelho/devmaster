package com.devmaster.application.api;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.application.api.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST para gestão de endereços de clientes.
 * Todos os endpoints são públicos (não requerem autenticação).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Tag(name = "Endereços", description = "Gestão de endereços dos clientes")
@RequestMapping("/public/v1/clientes/{clienteId}/enderecos")
public interface EnderecoClienteAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adicionar endereço", description = "Adiciona endereço ao cadastro do cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço adicionado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    EnderecoClienteResponse adicionarEndereco(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId,
        @Valid @RequestBody EnderecoClienteRequest request
    );
    
    @GetMapping
    @Operation(summary = "Listar endereços", description = "Lista todos os endereços do cliente")
    List<EnderecoClienteResponse> listarEnderecos(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId
    );
    
    @GetMapping("/{enderecoId}")
    @Operation(summary = "Buscar endereço", description = "Busca endereço específico")
    EnderecoClienteResponse buscarEndereco(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId,
        @Parameter(description = "ID do endereço") @PathVariable Long enderecoId
    );
    
    @GetMapping("/padrao")
    @Operation(summary = "Buscar endereço padrão", description = "Busca endereço padrão do cliente")
    EnderecoClienteResponse buscarEnderecoPadrao(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId
    );
    
    @PutMapping("/{enderecoId}")
    @Operation(summary = "Atualizar endereço", description = "Atualiza dados de um endereço")
    EnderecoClienteResponse atualizarEndereco(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId,
        @Parameter(description = "ID do endereço") @PathVariable Long enderecoId,
        @Valid @RequestBody EnderecoClienteRequest request
    );
    
    @PatchMapping("/{enderecoId}/padrao")
    @Operation(summary = "Definir endereço padrão", description = "Define endereço como padrão")
    EnderecoClienteResponse definirEnderecoPadrao(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId,
        @Parameter(description = "ID do endereço") @PathVariable Long enderecoId
    );
    
    @DeleteMapping("/{enderecoId}")
    @Operation(summary = "Remover endereço", description = "Remove endereço do cadastro")
    MessageResponse removerEndereco(
        @Parameter(description = "ID do cliente") @PathVariable Long clienteId,
        @Parameter(description = "ID do endereço") @PathVariable Long enderecoId
    );
}
