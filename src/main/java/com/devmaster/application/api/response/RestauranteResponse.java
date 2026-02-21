package com.devmaster.application.api.response;

import com.devmaster.domain.Restaurante;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta com dados completos do restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record RestauranteResponse(
    Long id,
    String nome,
    String slug,
    String descricao,
    String telefone,
    String email,
    String cnpj,
    String logoUrl,
    String bannerUrl,
    Boolean ativo,
    Boolean aberto,
    BigDecimal avaliacao,
    BigDecimal taxaEntrega,
    BigDecimal valorMinimoPedido,
    Integer tempoMedioEntrega,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    EnderecoRestauranteResponse endereco
) {
    public static RestauranteResponse from(Restaurante restaurante, EnderecoRestauranteResponse endereco) {
        return new RestauranteResponse(
            restaurante.getId(),
            restaurante.getNome(),
            restaurante.getSlug(),
            restaurante.getDescricao(),
            restaurante.getTelefone(),
            restaurante.getEmail(),
            restaurante.getCnpj(),
            restaurante.getLogoUrl(),
            restaurante.getBannerUrl(),
            restaurante.getAtivo(),
            restaurante.getAberto(),
            restaurante.getAvaliacao(),
            restaurante.getTaxaEntrega(),
            restaurante.getValorMinimoPedido(),
            restaurante.getTempoMedioEntrega(),
            restaurante.getCriadoEm(),
            restaurante.getAtualizadoEm(),
            endereco
        );
    }
}
