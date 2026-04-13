package com.devmaster.application.api.response;

import com.devmaster.application.api.request.RestauranteRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurante", description = "API para gerenciamento de restaurantes")
@RequestMapping("/public/v1/restaurantes")
public interface RestauranteApi {

    @GetMapping
    @Operation(summary = "Busca todos os restaurantes", description = "Busca todos os restaurantes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<List<RestauranteResponse>> findAll();

    @GetMapping("/{id}")
    @Operation(summary = "Busca um restaurante por ID", description = "Busca um restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<RestauranteResponse> finById(@PathVariable int id);

    @PostMapping
    @Operation(summary = "Cria um restaurante", description = "Cria um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<RestauranteResponse> criar(@Valid @RequestBody RestauranteRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um restaurante por ID", description = "Atualiza um restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<RestauranteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteRequest request);

    @PatchMapping("/{id}/ativo")
    @Operation(summary = "Ativa um restaurante por ID", description = "Ativa um restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<RestauranteResponse> ativar(@PathVariable Long id);

    @PatchMapping("/{id}/inativo")
    @Operation(summary = "Inativa um restaurante por ID", description = "Inativa um restaurante por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno ocorreu")
    })
    ResponseEntity<RestauranteResponse> inativar(@PathVariable Long id);

    ResponseEntity<RestauranteResponse> findById(Long id);

    ResponseEntity<Page<RestauranteResponse>> findAllPageable(Pageable pageable);
}
