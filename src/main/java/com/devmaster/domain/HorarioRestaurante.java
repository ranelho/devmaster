package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

/**
 * Entidade HorarioRestaurante.
 * Representa os horários de funcionamento por dia da semana.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "horarios_restaurante")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HorarioRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    /**
     * Dia da semana: 0=Domingo, 1=Segunda, 2=Terça, 3=Quarta, 4=Quinta, 5=Sexta, 6=Sábado
     */
    @Column(name = "dia_semana", nullable = false)
    private Integer diaSemana;
    
    @Column(name = "horario_abertura", nullable = false)
    private LocalTime horarioAbertura;
    
    @Column(name = "horario_fechamento", nullable = false)
    private LocalTime horarioFechamento;
    
    @Column(nullable = false)
    private Boolean fechado = false;
    
    // Métodos de negócio
    
    public void marcarComoFechado() {
        this.fechado = true;
    }
    
    public void marcarComoAberto() {
        this.fechado = false;
    }
    
    public boolean isAberto() {
        return !this.fechado;
    }
}
