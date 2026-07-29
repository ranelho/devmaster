package com.devmaster.domain;


public enum Tipo {
    SIMPLES("Simples"),
    COMBO("Combo");

    private final String nome;

    Tipo(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
