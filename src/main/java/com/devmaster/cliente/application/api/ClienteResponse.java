package com.devmaster.cliente.application.api;

import com.devmaster.cliente.domain.Cliente;
import com.devmaster.cliente.domain.enums.EstadoCivil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ClienteResponse(
    UUID idCliente,
    String cpf,
    String fullName,
    LocalDate dataNascimento,
    String naturalidade,
    String nacionalidade,
    EstadoCivil estadoCivil,
    LocalDateTime createdAt,
    List<ContatoResponse> contatos

) {
    public ClienteResponse(Cliente cliente) {
        this(
            cliente.getIdCliente(),
            cliente.getCpf(),
            cliente.getFullName(),
            cliente.getDataNascimento(),
            cliente.getNaturalidade(),
            cliente.getNacionalidade(),
            cliente.getEstadoCivil(),
            cliente.getCreatedAt(),
            ContatoResponse.convertList(cliente.getContatos())
        );
    }
}