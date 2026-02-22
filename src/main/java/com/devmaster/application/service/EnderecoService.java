package com.devmaster.application.service;

import com.devmaster.application.api.request.CalcularEntregaRequest;
import com.devmaster.application.api.response.CalcularEntregaResponse;
import com.devmaster.application.api.response.EnderecoViaCepResponse;

public interface EnderecoService {
    EnderecoViaCepResponse buscarEnderecoPorCep(String cep);
    CalcularEntregaResponse calcularEntrega(CalcularEntregaRequest request);
}
