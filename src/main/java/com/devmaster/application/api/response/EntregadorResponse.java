package com.devmaster.application.api.response;

import com.devmaster.domain.Entregador;
import com.devmaster.domain.enums.CategoriaCNH;
import com.devmaster.domain.enums.TipoVeiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EntregadorResponse(
    Long id,
    String nomeCompleto,
    String telefone,
    String email,
    String cpf,
    String cnh,
    CategoriaCNH categoriaCnh,
    TipoVeiculo tipoVeiculo,
    String placaVeiculo,
    String modeloVeiculo,
    String corVeiculo,
    Boolean ativo,
    Boolean disponivel,
    BigDecimal avaliacao,
    Integer totalEntregas,
    String fotoPerfilUrl,
    String fotoCnhUrl,
    String fotoVeiculoUrl,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm
) {
    public static EntregadorResponse from(Entregador entregador) {
        return new EntregadorResponse(
            entregador.getId(),
            entregador.getNomeCompleto(),
            entregador.getTelefone(),
            entregador.getEmail(),
            entregador.getCpf(),
            entregador.getCnh(),
            entregador.getCategoriaCnh(),
            entregador.getTipoVeiculo(),
            entregador.getPlacaVeiculo(),
            entregador.getModeloVeiculo(),
            entregador.getCorVeiculo(),
            entregador.getAtivo(),
            entregador.getDisponivel(),
            entregador.getAvaliacao(),
            entregador.getTotalEntregas(),
            entregador.getFotoPerfilUrl(),
            entregador.getFotoCnhUrl(),
            entregador.getFotoVeiculoUrl(),
            entregador.getCriadoEm(),
            entregador.getAtualizadoEm()
        );
    }
}
