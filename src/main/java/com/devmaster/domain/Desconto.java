package com.devmaster.domain;

import com.devmaster.domain.enums.TipoIntervaloDesconto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "descontos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Desconto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualDesconto;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoIntervaloDesconto tipoIntervalo;
    
    @Column(nullable = false)
    private LocalDateTime dataHoraInicio;
    
    @Column(nullable = false)
    private LocalDateTime dataHoraFim;
    
    @Column(nullable = false)
    private Boolean ativo = true;
    
    @Column(updatable = false)
    private LocalDateTime criadoEm;
    
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
    
    public boolean isVigente() {
        LocalDateTime agora = LocalDateTime.now();
        return ativo && !agora.isBefore(dataHoraInicio) && !agora.isAfter(dataHoraFim);
    }
}
