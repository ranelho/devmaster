package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidade GrupoOpcao.
 * Representa um grupo de opções para um produto (ex: Tamanho, Adicionais).
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "grupos_opcoes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrupoOpcao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(columnDefinition = "TEXT")
    private String descricao;
    
    @Column(name = "minimo_selecoes")
    private Integer minimoSelecoes = 0;
    
    @Column(name = "maximo_selecoes")
    private Integer maximoSelecoes = 1;
    
    @Column(nullable = false)
    private Boolean obrigatorio = false;
    
    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao = 0;
    
    // Métodos de negócio
    
    public boolean isSelecaoValida(int quantidadeSelecionada) {
        if (quantidadeSelecionada < minimoSelecoes) {
            return false;
        }
        return quantidadeSelecionada <= maximoSelecoes;
    }
    
    public boolean isSelecaoMultipla() {
        return maximoSelecoes > 1;
    }
}
