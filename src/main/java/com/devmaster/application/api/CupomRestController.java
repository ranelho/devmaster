package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarCupomRequest;
import com.devmaster.application.api.request.CupomRequest;
import com.devmaster.application.api.request.ValidarCupomRequest;
import com.devmaster.application.api.response.CupomResponse;
import com.devmaster.application.api.response.ValidacaoCupomResponse;
import com.devmaster.application.service.CupomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para Cupom.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class CupomRestController implements CupomAPI {
    
    private final CupomService cupomService;
    
    @Override
    public CupomResponse criarCupom(UUID usuarioId, CupomRequest request) {
        return cupomService.criarCupom(usuarioId, request);
    }
    
    @Override
    public CupomResponse buscarCupom(UUID usuarioId, Long cupomId) {
        return cupomService.buscarCupom(usuarioId, cupomId);
    }
    
    @Override
    public CupomResponse buscarCupomPorCodigo(UUID usuarioId, String codigo) {
        return cupomService.buscarCupomPorCodigo(usuarioId, codigo);
    }
    
    @Override
    public Page<CupomResponse> listarCupons(UUID usuarioId, Boolean ativo, Boolean validos, Pageable pageable) {
        return cupomService.listarCupons(usuarioId, ativo, validos, pageable);
    }
    
    @Override
    public List<CupomResponse> listarCuponsValidos(UUID usuarioId) {
        return cupomService.listarCuponsValidos(usuarioId);
    }
    
    @Override
    public CupomResponse atualizarCupom(UUID usuarioId, Long cupomId, AtualizarCupomRequest request) {
        return cupomService.atualizarCupom(usuarioId, cupomId, request);
    }
    
    @Override
    public void ativarCupom(UUID usuarioId, Long cupomId) {
        cupomService.ativarCupom(usuarioId, cupomId);
    }
    
    @Override
    public void desativarCupom(UUID usuarioId, Long cupomId) {
        cupomService.desativarCupom(usuarioId, cupomId);
    }
    
    @Override
    public void removerCupom(UUID usuarioId, Long cupomId) {
        cupomService.removerCupom(usuarioId, cupomId);
    }
    
    @Override
    public ValidacaoCupomResponse validarCupom(UUID usuarioId, ValidarCupomRequest request) {
        return cupomService.validarCupom(usuarioId, request);
    }
}
