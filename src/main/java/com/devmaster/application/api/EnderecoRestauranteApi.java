package com.devmaster.application.api;

import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Endereço Restaurante", description = "API de gerenciamento de endereço dos restaurantes")
@RequestMapping("/public/v1/enderecos-restaurante")
public interface EnderecoRestauranteApi {

    @GetMapping("/all")
    @Operation(summary = "Busca o endereço do restaurante por ID", description = "Busca o endereço do restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<List<EnderecoRestauranteResponse>> findAll();

    @GetMapping("/{id}")
    @Operation(summary = "Busca um endereço do restaurante por ID", description = "Busca um endereço do restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço do restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoRestauranteResponse> findById(@PathVariable Long id);

    @GetMapping("/{id}/restaurante")
    @Operation(summary = "Busca todos os endereços do restaurante por ID", description = "Busca todos os endereços do restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço do restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<List<EnderecoRestauranteResponse>> findAllByRestauranteId(@PathVariable Long id);

    @PostMapping
    @Operation(summary = "Cria o endereço do restaurante", description = "Cria o endereço do restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoRestauranteResponse> criar(@RequestBody EnderecoRestauranteRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza o endereço do restaurante", description = "Atualiza o endereço do restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<EnderecoRestauranteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EnderecoRestauranteRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete um restaurante por ID", description = "Deleta um restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<Void> deletar(@PathVariable Long id);
}
