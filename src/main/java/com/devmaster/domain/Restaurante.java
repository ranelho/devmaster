package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "aberto", nullable = false)
    private Boolean aberto;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "avaliacao")
    private BigDecimal avaliacao;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Column(name = "cnpj", length = 14)
    private String cnpj;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "slug", length = 255, nullable = false)
    private String slug;

    @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "tempo_medio_entrega")
    private Integer tempoMedioEntrega;

    @Column(name = "valor_minimo_perdido")
    private BigDecimal valorMinimoPerdido;

    @Column(name = "empresa_id")
    private Long empresaId;

    public void ativoInativo(boolean ativoInativo) {
        this.ativo = ativoInativo;
    }
}
