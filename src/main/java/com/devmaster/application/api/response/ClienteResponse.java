package com.devmaster.application.api.response;

import com.devmaster.domain.Cliente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record ClienteResponse(
        Long id,
        String email,
        String nomeCompleto,
        String telefone,
        String cpf,
        LocalDateTime dataNascimento,
        Boolean ativo
) {

    public ClienteResponse(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getEmail(),
                cliente.getNomeCompleto(),
                cliente.getTelefone(),
                cliente.getCpf(),
                cliente.getDataNascimento(),
                cliente.getAtivo());
    }

    public static List<ClienteResponse> convert(List<Cliente> list) {
        return list.stream().map(ClienteResponse::new).toList();
    }
}
