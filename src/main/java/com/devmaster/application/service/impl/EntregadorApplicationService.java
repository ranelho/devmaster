package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AlterarDisponibilidadeRequest;
import com.devmaster.application.api.request.AtualizarEntregadorRequest;
import com.devmaster.application.api.request.EntregadorRequest;
import com.devmaster.application.api.response.EntregadorResponse;
import com.devmaster.application.api.response.EntregadorResumoResponse;
import com.devmaster.application.api.response.EstatisticasEntregadorResponse;
import com.devmaster.application.service.EntregadorService;
import com.devmaster.domain.DocumentoEntregador;
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

/**
 * Implementação do serviço de Entregador.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class EntregadorApplicationService implements EntregadorService {

    public static final String ENTREGADOR_NAO_ENCONTRADO = "Entregador não encontrado";
    private final EntregadorRepository entregadorRepository;
    private final HistoricoDisponibilidadeRepository historicoRepository;
    private final DocumentoEntregadorRepository documentoRepository;

    @Override
    @Transactional
    public EntregadorResponse criarEntregador(EntregadorRequest request) {
        // Validar duplicação de CPF
        if (entregadorRepository.existsByCpf(request.cpf())) {
            throw APIException.build(HttpStatus.CONFLICT, "CPF já cadastrado");
        }

        // Validar duplicação de telefone
        if (entregadorRepository.existsByTelefone(request.telefone())) {
            throw APIException.build(HttpStatus.CONFLICT, "Telefone já cadastrado");
        }

        // Validar duplicação de email
        if (request.email() != null && entregadorRepository.existsByEmail(request.email())) {
            throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
        }

        // Validar duplicação de CNH
        if (request.cnh() != null && entregadorRepository.existsByCnh(request.cnh())) {
            throw APIException.build(HttpStatus.CONFLICT, "CNH já cadastrada");
        }

        // Criar entidade
        Entregador entregador = Entregador.builder()
                .nomeCompleto(request.nomeCompleto())
                .telefone(request.telefone())
                .email(request.email())
                .cpf(request.cpf())
                .cnh(request.cnh())
                .categoriaCnh(request.categoriaCnh())
                .tipoVeiculo(request.tipoVeiculo())
                .placaVeiculo(request.placaVeiculo())
                .modeloVeiculo(request.modeloVeiculo())
                .corVeiculo(request.corVeiculo())
                .fotoPerfilUrl(request.fotoPerfilUrl())
                .fotoCnhUrl(request.fotoCnhUrl())
                .fotoVeiculoUrl(request.fotoVeiculoUrl())
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
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

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
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

        atualizarDadosPessoais(request, entregador);
        atualizarDadosVeiculo(request, entregador);
        atualizarFotos(request, entregador);

        entregador = entregadorRepository.save(entregador);
        return EntregadorResponse.from(entregador);
    }

    private void atualizarDadosPessoais(AtualizarEntregadorRequest request, Entregador entregador) {
        if (request.nomeCompleto() != null) entregador.setNomeCompleto(request.nomeCompleto());
        if (request.categoriaCnh() != null) entregador.setCategoriaCnh(request.categoriaCnh());

        if (request.telefone() != null) {
            validarDuplicacao(request.telefone(), entregador.getTelefone(),
                    () -> entregadorRepository.existsByTelefone(request.telefone()), "Telefone já cadastrado");
            entregador.setTelefone(request.telefone());
        }

        if (request.email() != null) {
            validarDuplicacao(request.email(), entregador.getEmail(),
                    () -> entregadorRepository.existsByEmail(request.email()), "Email já cadastrado");
            entregador.setEmail(request.email());
        }

        if (request.cnh() != null) {
            validarDuplicacao(request.cnh(), entregador.getCnh(),
                    () -> entregadorRepository.existsByCnh(request.cnh()), "CNH já cadastrada");
            entregador.setCnh(request.cnh());
        }
    }

    private void atualizarDadosVeiculo(AtualizarEntregadorRequest request, Entregador entregador) {
        if (request.tipoVeiculo() != null) entregador.setTipoVeiculo(request.tipoVeiculo());
        if (request.placaVeiculo() != null) entregador.setPlacaVeiculo(request.placaVeiculo());
        if (request.modeloVeiculo() != null) entregador.setModeloVeiculo(request.modeloVeiculo());
        if (request.corVeiculo() != null) entregador.setCorVeiculo(request.corVeiculo());
    }

    private void atualizarFotos(AtualizarEntregadorRequest request, Entregador entregador) {
        if (request.fotoPerfilUrl() != null) entregador.setFotoPerfilUrl(request.fotoPerfilUrl());
        if (request.fotoCnhUrl() != null) entregador.setFotoCnhUrl(request.fotoCnhUrl());
        if (request.fotoVeiculoUrl() != null) entregador.setFotoVeiculoUrl(request.fotoVeiculoUrl());
    }

    private void validarDuplicacao(String novoValor, String valorAtual,
                                   java.util.function.BooleanSupplier existeCheck, String mensagemErro) {
        if (!novoValor.equals(valorAtual) && existeCheck.getAsBoolean()) {
            throw APIException.build(HttpStatus.CONFLICT, mensagemErro);
        }
    }

    @Override
    @Transactional
    public void desativarEntregador(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

        entregador.desativar();
        entregadorRepository.save(entregador);

        // Registrar mudança de disponibilidade
        registrarHistoricoDisponibilidade(entregador, false, null, null);
    }

    @Override
    @Transactional
    public void reativarEntregador(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

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
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

        if (Boolean.FALSE.equals(entregador.getAtivo())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "Entregador inativo não pode alterar disponibilidade");
        }

        entregador.alterarDisponibilidade(request.disponivel());
        entregadorRepository.save(entregador);

        // Registrar histórico
        registrarHistoricoDisponibilidade(
                entregador,
                request.disponivel(),
                request.latitude(),
                request.longitude()
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
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EstatisticasEntregadorResponse obterEstatisticas(Long entregadorId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

        // Contar documentos
        long totalDocumentos = documentoRepository.countByEntregadorId(entregadorId);
        long documentosVerificados = documentoRepository.countByEntregadorIdAndVerificado(entregadorId, true);
        long documentosPendentes = totalDocumentos - documentosVerificados;

        // Documentos vencidos
        List<Long> documentosVencidos = documentoRepository.findDocumentosVencidos(LocalDate.now())
                .stream()
                .filter(d -> d.getEntregador().getId().equals(entregadorId))
                .map(DocumentoEntregador::getId)
                .toList();

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
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

        return EntregadorResumoResponse.from(entregador);
    }

    @Override
    @Transactional
    public void notificarAtribuicaoEntrega(Long entregadorId, Long entregaId) {
        Entregador entregador = entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

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
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, ENTREGADOR_NAO_ENCONTRADO));

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
