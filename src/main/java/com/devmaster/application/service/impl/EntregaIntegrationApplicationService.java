package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.CalcularEntregaRequest;
import com.devmaster.application.api.response.CalcularEntregaResponse;
import com.devmaster.application.service.EntregaIntegrationService;
import com.devmaster.domain.EnderecoRestaurante;
import com.devmaster.domain.Restaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.EnderecoRestauranteRepository;
import com.devmaster.infrastructure.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Implementação do serviço de integração com a API de Entrega.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EntregaIntegrationApplicationService implements EntregaIntegrationService {

    private final RestTemplate restTemplate;
    private final RestauranteRepository restauranteRepository;
    private final EnderecoRestauranteRepository enderecoRestauranteRepository;

    @Value("${entrega.api.url:http://localhost:8081/api}")
    private String entregaApiUrl;

    @Value("${entrega.api.enabled:false}")
    private boolean entregaApiEnabled;

    @Override
    public CalcularEntregaResponse calcularEntrega(CalcularEntregaRequest request) {
        log.info("Calculando entrega para restaurante {} - lat: {}, lng: {}", 
            request.restauranteId(), request.latitude(), request.longitude());

        // Buscar restaurante
        Restaurante restaurante = restauranteRepository.findById(request.restauranteId())
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        // Buscar endereço do restaurante
        EnderecoRestaurante enderecoRestaurante = enderecoRestauranteRepository
            .findByRestauranteId(request.restauranteId())
            .orElseThrow(() -> APIException.build(
                HttpStatus.BAD_REQUEST,
                "Restaurante sem endereço cadastrado"
            ));

        // Verificar se restaurante tem coordenadas
        if (enderecoRestaurante.getLatitude() == null || enderecoRestaurante.getLongitude() == null) {
            throw APIException.build(
                HttpStatus.BAD_REQUEST,
                "Restaurante sem coordenadas cadastradas"
            );
        }

        // Se API de entrega estiver habilitada, tenta usar
        if (entregaApiEnabled && isApiDisponivel()) {
            try {
                return calcularViaApiEntrega(restaurante, enderecoRestaurante, request);
            } catch (Exception e) {
                log.warn("Erro ao usar API de Entrega, usando cálculo local: {}", e.getMessage());
                // Fallback para cálculo local
            }
        }

        // Cálculo local (fallback)
        return calcularLocal(restaurante, enderecoRestaurante, request);
    }

    @Override
    public boolean isApiDisponivel() {
        if (!entregaApiEnabled) {
            return false;
        }

        try {
            String healthUrl = entregaApiUrl + "/actuator/health";
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            log.debug("API de Entrega não disponível: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Calcula entrega usando a API de Entrega (com Google Maps).
     */
    private CalcularEntregaResponse calcularViaApiEntrega(
            Restaurante restaurante,
            EnderecoRestaurante enderecoRestaurante,
            CalcularEntregaRequest request) {

        log.info("Usando API de Entrega para cálculo");

        String url = entregaApiUrl + "/public/v1/entrega/calcular";

        // Monta request para API de entrega
        Map<String, Object> apiRequest = Map.of(
            "origemLatitude", enderecoRestaurante.getLatitude(),
            "origemLongitude", enderecoRestaurante.getLongitude(),
            "destinoLatitude", request.latitude(),
            "destinoLongitude", request.longitude()
        );

        try {
            // Chama API de entrega
            @SuppressWarnings("unchecked")
            ResponseEntity<Map<String, Object>> response = (ResponseEntity<Map<String, Object>>) (ResponseEntity<?>) restTemplate.postForEntity(url, apiRequest, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();

                double distanciaKm = ((Number) body.get("distanciaKm")).doubleValue();
                int tempoMinutos = ((Number) body.get("tempoMinutos")).intValue();

                // Calcula taxa baseada na distância
                BigDecimal taxaEntrega = calcularTaxaEntrega(distanciaKm);

                // Adiciona tempo de preparo
                int tempoPreparo = restaurante.getTempoMedioEntrega() != null ? 
                    restaurante.getTempoMedioEntrega() : 30;
                int tempoTotal = tempoPreparo + tempoMinutos;

                return new CalcularEntregaResponse(
                    Math.round(distanciaKm * 100.0) / 100.0,
                    tempoTotal,
                    taxaEntrega,
                    formatarEnderecoRestaurante(enderecoRestaurante),
                    "Endereço de entrega"
                );
            }

            throw APIException.build(HttpStatus.BAD_GATEWAY, "Resposta inválida da API de Entrega");

        } catch (RestClientException e) {
            log.error("Erro ao chamar API de Entrega: {}", e.getMessage());
            throw APIException.build(HttpStatus.SERVICE_UNAVAILABLE, "Erro ao calcular entrega via API", e);
        }
    }

    /**
     * Calcula entrega localmente usando fórmula de Haversine.
     */
    private CalcularEntregaResponse calcularLocal(
            Restaurante restaurante,
            EnderecoRestaurante enderecoRestaurante,
            CalcularEntregaRequest request) {

        log.info("Usando cálculo local (Haversine)");

        // Calcula distância usando fórmula de Haversine
        Double distanciaKm = calcularDistancia(
            enderecoRestaurante.getLatitude().doubleValue(),
            enderecoRestaurante.getLongitude().doubleValue(),
            request.latitude(),
            request.longitude()
        );

        // Calcula tempo estimado
        int tempoPreparo = restaurante.getTempoMedioEntrega() != null ? 
            restaurante.getTempoMedioEntrega() : 30;
        int tempoDeslocamento = calcularTempoDeslocamento(distanciaKm);
        int tempoTotal = tempoPreparo + tempoDeslocamento;

        // Calcula taxa de entrega
        BigDecimal taxaEntrega = calcularTaxaEntrega(distanciaKm);

        return new CalcularEntregaResponse(
            Math.round(distanciaKm * 100.0) / 100.0,
            tempoTotal,
            taxaEntrega,
            formatarEnderecoRestaurante(enderecoRestaurante),
            "Endereço de entrega"
        );
    }

    /**
     * Calcula a distância entre dois pontos usando a fórmula de Haversine.
     */
    private Double calcularDistancia(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int RAIO_TERRA_KM = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RAIO_TERRA_KM * c;
    }

    /**
     * Calcula o tempo de deslocamento baseado na distância.
     * Considera velocidade média de 20 km/h em área urbana.
     */
    private Integer calcularTempoDeslocamento(Double distanciaKm) {
        double velocidadeMediaKmH = 20.0;
        double tempoHoras = distanciaKm / velocidadeMediaKmH;
        int tempoMinutos = (int) Math.ceil(tempoHoras * 60);

        // Mínimo de 10 minutos
        return Math.max(tempoMinutos, 10);
    }

    /**
     * Calcula a taxa de entrega baseada na distância.
     * Taxa base de R$ 5,00 + R$ 1,50 por km.
     */
    private BigDecimal calcularTaxaEntrega(Double distanciaKm) {
        BigDecimal taxaBase = BigDecimal.valueOf(5.00);
        BigDecimal valorPorKm = BigDecimal.valueOf(1.50);

        BigDecimal taxa = taxaBase.add(
            valorPorKm.multiply(BigDecimal.valueOf(distanciaKm))
        );

        // Arredondar para 2 casas decimais
        return taxa.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Formata o endereço do restaurante para exibição.
     */
    private String formatarEnderecoRestaurante(EnderecoRestaurante endereco) {
        return String.format("%s, %s - %s",
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getBairro()
        );
    }
}
