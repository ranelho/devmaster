package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteRequest(
        @NotNull(message = "Nome é obrigatório.")
        String nomeCompleto,
        @CPF(message = "CPF inválido.")
        String cpf,
        LocalDate dataNascimento

        //todo: Adicionar uma lista de enderecos
) {
}
