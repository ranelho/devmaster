package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.DescontoRequest;
import com.devmaster.application.api.response.DescontoResponse;
import com.devmaster.application.repository.DescontoRepository;
import com.devmaster.application.service.DescontoService;
import com.devmaster.domain.Desconto;
import com.devmaster.domain.Produto;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DescontoApplicationService implements DescontoService {
    
    private final DescontoRepository descontoRepository;
    private final ProdutoRepository produtoRepository;
    
    @Override
    @Transactional
    public DescontoResponse criar(UUID usuarioId, DescontoRequest request) {
        validarPeriodo(request.getDataHoraInicio(), request.getDataHoraFim());
        
        Produto produto = produtoRepository.findById(request.getProdutoId())
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        
        Desconto desconto = Desconto.builder()
            .produto(produto)
            .percentualDesconto(request.getPercentualDesconto())
            .tipoIntervalo(request.getTipoIntervalo())
            .dataHoraInicio(request.getDataHoraInicio())
            .dataHoraFim(request.getDataHoraFim())
            .ativo(request.getAtivo())
            .build();
        
        desconto = descontoRepository.save(desconto);
        log.info("✅ Desconto criado: ID={}, Produto={}, Percentual={}%", 
            desconto.getId(), produto.getNome(), desconto.getPercentualDesconto());
        
        return toResponse(desconto);
    }
    
    @Override
    @Transactional
    public DescontoResponse atualizar(UUID usuarioId, Long id, DescontoRequest request) {
        validarPeriodo(request.getDataHoraInicio(), request.getDataHoraFim());
        
        Desconto desconto = descontoRepository.findById(id)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Desconto não encontrado"));
        
        Produto produto = produtoRepository.findById(request.getProdutoId())
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        
        desconto.setProduto(produto);
        desconto.setPercentualDesconto(request.getPercentualDesconto());
        desconto.setTipoIntervalo(request.getTipoIntervalo());
        desconto.setDataHoraInicio(request.getDataHoraInicio());
        desconto.setDataHoraFim(request.getDataHoraFim());
        desconto.setAtivo(request.getAtivo());
        
        desconto = descontoRepository.save(desconto);
        log.info("🔄 Desconto atualizado: ID={}", id);
        
        return toResponse(desconto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DescontoResponse buscarPorId(UUID usuarioId, Long id) {
        Desconto desconto = descontoRepository.findById(id)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Desconto não encontrado"));
        return toResponse(desconto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DescontoResponse> listarTodos(UUID usuarioId) {
        return descontoRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DescontoResponse> listarPorProduto(UUID usuarioId, Long produtoId) {
        return descontoRepository.findByProdutoId(produtoId).stream()
            .map(this::toResponse)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DescontoResponse> listarVigentes(UUID usuarioId) {
        return descontoRepository.findDescontosVigentes(LocalDateTime.now()).stream()
            .map(this::toResponse)
            .toList();
    }
    
    @Override
    @Transactional
    public void deletar(UUID usuarioId, Long id) {
        if (!descontoRepository.existsById(id)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Desconto não encontrado");
        }
        descontoRepository.deleteById(id);
        log.info("🗑️ Desconto deletado: ID={}", id);
    }
    
    private void validarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        if (fim.isBefore(inicio) || fim.isEqual(inicio)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Data/hora de fim deve ser posterior à data/hora de início");
        }
    }
    
    private DescontoResponse toResponse(Desconto desconto) {
        return DescontoResponse.builder()
            .id(desconto.getId())
            .produtoId(desconto.getProduto().getId())
            .nomeProduto(desconto.getProduto().getNome())
            .percentualDesconto(desconto.getPercentualDesconto())
            .tipoIntervalo(desconto.getTipoIntervalo())
            .dataHoraInicio(desconto.getDataHoraInicio())
            .dataHoraFim(desconto.getDataHoraFim())
            .ativo(desconto.getAtivo())
            .vigente(desconto.isVigente())
            .criadoEm(desconto.getCriadoEm())
            .atualizadoEm(desconto.getAtualizadoEm())
            .build();
    }
}
