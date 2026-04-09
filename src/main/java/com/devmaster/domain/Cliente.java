package com.devmaster.domain;

import com.devmaster.application.api.request.ClienteRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "data_nascimento")
    private LocalDateTime dataNascimento;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    public Cliente(ClienteRequest request) {
        this.email = request.email();
        this.nomeCompleto = request.nomeCompleto();
        this.telefone = request.telefone();
        this.cpf = request.cpf();
        this.ativo = true;
        this.dataNascimento = request.dataNascimento();
        this.criadoEm = LocalDateTime.now();
    }

    public void update(ClienteRequest request) {
        this.email = request.email();
        this.nomeCompleto = request.nomeCompleto();
        this.telefone = request.telefone();
        this.cpf = request.cpf();
        this.ativo = true;
        this.dataNascimento = request.dataNascimento();
        this.criadoEm = LocalDateTime.now();
    }

    public void alternar() {
        this.ativo = !this.ativo;
    }

    @Override
    public final boolean equals(Object o) {
        return o instanceof Cliente cliente && id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
