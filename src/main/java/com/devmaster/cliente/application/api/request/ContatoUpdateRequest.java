package com.devmaster.cliente.application.api.request;

public record ContatoUpdateRequest(
        Long idContato,
        String telefone,
        String celular,
        String email
) {
}
