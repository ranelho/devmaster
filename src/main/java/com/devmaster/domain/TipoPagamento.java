package com.devmaster.domain;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.request.TipoPagamentoUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "tipos_pagamento",
        uniqueConstraints = @UniqueConstraint(columnNames = "codigo")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ícone_url", length = 500)
    private String iconeUrl;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "requer_troco", nullable = false)
    private Boolean requerTroco = false;

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    public TipoPagamento(TipoPagamentoRequest request) {
        this.ativo = request.ativo() != null ? request.ativo() : true;
        this.nome = request.nome().toUpperCase();
        this.descricao = request.descricao();
        this.iconeUrl = request.iconeUrl();
        this.codigo = request.codigo().toUpperCase();
        this.ordemExibicao = request.ordemExibicao();
        this.requerTroco = request.requerTroco();
        this.criadoEm = LocalDateTime.now();
    }

    public void update(TipoPagamentoUpdateRequest request) {
        this.ativo = request.ativo();
        this.nome = request.nome();
        this.descricao = request.descricao();
        this.iconeUrl = request.iconeUrl();
        this.ordemExibicao = request.ordemExibicao();
        this.requerTroco = request.requerTroco();
    }

    public void ativoInativo(boolean ativoInativo) {
        this.ativo = ativoInativo;
    }
}

