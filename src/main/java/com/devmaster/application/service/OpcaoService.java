package com.devmaster.application.service;

import com.devmaster.application.api.request.OpcaoRequest;
import com.devmaster.application.api.response.OpcaoResponse;

import java.util.List;
import java.util.UUID;

public interface OpcaoService {
    OpcaoResponse adicionarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, OpcaoRequest request);
    List<OpcaoResponse> listarOpcoes(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Boolean disponivel);
    OpcaoResponse atualizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId, OpcaoRequest request);
    void disponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId);
    void indisponibilizarOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId);
    void removerOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, Long opcaoId);
}
