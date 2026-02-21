package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa o histórico de mudanças de disponibilidade e localização do entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "historico_disponibilidade_entregador", indexes = {
    @Index(name = "idx_historico_disponibilidade_entregador_id", columnList = "entregador_id"),
    @Index(name = "idx_historico_disponibilidade_criado_em", columnList = "criado_em"),
    @Index(name = "idx_historico_disponibilidade_entregador_criado", columnList = "entregador_id, criado_em")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoDisponibilidadeEntregador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entregador_id", nullable = false)
    private Entregador entregador;
    
    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    /**
     * Verifica se o histórico possui coordenadas de localização.
     * 
     * @return true se possui latitude e longitude, false caso contrário
     */
    public boolean temLocalizacao() {
        return latitude != null && longitude != null;
    }
}
