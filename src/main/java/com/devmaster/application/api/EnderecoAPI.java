package com.devmaster.application.api;

import com.devmaster.application.api.request.CalcularEntregaRequest;
import com.devmaster.application.api.response.CalcularEntregaResponse;
import com.devmaster.application.api.response.EnderecoViaCepResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Endereço Público", description = "APIs públicas para buscar endereço e calcular entrega (sem autenticação)")
@RequestMapping("/public/v1/enderecos")
public interface EnderecoAPI {

    @Operation(
        summary = "Buscar endereço por CEP", 
        description = "Busca endereço completo com coordenadas usando ViaCEP e Google Maps. Endpoint público - não requer autenticação."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
        @ApiResponse(responseCode = "400", description = "CEP inválido"),
        @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    })
    @GetMapping("/cep/{cep}")
    ResponseEntity<EnderecoViaCepResponse> buscarPorCep(
        @Parameter(description = "CEP (apenas números)") @PathVariable String cep
    );

    @Operation(
        summary = "Calcular taxa e tempo de entrega",
        description = """
            Calcula a distância, tempo estimado e taxa de entrega entre o restaurante e o endereço do cliente.
            Usa a API do Google Maps para cálculo preciso.
            Endpoint público - não requer autenticação.
            """
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PostMapping("/calcular-entrega")
    ResponseEntity<CalcularEntregaResponse> calcularEntrega(
        @Valid @RequestBody CalcularEntregaRequest request
    );
}
