package com.devmaster.cliente.application.api;

import com.devmaster.cliente.domain.Contato;

import java.util.List;

public record ContatoResponse(
        Long idContato,
        String telefone,
        String celular,
        String email
) {

    public ContatoResponse(Contato contato) {
        this(
                contato.getIdContato(),
                contato.getTelefone(),
                contato.getCelular(),
                contato.getEmail()
        );
    }

    public static List<ContatoResponse> convertList(List<Contato> contatos) {
        return contatos.stream().map(ContatoResponse::new).toList();
    }

}