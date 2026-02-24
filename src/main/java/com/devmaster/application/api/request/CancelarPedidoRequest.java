package com.devmaster.application.api.request;

import jakarta.validation.constraints.*;

public record CancelarPedidoRequest(
    @NotBlank(message = "Motivo do cancelamento é obrigatório")
    @Size(max = 2000, message = "Motivo deve ter no máximo 2000 caracteres")
    String motivo
) {}
