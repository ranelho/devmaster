package com.devmaster.application.api;

import com.devmaster.application.api.request.VincularTipoPagamentoRequest;
import com.devmaster.application.api.response.RestauranteTipoPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Restaurante - Tipos de Pagamento", description = "Gerenciamento de tipos de pagamento do restaurante")
@RequestMapping("/v1/restaurantes/{restauranteId}/tipos-pagamento")
@SecurityRequirement(name = "bearerAuth")
public interface RestauranteTipoPagamentoAPI {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Vincular tipo de pagamento", description = "Vincula um tipo de pagamento ao restaurante")
    @ApiResponse(responseCode = "201", description = "Tipo de pagamento vinculado com sucesso")
    @ApiResponse(responseCode = "409", description = "Tipo de pagamento já vinculado")
    RestauranteTipoPagamentoResponse vincularTipoPagamento(
        @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId,
        @Valid @RequestBody VincularTipoPagamentoRequest request
    );
    
    @GetMapping
    @Operation(summary = "Listar tipos de pagamento", description = "Lista todos os tipos de pagamento do restaurante")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    List<RestauranteTipoPagamentoResponse> listarTiposPagamento(
        @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId
    );
    
    @DeleteMapping("/{tipoPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desvincular tipo de pagamento", description = "Remove o vínculo do tipo de pagamento com o restaurante")
    @ApiResponse(responseCode = "204", description = "Tipo de pagamento desvinculado com sucesso")
    void desvincularTipoPagamento(
        @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId,
        @Parameter(description = "ID do tipo de pagamento") @PathVariable Long tipoPagamentoId
    );
}
