package com.devmaster.application.api.response;

import com.devmaster.domain.Entregador;
import com.devmaster.domain.enums.TipoVeiculo;

import java.math.BigDecimal;

/**
 * DTO de resposta com resumo dos dados do entregador.
 * Usado em listagens e referências.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record EntregadorResumoResponse(
    Long id,
    String nomeCompleto,
    String telefone,
    TipoVeiculo tipoVeiculo,
    Boolean disponivel,
    BigDecimal avaliacao,
    Integer totalEntregas,
    String fotoPerfilUrl
) {
    public static EntregadorResumoResponse from(Entregador entregador) {
        return new EntregadorResumoResponse(
            entregador.getId(),
            entregador.getNomeCompleto(),
            entregador.getTelefone(),
            entregador.getTipoVeiculo(),
            entregador.getDisponivel(),
            entregador.getAvaliacao(),
            entregador.getTotalEntregas(),
            entregador.getFotoPerfilUrl()
        );
    }
}
