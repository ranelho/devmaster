package com.devmaster.domain;

import com.devmaster.application.api.request.RestauranteRequest;
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

    @Column(name = "avaliacao", precision = 3, scale = 2)
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

    @Column(name = "taxa_entrega", precision = 10, scale = 2)
    private BigDecimal taxaEntrega;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "tempo_medio_entrega")
    private Integer tempoMedioEntrega;

    @Column(name = "valor_minimo_perdido", precision = 10, scale = 2)
    private BigDecimal valorMinimoPerdido;

    @Column(name = "empresa_id")
    private Long empresaId;

    public void alternarAtivo() {
        this.ativo = !this.ativo;
    }

    public Restaurante(RestauranteRequest restauranteRequest) {
        this.cnpj = restauranteRequest.cnpj();
        this.nome = restauranteRequest.nome();
        this.descricao = restauranteRequest.descricao();
        this.bannerUrl = restauranteRequest.bannerUrl();
        this.aberto = restauranteRequest.aberto();
        this.ativo = true;
        this.avaliacao = restauranteRequest.avaliacao();
        this.logoUrl = restauranteRequest.logoUrl();
        this.slug = restauranteRequest.slug();
        this.taxaEntrega = restauranteRequest.taxaEntrega();
        this.telefone = restauranteRequest.telefone();
        this.empresaId = restauranteRequest.empresaId();
        this.criadoEm = LocalDateTime.now();
    }

    public void update(RestauranteRequest resquest) {
        this.cnpj = resquest.cnpj();
        this.nome = resquest.nome();
        this.descricao = resquest.descricao();
        this.bannerUrl = resquest.bannerUrl();
        this.aberto = resquest.aberto();
        this.telefone = resquest.telefone();
        this.email = resquest.email();
        this.slug = resquest.slug();
        this.empresaId = resquest.empresaId();
        this.logoUrl = resquest.logoUrl();
        this.avaliacao = resquest.avaliacao();
        this.taxaEntrega = resquest.taxaEntrega();
        this.atualizadoEm = LocalDateTime.now();
    }
}
