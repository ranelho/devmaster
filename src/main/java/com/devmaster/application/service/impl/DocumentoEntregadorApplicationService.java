package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.DocumentoEntregadorRequest;
import com.devmaster.application.api.request.VerificarDocumentoRequest;
import com.devmaster.application.api.response.DocumentoEntregadorResponse;
import com.devmaster.application.service.DocumentoEntregadorService;
import com.devmaster.domain.DocumentoEntregador;
import com.devmaster.domain.Entregador;
import com.devmaster.domain.enums.TipoDocumento;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.DocumentoEntregadorRepository;
import com.devmaster.infrastructure.repository.EntregadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Implementação do serviço de DocumentoEntregador.
 *
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class DocumentoEntregadorApplicationService implements DocumentoEntregadorService {

    public static final String NAO_PERTENCE = "Documento não pertence ao entregador informado";
    private final DocumentoEntregadorRepository documentoRepository;
    private final EntregadorRepository entregadorRepository;

    @Override
    @Transactional
    public DocumentoEntregadorResponse adicionarDocumento(
            UUID usuarioId,
            Long entregadorId,
            DocumentoEntregadorRequest request
    ) {
        Entregador entregador = buscarEntregadorOuFalhar(entregadorId);

        // Verificar se já existe documento do mesmo tipo
        if (documentoRepository.existsByEntregadorIdAndTipoDocumento(entregadorId, request.tipoDocumento())) {
            throw APIException.build(HttpStatus.CONFLICT,
                    "Entregador já possui documento do tipo " + request.tipoDocumento().getDescricao());
        }

        DocumentoEntregador documento = DocumentoEntregador.builder()
                .entregador(entregador)
                .tipoDocumento(request.tipoDocumento())
                .numeroDocumento(request.numeroDocumento())
                .urlArquivo(request.urlArquivo())
                .dataValidade(request.dataValidade())
                .verificado(false)
                .build();

        documento = documentoRepository.save(documento);

        return DocumentoEntregadorResponse.from(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoEntregadorResponse> listarDocumentos(UUID usuarioId, Long entregadorId) {
        buscarEntregadorOuFalhar(entregadorId);

        return documentoRepository.findByEntregadorId(entregadorId)
                .stream()
                .map(DocumentoEntregadorResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoEntregadorResponse buscarDocumento(
            UUID usuarioId,
            Long entregadorId,
            Long documentoId
    ) {
        DocumentoEntregador documento = buscarDocumentoOuFalhar(documentoId);

        if (!documento.getEntregador().getId().equals(entregadorId)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, NAO_PERTENCE);
        }

        return DocumentoEntregadorResponse.from(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentoEntregadorResponse buscarDocumentoPorTipo(
            UUID usuarioId,
            Long entregadorId,
            TipoDocumento tipoDocumento
    ) {
        buscarEntregadorOuFalhar(entregadorId);

        DocumentoEntregador documento = documentoRepository
                .findByEntregadorIdAndTipoDocumento(entregadorId, tipoDocumento)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND,
                        "Documento do tipo " + tipoDocumento.getDescricao() + " não encontrado"));

        return DocumentoEntregadorResponse.from(documento);
    }

    @Override
    @Transactional
    public DocumentoEntregadorResponse verificarDocumento(
            UUID usuarioId,
            Long entregadorId,
            Long documentoId,
            VerificarDocumentoRequest request
    ) {
        DocumentoEntregador documento = buscarDocumentoOuFalhar(documentoId);

        if (!documento.getEntregador().getId().equals(entregadorId)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, NAO_PERTENCE);
        }

        if (request.verificado()) {
            documento.verificar(usuarioId);
        } else {
            documento.removerVerificacao();
        }

        documento = documentoRepository.save(documento);

        return DocumentoEntregadorResponse.from(documento);
    }

    @Override
    @Transactional
    public void removerDocumento(UUID usuarioId, Long entregadorId, Long documentoId) {
        DocumentoEntregador documento = buscarDocumentoOuFalhar(documentoId);

        if (!documento.getEntregador().getId().equals(entregadorId)) {
            throw APIException.build(HttpStatus.BAD_REQUEST, NAO_PERTENCE);
        }

        documentoRepository.delete(documento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoEntregadorResponse> listarDocumentosVencidos(UUID usuarioId) {
        return documentoRepository.findDocumentosVencidos(LocalDate.now())
                .stream()
                .map(DocumentoEntregadorResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoEntregadorResponse> listarDocumentosProximosVencimento(UUID usuarioId) {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataLimite = dataAtual.plusDays(30);

        return documentoRepository.findDocumentosProximosVencimento(dataAtual, dataLimite)
                .stream()
                .map(DocumentoEntregadorResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoEntregadorResponse> listarDocumentosNaoVerificados(UUID usuarioId) {
        return documentoRepository.findByVerificado(false)
                .stream()
                .map(DocumentoEntregadorResponse::from)
                .toList();
    }

    // Métodos auxiliares

    private Entregador buscarEntregadorOuFalhar(Long entregadorId) {
        return entregadorRepository.findById(entregadorId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
    }

    private DocumentoEntregador buscarDocumentoOuFalhar(Long documentoId) {
        return documentoRepository.findById(documentoId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Documento não encontrado"));
    }
}
