package com.devmaster.application.api.response;

import com.devmaster.domain.RestauranteTipoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Tipo de pagamento disponível no restaurante")
public record RestauranteTipoPagamentoResponse(
    
    @Schema(description = "ID do vínculo", example = "1")
    Long id,
    
    @Schema(description = "ID do tipo de pagamento", example = "1")
    Long tipoPagamentoId,
    
    @Schema(description = "Nome do tipo de pagamento", example = "Cartão de Crédito")
    String nome,
    
    @Schema(description = "Código do tipo de pagamento", example = "CREDIT_CARD")
    String codigo,
    
    @Schema(description = "Descrição", example = "Pagamento com cartão de crédito")
    String descricao,
    
    @Schema(description = "URL do ícone", example = "https://example.com/icon.png")
    String iconeUrl,
    
    @Schema(description = "Requer troco", example = "false")
    Boolean requerTroco,
    
    @Schema(description = "Ordem de exibição", example = "1")
    Integer ordemExibicao,
    
    @Schema(description = "Status ativo", example = "true")
    Boolean ativo
) {
    public static RestauranteTipoPagamentoResponse from(RestauranteTipoPagamento entity) {
        return RestauranteTipoPagamentoResponse.builder()
            .id(entity.getId())
            .tipoPagamentoId(entity.getTipoPagamento().getId())
            .nome(entity.getTipoPagamento().getNome())
            .codigo(entity.getTipoPagamento().getCodigo())
            .descricao(entity.getTipoPagamento().getDescricao())
            .iconeUrl(entity.getTipoPagamento().getIconeUrl())
            .requerTroco(entity.getTipoPagamento().getRequerTroco())
            .ordemExibicao(entity.getOrdemExibicao())
            .ativo(entity.getAtivo())
            .build();
    }
}
