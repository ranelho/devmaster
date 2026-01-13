package com.devmaster.cliente.application.api;

import com.devmaster.cliente.domain.enums.EstadoCivil;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record EditaClienteRequest(
        @NotBlank(message = "Campo Obrigatório!") String firstName,
        LocalDate dataNascimento,
        String naturalidade,
        String nacionalidade,
        EstadoCivil estadoCivil
) {}