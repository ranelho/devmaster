package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "enderecos_restaurante")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoRestaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @Column(name = "cep", nullable = false, length = 10)
    private String cep;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "complemento", length = 255)
    private String complemento;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "logradouro", nullable = false, length = 255)
    private String logradouro;

    @Column(name = "longitude", precision = 10, scale = 8)
    private BigDecimal longitude;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id", nullable = false)
    private Restaurante restaurante;

}
