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
import java.util.UUID;

/**
 * Interface de serviço para operações relacionadas a Entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface EntregadorService {
    
    /**
     * Cria um novo entregador.
     * 
     * @param usuarioId ID do usuário que está criando (para auditoria)
     * @param request Dados do entregador
     * @return Entregador criado
     */
    EntregadorResponse criarEntregador(UUID usuarioId, EntregadorRequest request);
    
    /**
     * Busca entregador por ID.
     * 
     * @param usuarioId ID do usuário que está buscando
     * @param entregadorId ID do entregador
     * @return Dados do entregador
     */
    EntregadorResponse buscarEntregador(UUID usuarioId, Long entregadorId);
    
    /**
     * Lista todos os entregadores com filtros e paginação.
     * 
     * @param usuarioId ID do usuário que está listando
     * @param ativo Filtro por status ativo (opcional)
     * @param disponivel Filtro por disponibilidade (opcional)
     * @param tipoVeiculo Filtro por tipo de veículo (opcional)
     * @param pageable Configuração de paginação
     * @return Página de entregadores
     */
    Page<EntregadorResumoResponse> listarEntregadores(
        UUID usuarioId,
        Boolean ativo,
        Boolean disponivel,
        TipoVeiculo tipoVeiculo,
        Pageable pageable
    );
    
    /**
     * Atualiza dados de um entregador.
     * 
     * @param usuarioId ID do usuário que está atualizando
     * @param entregadorId ID do entregador
     * @param request Dados a atualizar
     * @return Entregador atualizado
     */
    EntregadorResponse atualizarEntregador(
        UUID usuarioId,
        Long entregadorId,
        AtualizarEntregadorRequest request
    );
    
    /**
     * Desativa um entregador.
     * 
     * @param usuarioId ID do usuário que está desativando
     * @param entregadorId ID do entregador
     */
    void desativarEntregador(UUID usuarioId, Long entregadorId);
    
    /**
     * Reativa um entregador.
     * 
     * @param usuarioId ID do usuário que está reativando
     * @param entregadorId ID do entregador
     */
    void reativarEntregador(UUID usuarioId, Long entregadorId);
    
    /**
     * Altera disponibilidade do entregador.
     * 
     * @param usuarioId ID do usuário que está alterando
     * @param entregadorId ID do entregador
     * @param request Dados de disponibilidade
     */
    void alterarDisponibilidade(
        UUID usuarioId,
        Long entregadorId,
        AlterarDisponibilidadeRequest request
    );
    
    /**
     * Busca entregadores disponíveis próximos a uma localização.
     * 
     * @param usuarioId ID do usuário que está buscando
     * @param latitude Latitude de referência
     * @param longitude Longitude de referência
     * @param raioKm Raio de busca em km
     * @return Lista de entregadores próximos
     */
    List<EntregadorResumoResponse> buscarEntregadoresDisponiveisProximos(
        UUID usuarioId,
        Double latitude,
        Double longitude,
        Double raioKm
    );
    
    /**
     * Obtém estatísticas de um entregador.
     * 
     * @param usuarioId ID do usuário que está consultando
     * @param entregadorId ID do entregador
     * @return Estatísticas do entregador
     */
    EstatisticasEntregadorResponse obterEstatisticas(UUID usuarioId, Long entregadorId);
    
    /**
     * Valida existência e disponibilidade do entregador.
     * Usado para integração com módulo ENTREGA.
     * 
     * @param entregadorId ID do entregador
     * @return Dados resumidos do entregador
     */
    EntregadorResumoResponse validarEntregador(Long entregadorId);
    
    /**
     * Notifica atribuição de entrega ao entregador.
     * Atualiza estatísticas e histórico.
     * 
     * @param entregadorId ID do entregador
     * @param entregaId ID da entrega
     */
    void notificarAtribuicaoEntrega(Long entregadorId, Long entregaId);
    
    /**
     * Notifica finalização de entrega.
     * Atualiza estatísticas e avaliação.
     * 
     * @param entregadorId ID do entregador
     * @param entregaId ID da entrega
     * @param avaliacao Avaliação recebida (1-5)
     * @param tempoEntregaMinutos Tempo de entrega em minutos
     */
    void notificarFinalizacaoEntrega(
        Long entregadorId,
        Long entregaId,
        Integer avaliacao,
        Integer tempoEntregaMinutos
    );
}
