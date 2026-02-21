package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidade EnderecoRestaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "enderecos_restaurante")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "restaurante_id", nullable = false, unique = true)
    private Restaurante restaurante;
    
    @Column(nullable = false)
    private String logradouro;
    
    @Column(nullable = false, length = 20)
    private String numero;
    
    private String complemento;
    
    @Column(nullable = false, length = 100)
    private String bairro;
    
    @Column(nullable = false, length = 100)
    private String cidade;
    
    @Column(nullable = false, length = 2)
    private String estado;
    
    @Column(nullable = false, length = 10)
    private String cep;
    
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;
}
