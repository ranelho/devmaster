package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade TipoPagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "tipos_pagamento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoPagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 30)
    private String codigo;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "icone_url", length = 500)
    private String iconeUrl;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "requer_troco", nullable = false)
    private Boolean requerTroco = false;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
    
    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
}
