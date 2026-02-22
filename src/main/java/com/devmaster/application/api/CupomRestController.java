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
    public CupomResponse criarCupom(CupomRequest request) {
        return cupomService.criarCupom(null, request);
    }
    
    @Override
    public CupomResponse buscarCupom(Long cupomId) {
        return cupomService.buscarCupom(null, cupomId);
    }
    
    @Override
    public CupomResponse buscarCupomPorCodigo(String codigo) {
        return cupomService.buscarCupomPorCodigo(null, codigo);
    }
    
    @Override
    public Page<CupomResponse> listarCupons(Boolean ativo, Boolean validos, Pageable pageable) {
        return cupomService.listarCupons(null, ativo, validos, pageable);
    }
    
    @Override
    public List<CupomResponse> listarCuponsValidos() {
        return cupomService.listarCuponsValidos(null);
    }
    
    @Override
    public CupomResponse atualizarCupom(Long cupomId, AtualizarCupomRequest request) {
        return cupomService.atualizarCupom(null, cupomId, request);
    }
    
    @Override
    public void ativarCupom(Long cupomId) {
        cupomService.ativarCupom(null, cupomId);
    }
    
    @Override
    public void desativarCupom(Long cupomId) {
        cupomService.desativarCupom(null, cupomId);
    }
    
    @Override
    public void removerCupom(Long cupomId) {
        cupomService.removerCupom(null, cupomId);
    }
    
    @Override
    public ValidacaoCupomResponse validarCupom(ValidarCupomRequest request) {
        return cupomService.validarCupom(null, request);
    }
}
