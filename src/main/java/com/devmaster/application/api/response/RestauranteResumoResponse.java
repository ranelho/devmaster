package com.devmaster.application.api.response;

import com.devmaster.domain.Restaurante;

import java.math.BigDecimal;

/**
 * DTO de resposta com resumo do restaurante.
 * Usado em listagens.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record RestauranteResumoResponse(
    Long id,
    String nome,
    String slug,
    String descricao,
    String logoUrl,
    Boolean ativo,
    Boolean aberto,
    BigDecimal avaliacao,
    BigDecimal taxaEntrega,
    BigDecimal valorMinimoPedido,
    Integer tempoMedioEntrega
) {
    public static RestauranteResumoResponse from(Restaurante restaurante) {
        return new RestauranteResumoResponse(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getSlug(),
            restaurante.getDescricao(),
            restaurante.getLogoUrl(),
            restaurante.getAtivo(),
            restaurante.getAberto(),
            restaurante.getAvaliacao(),
            restaurante.getTaxaEntrega(),
            restaurante.getValorMinimoPedido(),
            restaurante.getTempoMedioEntrega()
        );
    }
}
