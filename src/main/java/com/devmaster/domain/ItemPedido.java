package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade ItemPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "itens_pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false)
    private Integer quantidade = 1;
    
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    // Métodos de negócio
    
    public void calcularSubtotal(BigDecimal totalOpcoesAdicionais) {
        BigDecimal precoComOpcoes = precoUnitario.add(totalOpcoesAdicionais);
        this.subtotal = precoComOpcoes.multiply(BigDecimal.valueOf(quantidade));
    }
}
