package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa um cliente no sistema.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "clientes", indexes = {
    @Index(name = "idx_clientes_telefone", columnList = "telefone"),
    @Index(name = "idx_clientes_email", columnList = "email"),
    @Index(name = "idx_clientes_cpf", columnList = "cpf")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "telefone", nullable = false, unique = true, length = 20)
    private String telefone;
    
    @Column(name = "email", unique = true, length = 255)
    private String email;
    
    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;
    
    @Column(name = "cpf", unique = true, length = 14)
    private String cpf;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @CreationTimestamp
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @Column(name = "ativo", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
    
    /**
     * Desativa o cliente no sistema.
     */
    public void desativar() {
        this.ativo = false;
    }
    
    /**
     * Reativa o cliente no sistema.
     */
    public void reativar() {
        this.ativo = true;
    }
}
