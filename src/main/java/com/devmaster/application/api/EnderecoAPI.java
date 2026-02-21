package com.devmaster.application.api;

import com.devmaster.application.api.response.EnderecoViaCepResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Endereço", description = "APIs para buscar endereço por CEP")
@RequestMapping("/public/enderecos")
public interface EnderecoAPI {

    @Operation(summary = "Buscar endereço por CEP", 
               description = "Busca endereço completo com coordenadas usando ViaCEP e Google Maps")
    @GetMapping("/cep/{cep}")
    ResponseEntity<EnderecoViaCepResponse> buscarPorCep(@PathVariable String cep);
}
