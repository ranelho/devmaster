package com.devmaster.cliente.application.api.request;


import com.devmaster.cliente.application.api.annotation.Adult;
import com.devmaster.cliente.domain.enums.EstadoCivil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteRequest(
        @NotBlank(message = "Campo Obrigatório!")
        @Pattern(regexp = "(^\\d{3}(\\.?\\d{3}){2}-?\\d{2}$)|(^\\d{11}$)", message = "CPF inválido!")
        @CPF(message = "CPF inválido!")
        String cpf,

        @NotNull(message = "Campo Obrigatório!")
        String nomeCompleto,

        @Adult
        LocalDate dataNascimento,

        String naturalidade,
        String nacionalidade,
        EstadoCivil estadoCivil,

        ContatoRequest contatoRequest
) {}