package com.devmaster.domain;

import com.devmaster.application.api.request.CategoriaRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias", uniqueConstraints = {
        @UniqueConstraint(name = "NomeUnicoPorRestaurante", columnNames = {"nome", "restaurante_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", referencedColumnName = "id")
    private Restaurante restaurante;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", nullable = true, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "ordem_exibicao", nullable = true)
    private Integer ordemExibicao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }

    public Categoria(CategoriaRequest request) {
        this.nome = request.nome();
        this.restaurante = new Restaurante();
        this.setId(request.restauranteId());
        this.descricao = request.descricao();
        this.ordemExibicao = request.ordemExibicao();
        this.ativo = true;
    }

    public void update(CategoriaRequest request) {
        this.nome = request.nome();
        this.descricao = request.descricao();
        this.ordemExibicao = request.ordemExibicao();
    }

    public void alternar() {
        this.ativo = !this.ativo;
    }
}
