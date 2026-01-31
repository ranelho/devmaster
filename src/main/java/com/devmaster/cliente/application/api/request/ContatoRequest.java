package com.devmaster.cliente.application.api.request;

public record ContatoRequest(
        String telefone,
        String celular,
        String email
) {
}
