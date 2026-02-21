package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade Restaurante/Estabelecimento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true, nullable = false)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(length = 20)
    private String telefone;
    
    private String email;
    
    @Column(unique = true, length = 18)
    private String cnpj;
    
    @Column(name = "logo_url", length = 500)
    private String logoUrl;
    
    @Column(name = "banner_url", length = 500)
    private String bannerUrl;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(nullable = false)
    private Boolean aberto = false;
    
    @Column(precision = 3, scale = 2)
    private BigDecimal avaliacao = BigDecimal.ZERO;
    
    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega = BigDecimal.ZERO;
    
    @Column(name = "valor_minimo_pedido", precision = 10, scale = 2)
    private BigDecimal valorMinimoPedido = BigDecimal.ZERO;
    
    @Column(name = "tempo_medio_entrega")
    private Integer tempoMedioEntrega;
    
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
    
    public void ativar() {
        this.ativo = true;
    }
    
    public void desativar() {
        this.ativo = false;
    }
    
    public void abrir() {
        if (!this.ativo) {
            throw new IllegalStateException("Restaurante inativo não pode ser aberto");
        }
        this.aberto = true;
    }
    
    public void fechar() {
        this.aberto = false;
    }
    
    public void atualizarAvaliacao(BigDecimal novaAvaliacao) {
        if (novaAvaliacao.compareTo(BigDecimal.ZERO) < 0 || novaAvaliacao.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new IllegalArgumentException("Avaliação deve estar entre 0 e 5");
        }
        this.avaliacao = novaAvaliacao;
    }
}
