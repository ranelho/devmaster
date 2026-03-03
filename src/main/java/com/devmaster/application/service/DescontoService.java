package com.devmaster.application.service;

import com.devmaster.application.api.request.DescontoRequest;
import com.devmaster.application.api.response.DescontoResponse;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Desconto.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface DescontoService {
    
    DescontoResponse criar(UUID usuarioId, DescontoRequest request);
    
    DescontoResponse atualizar(UUID usuarioId, Long id, DescontoRequest request);
    
    DescontoResponse buscarPorId(UUID usuarioId, Long id);
    
    List<DescontoResponse> listarTodos(UUID usuarioId);
    
    List<DescontoResponse> listarPorProduto(UUID usuarioId, Long produtoId);
    
    List<DescontoResponse> listarVigentes(UUID usuarioId);
    
    void deletar(UUID usuarioId, Long id);
}
