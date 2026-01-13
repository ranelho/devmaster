package com.devmaster.cliente.application.api;

public record ContatoRequest(
        String telefone,
        String celular,
        String email
) {
}
