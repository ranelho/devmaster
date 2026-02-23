package com.devmaster.application.api;

import com.devmaster.application.api.response.RestauranteTipoPagamentoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Tipos de Pagamento Público", description = "Consulta pública de tipos de pagamento por restaurante")
@RequestMapping("/public/v1/restaurantes/{restauranteId}/tipos-pagamento")
public interface TipoPagamentoPublicoAPI {
    
    @GetMapping
    @Operation(
        summary = "Listar tipos de pagamento do restaurante",
        description = "Lista todos os tipos de pagamento ativos disponíveis no restaurante para realizar pedidos"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    List<RestauranteTipoPagamentoResponse> listarTiposPagamentoRestaurante(
        @Parameter(description = "ID do restaurante", required = true)
        @PathVariable Long restauranteId
    );
}
