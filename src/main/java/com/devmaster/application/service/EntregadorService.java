package com.devmaster.application.service;

import com.devmaster.application.api.request.AlterarDisponibilidadeRequest;
import com.devmaster.application.api.request.AtualizarEntregadorRequest;
import com.devmaster.application.api.request.EntregadorRequest;
import com.devmaster.application.api.response.EntregadorResponse;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.EstatisticasEntregadorResponse;
import com.devmaster.domain.enums.TipoVeiculo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface EntregadorService {
    
    EntregadorResponse criarEntregador(EntregadorRequest request);
    
    EntregadorResponse buscarEntregador(Long entregadorId);
    
    Page<EntregadorResumoResponse> listarEntregadores(
        Boolean ativo,
        Boolean disponivel,
        TipoVeiculo tipoVeiculo,
        Pageable pageable
    );
    
    EntregadorResponse atualizarEntregador(
        Long entregadorId,
        AtualizarEntregadorRequest request
    );
    
    void desativarEntregador(Long entregadorId);
    
    void reativarEntregador(Long entregadorId);
    
    void alterarDisponibilidade(
        Long entregadorId,
        AlterarDisponibilidadeRequest request
    );
    
    List<EntregadorResumoResponse> buscarEntregadoresDisponiveisProximos(
        Double latitude,
        Double longitude,
        Double raioKm
    );
    
    EstatisticasEntregadorResponse obterEstatisticas(Long entregadorId);
    
    EntregadorResumoResponse validarEntregador(Long entregadorId);
    
    void notificarAtribuicaoEntrega(Long entregadorId, Long entregaId);
    
    void notificarFinalizacaoEntrega(
        Long entregadorId,
        Long entregaId,
        Integer avaliacao,
        Integer tempoEntregaMinutos
    );
}
