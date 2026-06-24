package com.devmaster.application.api;

import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Cliente", description = "API para gerenciamento de clientes")
@RequestMapping("/public/v1/clientes")
public interface ClienteApi {

    @GetMapping
    @Operation(summary = "Busca todos os clientes", description = "Busca todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<List<ClienteResponse>> findAll();

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente por ID", description = "Busca um Cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<ClienteResponse> findById(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Cria um cliente", description = "Cria um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<ClienteResponse> criar(@RequestBody @Valid ClienteRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um cliente por ID", description = "Cria um endereço de cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest request);

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Atribui um endereço como padrão para o cliente", description = "Alterna o status de ativo de um cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<ClienteResponse> alternarAtivo(@PathVariable Long id);

}
