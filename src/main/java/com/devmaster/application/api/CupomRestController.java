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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<CupomResponse> criarCupom(CupomRequest request) {
        CupomResponse response = cupomService.criarCupom(null, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<CupomResponse> buscarCupom(Long cupomId) {
        CupomResponse response = cupomService.buscarCupom(null, cupomId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<CupomResponse> buscarCupomPorCodigo(String codigo) {
        CupomResponse response = cupomService.buscarCupomPorCodigo(null, codigo);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Page<CupomResponse>> listarCupons(Boolean ativo, Boolean validos, Pageable pageable) {
        Page<CupomResponse> response = cupomService.listarCupons(null, ativo, validos, pageable);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<List<CupomResponse>> listarCuponsValidos() {
        List<CupomResponse> response = cupomService.listarCuponsValidos(null);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<CupomResponse> atualizarCupom(Long cupomId, AtualizarCupomRequest request) {
        CupomResponse response = cupomService.atualizarCupom(null, cupomId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> ativarCupom(Long cupomId) {
        cupomService.ativarCupom(null, cupomId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> desativarCupom(Long cupomId) {
        cupomService.desativarCupom(null, cupomId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<Void> removerCupom(Long cupomId) {
        cupomService.removerCupom(null, cupomId);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    public ResponseEntity<ValidacaoCupomResponse> validarCupomPublico(ValidarCupomRequest request) {
        ValidacaoCupomResponse response = cupomService.validarCupom(null, request);
        return ResponseEntity.ok(response);
    }
}
