package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Produto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "produtos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @Column(name = "preco_promocional", precision = 10, scale = 2)
    private BigDecimal precoPromocional;
    
    @Column(name = "tempo_preparo")
    private Integer tempoPreparo;
    
    @Column(nullable = false)
    private Boolean disponivel = true;
    
    @Column(nullable = false)
    private Boolean destaque = false;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
    
    // Métodos de negócio
    
    public void disponibilizar() {
        this.disponivel = true;
    }
    
    public void indisponibilizar() {
        this.disponivel = false;
    }
    
    public void destacar() {
        this.destaque = true;
    }
    
    public void removerDestaque() {
        this.destaque = false;
    }
    
    public boolean temPromocao() {
        return precoPromocional != null && precoPromocional.compareTo(preco) < 0;
    }
    
    public BigDecimal getPrecoFinal() {
        return temPromocao() ? precoPromocional : preco;
    }
}
