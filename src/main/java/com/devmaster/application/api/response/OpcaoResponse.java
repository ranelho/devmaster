package com.devmaster.application.api.response;

import com.devmaster.domain.Opcao;

import java.math.BigDecimal;

/**
 * Response para Opcao.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record OpcaoResponse(
    Long id,
    Long grupoOpcaoId,
    String nome,
    BigDecimal precoAdicional,
    Boolean disponivel,
    Integer ordemExibicao
) {
    public static OpcaoResponse from(Opcao opcao) {
        return new OpcaoResponse(
            opcao.getId(),
            opcao.getGrupoOpcao().getId(),
            opcao.getNome(),
            opcao.getPrecoAdicional(),
            opcao.getDisponivel(),
            opcao.getOrdemExibicao()
        );
    }
}
