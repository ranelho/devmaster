package com.devmaster.application.api;

import com.devmaster.application.api.request.DescontoRequest;
import com.devmaster.application.api.response.DescontoResponse;
import com.devmaster.application.service.DescontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DescontoController implements DescontoAPI {
    
    private final DescontoService descontoService;
    
    @Override
    public DescontoResponse criar(DescontoRequest request) {
        return descontoService.criar(null, request);
    }
    
    @Override
    public DescontoResponse atualizar(Long id, DescontoRequest request) {
        return descontoService.atualizar(null, id, request);
    }
    
    @Override
    public DescontoResponse buscarPorId(Long id) {
        return descontoService.buscarPorId(null, id);
    }
    
    @Override
    public List<DescontoResponse> listarTodos() {
        return descontoService.listarTodos(null);
    }
    
    @Override
    public List<DescontoResponse> listarPorProduto(Long produtoId) {
        return descontoService.listarPorProduto(null, produtoId);
    }
    
    @Override
    public List<DescontoResponse> listarVigentes() {
        return descontoService.listarVigentes(null);
    }
    
    @Override
    public void deletar(Long id) {
        descontoService.deletar(null, id);
    }
}
