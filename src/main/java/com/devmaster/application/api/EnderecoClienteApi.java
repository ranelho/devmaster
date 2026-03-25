package com.devmaster.application.api;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endereço Cliente", description = "API para gerenciamento dos endereços do clientes")
@RequestMapping("/public/v1/enderecos-cliente")
public interface EnderecoClienteApi {

    @GetMapping("/all")
    @Operation(summary = "Busca todos os endereços de cliente", description = "Busca todos os endereços de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<List<EnderecoClienteResponse>> findAll();

    @GetMapping("/{id}")
    @Operation(summary = "Busca um endereço do cliente por ID", description = "Busca um endereço do cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço do cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoClienteResponse> findById(@PathVariable Long id);

    @GetMapping("/{id}/cliente")
    @Operation(summary = "Busca todos os endereços do cliente por ID do cliente", description = "Busca todos os endereços do cliente por ID do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<List<EnderecoClienteResponse>> findAllByClienteId(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Cria um endereço de cliente", description = "Cria um endereço de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoClienteResponse> criar(@RequestBody @Valid EnderecoClienteRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o endereço de um cliente por ID", description = "Cria um endereço de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoClienteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EnderecoClienteRequest request);

    @PatchMapping("/{id}")
    @Operation(summary = "Atribui um endereço como padrão para o cliente", description = "Atribui um endereço como padrão para o cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Endereço já é padrão"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoClienteResponse> alterarPadrao(@PathVariable Long id);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um endereço por ID", description = "Deleta um endereço por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<Void> deletar(@PathVariable Long id);

}
