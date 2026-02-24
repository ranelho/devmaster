package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "entregador_restaurante", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"entregador_id", "restaurante_id"}),
    indexes = {
        @Index(name = "idx_entregador_restaurante_entregador", columnList = "entregador_id"),
        @Index(name = "idx_entregador_restaurante_restaurante", columnList = "restaurante_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntregadorRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entregador_id", nullable = false)
    private Entregador entregador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
    
    @CreationTimestamp
    @Column(name = "vinculado_em", nullable = false, updatable = false)
    private LocalDateTime vinculadoEm;
    
    @Column(name = "desvinculado_em")
    private LocalDateTime desvinculadoEm;
    
    public void desvincular() {
        this.ativo = false;
        this.desvinculadoEm = LocalDateTime.now();
    }
    
    public void reativar() {
        this.ativo = true;
        this.desvinculadoEm = null;
    }
}
