package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.CalcularEntregaRequest;
import com.devmaster.application.api.response.CalcularEntregaResponse;
import com.devmaster.application.api.response.EnderecoViaCepResponse;
import com.devmaster.application.service.EnderecoService;
import com.devmaster.application.service.EntregaIntegrationService;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EnderecoApplicationService implements EnderecoService {

    private final RestTemplate restTemplate;
    private final EntregaIntegrationService entregaIntegrationService;
    
    @Value("${google.maps.api.key:}")
    private String googleMapsApiKey;

    @Override
    public EnderecoViaCepResponse buscarEnderecoPorCep(String cep) {
        // Remove formatação do CEP
        String cepLimpo = cep.replaceAll("[^0-9]", "");
        
        if (cepLimpo.length() != 8) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "CEP inválido");
        }

        // Buscar endereço no ViaCEP
        String viaCepUrl = String.format("https://viacep.com.br/ws/%s/json/", cepLimpo);
        
        try {
            EnderecoViaCepResponse.ViaCepData viaCepData = restTemplate.getForObject(
                viaCepUrl, 
                EnderecoViaCepResponse.ViaCepData.class
            );
            
            if (viaCepData == null || viaCepData.cep() == null) {
                throw APIException.build(HttpStatus.NOT_FOUND, "CEP não encontrado");
            }
            
            // Buscar coordenadas no Google Maps
            Double[] coordenadas = buscarCoordenadas(viaCepData);
            
            return EnderecoViaCepResponse.fromViaCep(
                viaCepData, 
                coordenadas[0], 
                coordenadas[1]
            );
            
        } catch (Exception e) {
            throw APIException.build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar endereço", e);
        }
    }

    @Override
    public CalcularEntregaResponse calcularEntrega(CalcularEntregaRequest request) {
        // Delega para o serviço de integração que decide se usa API de Entrega ou cálculo local
        return entregaIntegrationService.calcularEntrega(request);
    }
    
    private Double[] buscarCoordenadas(EnderecoViaCepResponse.ViaCepData viaCepData) {
        // Se não tiver chave do Google Maps, retorna coordenadas padrão
        if (googleMapsApiKey == null || googleMapsApiKey.isEmpty()) {
            return new Double[]{-16.367809, -39.588305}; // Coordenadas padrão
        }
        
        try {
            // Monta endereço completo para geocoding
            String endereco = String.format("%s, %s, %s - %s, Brasil",
                viaCepData.logradouro(),
                viaCepData.bairro(),
                viaCepData.localidade(),
                viaCepData.uf()
            );
            
            String geocodeUrl = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                endereco.replace(" ", "+"),
                googleMapsApiKey
            );
            
            // Fazer requisição ao Google Maps
            GoogleMapsResponse response = restTemplate.getForObject(
                geocodeUrl,
                GoogleMapsResponse.class
            );
            
            if (response != null && 
                "OK".equals(response.status()) && 
                response.results() != null && 
                !response.results().isEmpty()) {
                
                GoogleMapsResponse.Result result = response.results().get(0);
                return new Double[]{
                    result.geometry().location().lat(),
                    result.geometry().location().lng()
                };
            }
            
        } catch (Exception e) {
            // Se falhar, retorna coordenadas padrão
            System.err.println("Erro ao buscar coordenadas: " + e.getMessage());
        }
        
        // Retorna coordenadas padrão se não conseguir buscar
        return new Double[]{-16.367809, -39.588305};
    }
    
    // Records para deserialização da resposta do Google Maps
    private record GoogleMapsResponse(
        String status,
        java.util.List<Result> results
    ) {
        record Result(Geometry geometry) {}
        record Geometry(Location location) {}
        record Location(Double lat, Double lng) {}
    }
}
