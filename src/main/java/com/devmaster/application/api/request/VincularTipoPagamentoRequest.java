package com.devmaster.application.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Requisição para vincular tipo de pagamento ao restaurante")
public record VincularTipoPagamentoRequest(
    
    @NotNull(message = "ID do tipo de pagamento é obrigatório")
    @Schema(description = "ID do tipo de pagamento", example = "1", required = true)
    Long tipoPagamentoId,
    
    @Schema(description = "Ordem de exibição", example = "1")
    Integer ordemExibicao
) {}
