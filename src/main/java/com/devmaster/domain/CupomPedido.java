package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade CupomPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "cupons_pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CupomPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "cupom_id", nullable = false)
    private Cupom cupom;
    
    @Column(name = "desconto_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal descontoAplicado;
}
