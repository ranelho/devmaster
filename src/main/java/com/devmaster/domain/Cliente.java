package com.devmaster.domain;

import com.devmaster.application.api.request.ClienteRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String nomeCompleto;
    String cpf;
    LocalDate dataNascimento;
    LocalDateTime criadoEm;
    LocalDateTime atualizadoEm;
    Boolean ativo;

    public Cliente(ClienteRequest request) {
        this.nomeCompleto = request.nomeCompleto();
        this.cpf = request.cpf();
        this.dataNascimento = request.dataNascimento();
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        this.ativo = true;
    }
}
