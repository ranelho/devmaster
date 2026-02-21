package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade Opcao.
 * Representa uma opção dentro de um grupo (ex: Tamanho Grande, Bacon).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "opcoes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Opcao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "grupo_opcao_id", nullable = false)
    private GrupoOpcao grupoOpcao;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(name = "preco_adicional", precision = 10, scale = 2)
    private BigDecimal precoAdicional = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Boolean disponivel = true;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    // Métodos de negócio
    
    public void disponibilizar() {
        this.disponivel = true;
    }
    
    public void indisponibilizar() {
        this.disponivel = false;
    }
    
    public boolean temPrecoAdicional() {
        return precoAdicional != null && precoAdicional.compareTo(BigDecimal.ZERO) > 0;
    }
}
