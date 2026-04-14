package com.devmaster.application.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

public record ClienteRequest(
        @Size(max = 255, message = "Email deve conter até 255 caracteres")
        String email,
        @NotBlank(message = "Nome completo é obrigatório")
        @Size(max = 255, message = "Nome completo deve conter até 255 caracteres")
        String nomeCompleto,
        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 20, message = "Telefone deve conter até 20 caracteres")
        String telefone,
        @CPF(message = "Cpf inválido")
        @Size(max = 14, message = "Cpf inválido")
        String cpf,
        LocalDateTime dataNascimento
) {
}
