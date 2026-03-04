package com.devmaster.application.api;

import com.devmaster.application.api.request.VincularEntregadorRequest;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Entregador-Restaurante", description = "Gestão de vínculos entre entregadores e restaurantes")
@RequestMapping({"/v1/entregador-restaurante", "/v2/entregador-restaurante"})
public interface EntregadorRestauranteAPI {
    
    @PostMapping("/vincular")
    @Operation(summary = "Vincular entregador a restaurante")
    ResponseEntity<MessageResponse> vincular(@Valid @RequestBody VincularEntregadorRequest request);
    
    @DeleteMapping("/desvincular")
    @Operation(summary = "Desvincular entregador de restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Entregador desvinculado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Vínculo não encontrado")
    })
    ResponseEntity<Void> desvincular(@Valid @RequestBody VincularEntregadorRequest request);
    
    @GetMapping("/restaurante/{restauranteId}/entregadores")
    @Operation(summary = "Listar entregadores vinculados ao restaurante")
    ResponseEntity<List<EntregadorResumoResponse>> listarEntregadoresPorRestaurante(@PathVariable Long restauranteId);
    
    @GetMapping("/restaurante/{restauranteId}/entregadores/disponiveis")
    @Operation(summary = "Listar entregadores disponíveis do restaurante")
    ResponseEntity<List<EntregadorResumoResponse>> listarEntregadoresDisponiveisPorRestaurante(@PathVariable Long restauranteId);
    
    @GetMapping("/buscar-por-cpf/{cpf}")
    @Operation(summary = "Buscar entregador por CPF")
    ResponseEntity<EntregadorResumoResponse> buscarPorCpf(@PathVariable String cpf);
}
