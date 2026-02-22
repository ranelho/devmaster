package com.devmaster.application.api;

import com.devmaster.application.api.request.CalcularEntregaRequest;
import com.devmaster.application.api.response.CalcularEntregaResponse;
import com.devmaster.application.api.response.EnderecoViaCepResponse;
import com.devmaster.application.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EnderecoRestController implements EnderecoAPI {

    private final EnderecoService enderecoService;

    @Override
    public ResponseEntity<EnderecoViaCepResponse> buscarPorCep(String cep) {
        EnderecoViaCepResponse endereco = enderecoService.buscarEnderecoPorCep(cep);
        return ResponseEntity.ok(endereco);
    }

    @Override
    public ResponseEntity<CalcularEntregaResponse> calcularEntrega(CalcularEntregaRequest request) {
        CalcularEntregaResponse response = enderecoService.calcularEntrega(request);
        return ResponseEntity.ok(response);
    }
}
