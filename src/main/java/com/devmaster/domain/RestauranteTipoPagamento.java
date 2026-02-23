package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que vincula tipos de pagamento aos restaurantes.
 */
@Entity
@Table(name = "restaurante_tipo_pagamento", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"restaurante_id", "tipo_pagamento_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteTipoPagamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_pagamento_id", nullable = false)
    private TipoPagamento tipoPagamento;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    @Column(name = "vinculado_em", nullable = false, updatable = false)
    private LocalDateTime vinculadoEm;
    
    @PrePersist
    protected void onCreate() {
        vinculadoEm = LocalDateTime.now();
    }
    
    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
}
