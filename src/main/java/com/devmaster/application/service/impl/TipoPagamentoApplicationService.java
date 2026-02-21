package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.TipoPagamentoRequest;
import com.devmaster.application.api.response.TipoPagamentoResponse;
import com.devmaster.application.service.TipoPagamentoService;
import com.devmaster.domain.TipoPagamento;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.TipoPagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de TipoPagamento.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class TipoPagamentoApplicationService implements TipoPagamentoService {
    
    private final TipoPagamentoRepository tipoPagamentoRepository;
    
    @Override
    @Transactional
    public TipoPagamentoResponse criarTipoPagamento(UUID usuarioId, TipoPagamentoRequest request) {
        if (tipoPagamentoRepository.existsByCodigo(request.codigo())) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe um tipo de pagamento com este código");
        }
        
        TipoPagamento tipo = TipoPagamento.builder()
            .nome(request.nome())
            .codigo(request.codigo())
            .descricao(request.descricao())
            .iconeUrl(request.iconeUrl())
            .ativo(true)
            .requerTroco(request.requerTroco() != null ? request.requerTroco() : false)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        tipo = tipoPagamentoRepository.save(tipo);
        return TipoPagamentoResponse.from(tipo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TipoPagamentoResponse buscarTipoPagamento(UUID usuarioId, Long id) {
        TipoPagamento tipo = buscarTipoPagamentoOuFalhar(id);
        return TipoPagamentoResponse.from(tipo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TipoPagamentoResponse> listarTiposPagamento(UUID usuarioId, Boolean ativo) {
        List<TipoPagamento> tipos = ativo != null
            ? tipoPagamentoRepository.findByAtivoOrderByOrdemExibicao(ativo)
            : tipoPagamentoRepository.findAll();
        
        return tipos.stream()
            .map(TipoPagamentoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public TipoPagamentoResponse atualizarTipoPagamento(UUID usuarioId, Long id, TipoPagamentoRequest request) {
        TipoPagamento tipo = buscarTipoPagamentoOuFalhar(id);
        
        if (request.nome() != null) {
            tipo.setNome(request.nome());
        }
        
        if (request.codigo() != null && !request.codigo().equals(tipo.getCodigo())) {
            if (tipoPagamentoRepository.existsByCodigo(request.codigo())) {
                throw APIException.build(HttpStatus.CONFLICT, "Já existe um tipo de pagamento com este código");
            }
            tipo.setCodigo(request.codigo());
        }
        
        if (request.descricao() != null) {
            tipo.setDescricao(request.descricao());
        }
        
        if (request.iconeUrl() != null) {
            tipo.setIconeUrl(request.iconeUrl());
        }
        
        if (request.requerTroco() != null) {
            tipo.setRequerTroco(request.requerTroco());
        }
        
        if (request.ordemExibicao() != null) {
            tipo.setOrdemExibicao(request.ordemExibicao());
        }
        
        tipo = tipoPagamentoRepository.save(tipo);
        return TipoPagamentoResponse.from(tipo);
    }
    
    @Override
    @Transactional
    public void ativarTipoPagamento(UUID usuarioId, Long id) {
        TipoPagamento tipo = buscarTipoPagamentoOuFalhar(id);
        tipo.ativar();
        tipoPagamentoRepository.save(tipo);
    }
    
    @Override
    @Transactional
    public void desativarTipoPagamento(UUID usuarioId, Long id) {
        TipoPagamento tipo = buscarTipoPagamentoOuFalhar(id);
        tipo.desativar();
        tipoPagamentoRepository.save(tipo);
    }
    
    @Override
    @Transactional
    public void removerTipoPagamento(UUID usuarioId, Long id) {
        TipoPagamento tipo = buscarTipoPagamentoOuFalhar(id);
        tipoPagamentoRepository.delete(tipo);
    }
    
    private TipoPagamento buscarTipoPagamentoOuFalhar(Long id) {
        return tipoPagamentoRepository.findById(id)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Tipo de pagamento não encontrado"));
    }
}
