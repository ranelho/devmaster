package com.devmaster.application.service;

import com.devmaster.application.api.request.GrupoOpcaoRequest;
import com.devmaster.application.api.response.GrupoOpcaoResponse;

import java.util.List;
import java.util.UUID;

public interface GrupoOpcaoService {
    GrupoOpcaoResponse adicionarGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, GrupoOpcaoRequest request);
    List<GrupoOpcaoResponse> listarGruposOpcoes(UUID usuarioId, Long restauranteId, Long produtoId);
    GrupoOpcaoResponse atualizarGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId, GrupoOpcaoRequest request);
    void removerGrupoOpcao(UUID usuarioId, Long restauranteId, Long produtoId, Long grupoId);
}
