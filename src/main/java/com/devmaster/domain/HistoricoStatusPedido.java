package com.devmaster.domain;

import com.devmaster.domain.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade HistoricoStatusPedido.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "historico_status_pedido")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoStatusPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @Column(name = "criado_por", length = 100)
    private String criadoPor;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
    }
}
