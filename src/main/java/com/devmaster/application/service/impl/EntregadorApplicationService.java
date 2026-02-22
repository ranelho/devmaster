package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AlterarDisponibilidadeRequest;
import com.devmaster.application.api.request.AtualizarEntregadorRequest;
import com.devmaster.application.api.request.EntregadorRequest;
import com.devmaster.application.api.response.EntregadorResponse;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.EstatisticasEntregadorResponse;
import com.devmaster.application.service.EntregadorService;
import com.devmaster.domain.Entregador;
import com.devmaster.domain.HistoricoDisponibilidadeEntregador;
import com.devmaster.domain.enums.TipoVeiculo;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.DocumentoEntregadorRepository;
import com.devmaster.infrastructure.repository.EntregadorRepository;
import com.devmaster.infrastructure.repository.HistoricoDisponibilidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Entregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class EntregadorApplicationService implements EntregadorService {
    
    private final EntregadorRepository entregadorRepository;
    private final HistoricoDisponibilidadeRepository historicoRepository;
    private final DocumentoEntregadorRepository documentoRepository;
    
    @Override
    @Transactional
    public EntregadorResponse criarEntregador(EntregadorRequest request) {
        // Validar duplicação de CPF
        if (entregadorRepository.existsByCpf(request.getCpf())) {
            throw APIException.build(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        
        // Validar duplicação de telefone
        if (entregadorRepository.existsByTelefone(request.getTelefone())) {
            throw APIException.build(HttpStatus.CONFLICT, "Telefone já cadastrado");
        }
        
        // Validar duplicação de email
        if (request.getEmail() != null && entregadorRepository.existsByEmail(request.getEmail())) {
            throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        
        // Validar duplicação de CNH
        if (request.getCnh() != null && entregadorRepository.existsByCnh(request.getCnh())) {
            throw APIException.build(HttpStatus.CONFLICT, "CNH já cadastrada");
        }
        
        // Criar entidade
        Entregador entregador = Entregador.builder()
            .nomeCompleto(request.getNomeCompleto())
            .telefone(request.getTelefone())
            .email(request.getEmail())
            .cpf(request.getCpf())
            .cnh(request.getCnh())
            .categoriaCnh(request.getCategoriaCnh())
            .tipoVeiculo(request.getTipoVeiculo())
            .placaVeiculo(request.getPlacaVeiculo())
            .modeloVeiculo(request.getModeloVeiculo())
            .corVeiculo(request.getCorVeiculo())
            .fotoPerfilUrl(request.getFotoPerfilUrl())
            .fotoCnhUrl(request.getFotoCnhUrl())
            .fotoVeiculoUrl(request.getFotoVeiculoUrl())
            .ativo(true)
            .disponivel(false)
            .avaliacao(BigDecimal.ZERO)
            .totalEntregas(0)
            .build();
        
        entregador = entregadorRepository.save(entregador);
        
        return EntregadorResponse.from(entregador);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EntregadorResponse buscarEntregador(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        return EntregadorResponse.from(entregador);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EntregadorResumoResponse> listarEntregadores(
        Boolean ativo,
        Boolean disponivel,
        TipoVeiculo tipoVeiculo,
        Pageable pageable
    ) {
        Page<Entregador> entregadores;
        
        if (ativo != null && disponivel != null && tipoVeiculo != null) {
            entregadores = entregadorRepository.findByAtivoAndDisponivelAndTipoVeiculo(
                ativo, disponivel, tipoVeiculo, pageable
            );
        } else if (ativo != null && disponivel != null) {
            entregadores = entregadorRepository.findByAtivoAndDisponivel(ativo, disponivel, pageable);
        } else if (ativo != null) {
            entregadores = entregadorRepository.findByAtivo(ativo, pageable);
        } else if (disponivel != null) {
            entregadores = entregadorRepository.findByDisponivel(disponivel, pageable);
        } else if (tipoVeiculo != null) {
            entregadores = entregadorRepository.findByTipoVeiculo(tipoVeiculo, pageable);
        } else {
            entregadores = entregadorRepository.findAll(pageable);
        }
        
        return entregadores.map(EntregadorResumoResponse::from);
    }
    
    @Override
    @Transactional
    public EntregadorResponse atualizarEntregador(
        Long entregadorId,
        AtualizarEntregadorRequest request
    ) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        // Atualizar campos se fornecidos
        if (request.getNomeCompleto() != null) {
            entregador.setNomeCompleto(request.getNomeCompleto());
        }
        
        if (request.getTelefone() != null) {
            // Validar duplicação
            if (!request.getTelefone().equals(entregador.getTelefone()) && 
                entregadorRepository.existsByTelefone(request.getTelefone())) {
                throw APIException.build(HttpStatus.CONFLICT, "Telefone já cadastrado");
            }
            entregador.setTelefone(request.getTelefone());
        }
        
        if (request.getEmail() != null) {
            // Validar duplicação
            if (!request.getEmail().equals(entregador.getEmail()) && 
                entregadorRepository.existsByEmail(request.getEmail())) {
                throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
            }
            entregador.setEmail(request.getEmail());
        }
        
        if (request.getCnh() != null) {
            // Validar duplicação
            if (!request.getCnh().equals(entregador.getCnh()) && 
                entregadorRepository.existsByCnh(request.getCnh())) {
                throw APIException.build(HttpStatus.CONFLICT, "CNH já cadastrada");
            }
            entregador.setCnh(request.getCnh());
        }
        
        if (request.getCategoriaCnh() != null) {
            entregador.setCategoriaCnh(request.getCategoriaCnh());
        }
        
        if (request.getTipoVeiculo() != null) {
            entregador.setTipoVeiculo(request.getTipoVeiculo());
        }
        
        if (request.getPlacaVeiculo() != null) {
            entregador.setPlacaVeiculo(request.getPlacaVeiculo());
        }
        
        if (request.getModeloVeiculo() != null) {
            entregador.setModeloVeiculo(request.getModeloVeiculo());
        }
        
        if (request.getCorVeiculo() != null) {
            entregador.setCorVeiculo(request.getCorVeiculo());
        }
        
        if (request.getFotoPerfilUrl() != null) {
            entregador.setFotoPerfilUrl(request.getFotoPerfilUrl());
        }
        
        if (request.getFotoCnhUrl() != null) {
            entregador.setFotoCnhUrl(request.getFotoCnhUrl());
        }
        
        if (request.getFotoVeiculoUrl() != null) {
            entregador.setFotoVeiculoUrl(request.getFotoVeiculoUrl());
        }
        
        entregador = entregadorRepository.save(entregador);
        
        return EntregadorResponse.from(entregador);
    }
    
    @Override
    @Transactional
    public void desativarEntregador(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        entregador.desativar();
        entregadorRepository.save(entregador);
        
        // Registrar mudança de disponibilidade
        registrarHistoricoDisponibilidade(entregador, false, null, null);
    }
    
    @Override
    @Transactional
    public void reativarEntregador(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        entregador.reativar();
        entregadorRepository.save(entregador);
    }
    
    @Override
    @Transactional
    public void alterarDisponibilidade(
        Long entregadorId,
        AlterarDisponibilidadeRequest request
    ) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        if (!entregador.getAtivo()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Entregador inativo não pode alterar disponibilidade");
        }
        
        entregador.alterarDisponibilidade(request.getDisponivel());
        entregadorRepository.save(entregador);
        
        // Registrar histórico
        registrarHistoricoDisponibilidade(
            entregador, 
            request.getDisponivel(), 
            request.getLatitude(), 
            request.getLongitude()
        );
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EntregadorResumoResponse> buscarEntregadoresDisponiveisProximos(
        Double latitude,
        Double longitude,
        Double raioKm
    ) {
        List<Entregador> entregadores = entregadorRepository.findEntregadoresDisponiveisProximos(
            latitude, longitude, raioKm
        );
        
        return entregadores.stream()
            .map(EntregadorResumoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public EstatisticasEntregadorResponse obterEstatisticas(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        // Contar documentos
        long totalDocumentos = documentoRepository.countByEntregadorId(entregadorId);
        long documentosVerificados = documentoRepository.countByEntregadorIdAndVerificado(entregadorId, true);
        long documentosPendentes = totalDocumentos - documentosVerificados;
        
        // Documentos vencidos
        List<Long> documentosVencidos = documentoRepository.findDocumentosVencidos(LocalDate.now())
            .stream()
            .filter(d -> d.getEntregador().getId().equals(entregadorId))
            .map(d -> d.getId())
            .collect(Collectors.toList());
        
        // Histórico de disponibilidade
        long totalMudancas = historicoRepository.countByEntregadorId(entregadorId);
        
        return EstatisticasEntregadorResponse.builder()
            .entregadorId(entregador.getId())
            .nomeCompleto(entregador.getNomeCompleto())
            .totalEntregas(entregador.getTotalEntregas())
            .avaliacaoMedia(entregador.getAvaliacao())
            .taxaSucesso(BigDecimal.valueOf(98.5)) // TODO: Calcular baseado em entregas
            .tempoMedioEntregaMinutos(25) // TODO: Calcular baseado em entregas
            .entregasUltimos30Dias(0) // TODO: Calcular baseado em entregas
            .totalMudancasDisponibilidade((int) totalMudancas)
            .documentosVerificados((int) documentosVerificados)
            .documentosPendentes((int) documentosPendentes)
            .documentosVencidos(documentosVencidos.size())
            .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public EntregadorResumoResponse validarEntregador(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        return EntregadorResumoResponse.from(entregador);
    }
    
    @Override
    @Transactional
    public void notificarAtribuicaoEntrega(Long entregadorId, Long entregaId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        // Marcar como indisponível
        entregador.alterarDisponibilidade(false);
        entregadorRepository.save(entregador);
        
        // Registrar histórico
        registrarHistoricoDisponibilidade(entregador, false, null, null);
    }
    
    @Override
    @Transactional
    public void notificarFinalizacaoEntrega(
        Long entregadorId,
        Long entregaId,
        Integer avaliacao,
        Integer tempoEntregaMinutos
    ) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
        
        // Incrementar total de entregas
        entregador.incrementarTotalEntregas();
        
        // Atualizar avaliação
        if (avaliacao != null && avaliacao >= 1 && avaliacao <= 5) {
            entregador.atualizarAvaliacao(BigDecimal.valueOf(avaliacao));
        }
        
        entregadorRepository.save(entregador);
    }
    
    // Métodos auxiliares
    
    private void registrarHistoricoDisponibilidade(
        Entregador entregador,
        Boolean disponivel,
        BigDecimal latitude,
        BigDecimal longitude
    ) {
        HistoricoDisponibilidadeEntregador historico = HistoricoDisponibilidadeEntregador.builder()
            .entregador(entregador)
            .disponivel(disponivel)
            .latitude(latitude)
            .longitude(longitude)
            .build();
        
        historicoRepository.save(historico);
    }
}
