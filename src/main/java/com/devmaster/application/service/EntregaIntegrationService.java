package com.devmaster.application.service;

import com.devmaster.application.api.request.CalcularEntregaRequest;
import com.devmaster.application.api.response.CalcularEntregaResponse;

/**
 * Serviço de integração com a API de Entrega.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface EntregaIntegrationService {
    
    /**
     * Calcula distância, tempo e taxa de entrega usando a API de Entrega.
     * 
     * @param request Dados para cálculo
     * @return Informações calculadas de entrega
     */
    CalcularEntregaResponse calcularEntrega(CalcularEntregaRequest request);
    
    /**
     * Verifica se a API de Entrega está disponível.
     * 
     * @return true se disponível, false caso contrário
     */
    boolean isApiDisponivel();
}
