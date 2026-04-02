package com.devmaster.application.api.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EnderecoClienteRequest(
        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 100, message = "Bairro deve conter até 100 caracteres")
        String bairro,
        @NotBlank(message = "CEP é obrigatório")
        @Size(max = 10, message = "CEP deve conter até 10 caracteres")
        String cep,
        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100, message = "Cidade deve conter até 100 caracteres")
        String cidade,
        @NotBlank(message = "Estado é obrigatório")
        @Size(max = 2, message = "Estado deve conter 2 caracteres")
        String estado,
        @NotBlank(message = "Logradouro é obrigatório")
        @Size(max = 255, message = "Logradouro deve conter até 255 caracteres")
        String logradouro,
        @NotBlank(message = "Número é obrigatório")
        @Size(max = 100, message = "Número deve conter até 20 caracteres")
        String numero,
        @Size(max = 255, message = "Complemento deve conter até 255 caracteres")
        String complemento,
        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,
        @Size(max = 50, message = "Rotulo deve conter até 50 caracteres")
        String rotulo,
        Boolean padrao,
        @Digits(integer = 10, fraction = 8, message = "Latitude inválida")
        BigDecimal latitude,
        @Digits(integer = 11, fraction = 8, message = "Longitude inválida")
        BigDecimal longitude
) {
}
