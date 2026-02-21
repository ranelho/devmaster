package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade Categoria de produtos.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
    
    // Métodos de negócio
    
    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
}
