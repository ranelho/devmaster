package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade OpcaoItemPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "opcoes_item_pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpcaoItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "item_pedido_id", nullable = false)
    private ItemPedido itemPedido;
    
    @ManyToOne
    @JoinColumn(name = "grupo_opcao_id", nullable = false)
    private GrupoOpcao grupoOpcao;
    
    @ManyToOne
    @JoinColumn(name = "opcao_id", nullable = false)
    private Opcao opcao;
    
    @Column(name = "preco_adicional", precision = 10, scale = 2)
    private BigDecimal precoAdicional = BigDecimal.ZERO;
}
