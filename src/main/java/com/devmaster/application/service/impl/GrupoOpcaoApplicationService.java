package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.GrupoOpcaoRequest;
import com.devmaster.application.api.response.GrupoOpcaoResponse;
import com.devmaster.application.api.response.OpcaoResponse;
import com.devmaster.application.service.GrupoOpcaoService;
import com.devmaster.domain.GrupoOpcao;
import com.devmaster.domain.Produto;
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
public class GrupoOpcaoApplicationService implements GrupoOpcaoService {

    private final GrupoOpcaoRepository grupoOpcaoRepository;
    private final OpcaoRepository opcaoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public GrupoOpcaoResponse adicionarGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, GrupoOpcaoRequest request) {
        Produto produto = buscarProdutoOuFalhar(restauranteId, produtoId);

        GrupoOpcao grupo = GrupoOpcao.builder()
                .produto(produto)
                .nome(request.nome())
                .descricao(request.descricao())
                .minimoSelecoes(request.minimoSelecoes() != null ? request.minimoSelecoes() : 0)
                .maximoSelecoes(request.maximoSelecoes() != null ? request.maximoSelecoes() : 1)
                .obrigatorio(request.obrigatorio() != null && request.obrigatorio())
                .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
                .build();

        grupo = grupoOpcaoRepository.save(grupo);
        return GrupoOpcaoResponse.from(grupo, List.of());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GrupoOpcaoResponse> listarGruposOpcoes(UUID usuarioId, Long restauranteId, Long produtoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);

        return grupoOpcaoRepository.findByProdutoIdOrderByOrdemExibicao(produtoId)
                .stream()
                .map(grupo -> {
                    List<OpcaoResponse> opcoes = opcaoRepository
                            .findByGrupoOpcaoIdOrderByOrdemExibicao(grupo.getId())
                            .stream()
                            .map(OpcaoResponse::from)
                            .toList();
                    return GrupoOpcaoResponse.from(grupo, opcoes);
                })
                .toList();
    }

    @Override
    @Transactional
    public GrupoOpcaoResponse atualizarGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, GrupoOpcaoRequest request) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(produtoId, grupoId);

        if (request.nome() != null) {
            grupo.setNome(request.nome());
        }

        if (request.descricao() != null) {
            grupo.setDescricao(request.descricao());
        }

        if (request.minimoSelecoes() != null) {
            grupo.setMinimoSelecoes(request.minimoSelecoes());
        }

        if (request.maximoSelecoes() != null) {
            grupo.setMaximoSelecoes(request.maximoSelecoes());
        }

        if (request.obrigatorio() != null) {
            grupo.setObrigatorio(request.obrigatorio());
        }

        if (request.ordemExibicao() != null) {
            grupo.setOrdemExibicao(request.ordemExibicao());
        }

        grupo = grupoOpcaoRepository.save(grupo);

        List<OpcaoResponse> opcoes = opcaoRepository
                .findByGrupoOpcaoIdOrderByOrdemExibicao(grupoId)
                .stream()
                .map(OpcaoResponse::from)
                .toList();

        return GrupoOpcaoResponse.from(grupo, opcoes);
    }

    @Override
    @Transactional
    public void removerGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId) {
        buscarProdutoOuFalhar(restauranteId, produtoId);
        GrupoOpcao grupo = buscarGrupoOpcaoOuFalhar(produtoId, grupoId);
        grupoOpcaoRepository.delete(grupo);
    }

    private Produto buscarProdutoOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findByIdAndRestauranteId(produtoId, restauranteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Produto não encontrado"));
    }

    private GrupoOpcao buscarGrupoOpcaoOuFalhar(Long produtoId, Long grupoId) {
        return grupoOpcaoRepository.findByIdAndProdutoId(grupoId, produtoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Grupo de opção não encontrado"));
    }
}
