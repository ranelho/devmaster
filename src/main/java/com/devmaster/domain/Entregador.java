package com.devmaster.domain;

import com.devmaster.domain.enums.CategoriaCNH;
import com.devmaster.domain.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa um entregador no sistema.
 * Cadastro centralizado gerenciado pelo módulo DEVMASTER.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "entregadores", indexes = {
    @Index(name = "idx_entregadores_telefone", columnList = "telefone"),
    @Index(name = "idx_entregadores_cpf", columnList = "cpf"),
    @Index(name = "idx_entregadores_cnh", columnList = "cnh"),
    @Index(name = "idx_entregadores_ativo", columnList = "ativo"),
    @Index(name = "idx_entregadores_disponivel", columnList = "disponivel"),
    @Index(name = "idx_entregadores_ativo_disponivel", columnList = "ativo, disponivel")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entregador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;
    
    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;
    
    @Column(name = "email", unique = true, length = 255)
    private String email;
    
    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;
    
    @Column(name = "cnh", unique = true, length = 20)
    private String cnh;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_cnh", length = 5)
    private CategoriaCNH categoriaCnh;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_veiculo", length = 30)
    private TipoVeiculo tipoVeiculo;
    
    @Column(name = "placa_veiculo", length = 10)
    private String placaVeiculo;
    
    @Column(name = "modelo_veiculo", length = 100)
    private String modeloVeiculo;
    
    @Column(name = "cor_veiculo", length = 50)
    private String corVeiculo;
    
    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
    
    @Column(name = "disponivel", nullable = false)
    @Builder.Default
    private Boolean disponivel = false;
    
    @Column(name = "avaliacao", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal avaliacao = BigDecimal.ZERO;
    
    @Column(name = "total_entregas", nullable = false)
    @Builder.Default
    private Integer totalEntregas = 0;
    
    @Column(name = "foto_perfil_url", length = 500)
    private String fotoPerfilUrl;
    
    @Column(name = "foto_cnh_url", length = 500)
    private String fotoCnhUrl;
    
    @Column(name = "foto_veiculo_url", length = 500)
    private String fotoVeiculoUrl;
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    /**
     * Incrementa o total de entregas realizadas pelo entregador.
     */
    public void incrementarTotalEntregas() {
        this.totalEntregas++;
    }
    
    /**
     * Atualiza a avaliação média do entregador.
     * 
     * @param novaAvaliacao Nova avaliação recebida (1-5)
     */
    public void atualizarAvaliacao(BigDecimal novaAvaliacao) {
        if (this.totalEntregas == 0) {
            this.avaliacao = novaAvaliacao;
        } else {
            // Calcula média ponderada
            BigDecimal somaAvaliacoes = this.avaliacao.multiply(BigDecimal.valueOf(this.totalEntregas));
            somaAvaliacoes = somaAvaliacoes.add(novaAvaliacao);
            this.avaliacao = somaAvaliacoes.divide(
                BigDecimal.valueOf(this.totalEntregas + 1), 
                2, 
                BigDecimal.ROUND_HALF_UP
            );
        }
    }
    
    /**
     * Altera a disponibilidade do entregador.
     * 
     * @param disponivel true se disponível, false caso contrário
     */
    public void alterarDisponibilidade(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    /**
     * Desativa o entregador no sistema.
     */
    public void desativar() {
        this.ativo = false;
        this.disponivel = false;
    }
    
    /**
     * Reativa o entregador no sistema.
     */
    public void reativar() {
        this.ativo = true;
    }
}
