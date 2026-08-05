package com.devmaster.domain;

import com.devmaster.application.api.request.ProdutoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
    private Categoria categoria;

    @Column(name = "nome", nullable = true, length = 100)
    private String nome;

    @Column(name = "descricao", nullable = true, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "preco", precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @Column(name = "preco_promocional", precision = 10, scale = 2, nullable = true)
    private BigDecimal precoPromocional;

    @Column(name = "tempo_preparo", nullable = true)
    private Integer tempoPreparo;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel;

    @Column(name = "destaque", nullable = false)
    private Boolean destaque;

    @Column(name = "ordem_exibicao")
    private Integer ordemExibicao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    public Produto(ProdutoRequest request) {
        this.nome = request.nome();
        this.descricao = request.descricao();
        this.preco = request.preco();
        this.precoPromocional = request.precoPromocional();
        this.tempoPreparo = request.tempoPreparo();
        this.tipo = request.tipo() == null ? Tipo.SIMPLES : Tipo.valueOf(request.tipo());
        this.disponivel = request.preco().equals(new BigDecimal("0.0"));
        this.destaque = false;
        this.ordemExibicao = request.ordemExibicao() == null ? 0 : request.ordemExibicao();
        this.categoria = new Categoria();
        this.categoria.setId(request.categoriaId());
        this.restaurante = new Restaurante();
        this.restaurante.setId(request.restauranteId());
    }

    public void update(ProdutoRequest request) {
        this.nome = request.nome();
        this.descricao = request.descricao();
        this.preco = request.preco();
        this.precoPromocional = request.precoPromocional();
        this.tempoPreparo = request.tempoPreparo();
        this.tipo = request.tipo() == null ? Tipo.SIMPLES : Tipo.valueOf(request.tipo());
        this.disponivel = request.preco().equals(new BigDecimal("0.0"));
        this.destaque = false;
        this.ordemExibicao = request.ordemExibicao() == null ? 0 : request.ordemExibicao();
        this.categoria = new Categoria();
        this.categoria.setId(request.categoriaId());
        this.restaurante = new Restaurante();
        this.restaurante.setId(request.restauranteId());
    }

    public void alternarDisponibilidade() {
        this.disponivel = !this.disponivel;
    }
}
