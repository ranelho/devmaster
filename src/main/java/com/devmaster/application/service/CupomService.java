package com.devmaster.application.service;

import com.devmaster.application.api.request.AtualizarCupomRequest;
import com.devmaster.application.api.request.CupomRequest;
import com.devmaster.application.api.request.ValidarCupomRequest;
import com.devmaster.application.api.response.CupomResponse;
import com.devmaster.application.api.response.ValidacaoCupomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para Cupom.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface CupomService {
    
    CupomResponse criarCupom(UUID usuarioId, CupomRequest request);
    
    CupomResponse buscarCupom(UUID usuarioId, Long cupomId);
    
    CupomResponse buscarCupomPorCodigo(UUID usuarioId, String codigo);
    
    Page<CupomResponse> listarCupons(UUID usuarioId, Boolean ativo, Boolean validos, Pageable pageable);
    
    List<CupomResponse> listarCuponsValidos(UUID usuarioId);
    
    CupomResponse atualizarCupom(UUID usuarioId, Long cupomId, AtualizarCupomRequest request);
    
    void ativarCupom(UUID usuarioId, Long cupomId);
    
    void desativarCupom(UUID usuarioId, Long cupomId);
    
    void removerCupom(UUID usuarioId, Long cupomId);
    
    ValidacaoCupomResponse validarCupom(UUID usuarioId, ValidarCupomRequest request);
}
