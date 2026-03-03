package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.OpcaoRequest;
import com.devmaster.application.api.response.OpcaoResponse;
import com.devmaster.application.service.OpcaoService;
import com.devmaster.domain.GrupoOpcao;
import com.devmaster.domain.Opcao;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.GrupoOpcaoRepository;
import com.devmaster.infrastructure.repository.OpcaoRepository;
import com.devmaster.infrastructure.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpcaoApplicationService implements OpcaoService {
    
    private final OpcaoRepository opcaoRepository;
    private final GrupoOpcaoRepository grupoOpcaoRepository;
    private final ProdutoRepository produtoRepository;
    
    @Override
    @Transactional
    public OpcaoResponse adicionarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, OpcaoRequest request) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        
        Opcao opcao = Opcao.builder()
            .grupoOpcao(grupo)
            .nome(request.nome())
            .precoAdicional(request.precoAdicional() != null ? request.precoAdicional() : java.math.BigDecimal.ZERO)
            .disponivel(request.disponivel() != null ? request.disponivel() : true)
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .build();
        
        opcao = opcaoRepository.save(opcao);
        return OpcaoResponse.from(opcao);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OpcaoResponse> listarOpcoes(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Boolean disponivel) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        
        List<Opcao> opcoes = disponivel != null
            ? opcaoRepository.findByGrupoOpcaoIdAndDisponivelOrderByOrdemExibicao(grupoId, disponivel)
            : opcaoRepository.findByGrupoOpcaoIdOrderByOrdemExibicao(grupoId);
        
        return opcoes.stream()
            .map(OpcaoResponse::from)
            .toList();
    }
    
    @Override
    @Transactional
    public OpcaoResponse atualizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId, OpcaoRequest request) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        
        if (request.nome() != null) {
            opcao.setNome(request.nome());
        }
        
        if (request.precoAdicional() != null) {
            opcao.setPrecoAdicional(request.precoAdicional());
        }
        
        if (request.disponivel() != null) {
            opcao.setDisponivel(request.disponivel());
        }
        
        if (request.ordemExibicao() != null) {
            opcao.setOrdemExibicao(request.ordemExibicao());
        }
        
        opcao = opcaoRepository.save(opcao);
        return OpcaoResponse.from(opcao);
    }
    
    @Override
    @Transactional
    public void disponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        opcao.disponibilizar();
        opcaoRepository.save(opcao);
    }
    
    @Override
    @Transactional
    public void indisponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        opcao.indisponibilizar();
        opcaoRepository.save(opcao);
    }
    
    @Override
    @Transactional
    public void removerOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        Opcao opcao = buscarOpcaoOuFalhar(grupoId, opcaoId);
        opcaoRepository.delete(opcao);
    }
    
    private void buscarProdutoOuFalhar(Long restauranteId, Long produtoId) {
        produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }
    
    private GrupoOpcao buscarGrupoOpcaoOuFalhar(Long produtoId, Long grupoId) {
        return grupoOpcaoRepository.findByIdAndProdutoId(grupoId, produtoId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Grupo de opção não encontrado"));
    }
    
    private Opcao buscarOpcaoOuFalhar(Long grupoId, Long opcaoId) {
        return opcaoRepository.findByIdAndGrupoOpcaoId(opcaoId, grupoId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Opção não encontrada"));
    }
}
