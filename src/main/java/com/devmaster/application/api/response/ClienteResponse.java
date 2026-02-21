package com.devmaster.application.api.response;

import com.devmaster.domain.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta com dados completos do cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record ClienteResponse(
    Long id,
    String telefone,
    String email,
    String nomeCompleto,
    String cpf,
    LocalDate dataNascimento,
    Boolean ativo,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm,
    List<EnderecoClienteResponse> enderecos
) {
    public static ClienteResponse from(Cliente cliente, List<EnderecoClienteResponse> enderecos) {
        return new ClienteResponse(
            cliente.getId(),
            cliente.getTelefone(),
            cliente.getEmail(),
            cliente.getNomeCompleto(),
            cliente.getCpf(),
            cliente.getDataNascimento(),
            cliente.getAtivo(),
            cliente.getCriadoEm(),
            cliente.getAtualizadoEm(),
            enderecos
        );
    }
}
