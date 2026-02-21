package com.devmaster.application.service;

import com.devmaster.application.api.response.EnderecoViaCepResponse;

public interface EnderecoService {
    EnderecoViaCepResponse buscarEnderecoPorCep(String cep);
}
