package com.devmaster.application.api.response;

import com.devmaster.domain.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClienteResponse(

        Long id,
        String nomeCompleto,
        String cpf,
        LocalDate dataNascimento,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm,
        Boolean ativo
) {
    public ClienteResponse(Cliente clienteSalvo) {
        this(clienteSalvo.getId(), clienteSalvo.getNomeCompleto(), clienteSalvo.getCpf(),
                clienteSalvo.getDataNascimento(), clienteSalvo.getCriadoEm(),
                clienteSalvo.getAtualizadoEm(),
                clienteSalvo.getAtivo());
    }
}
