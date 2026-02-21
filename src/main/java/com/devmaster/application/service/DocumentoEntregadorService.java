package com.devmaster.application.service;

import com.devmaster.application.api.request.DocumentoEntregadorRequest;
import com.devmaster.application.api.request.VerificarDocumentoRequest;
import com.devmaster.application.api.response.DocumentoEntregadorResponse;
import com.devmaster.domain.enums.TipoDocumento;

import java.util.List;
import java.util.UUID;

/**
 * Interface de serviço para operações relacionadas a DocumentoEntregador.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public interface DocumentoEntregadorService {
    
    /**
     * Adiciona documento ao cadastro do entregador.
     * 
     * @param usuarioId ID do usuário que está adicionando
     * @param entregadorId ID do entregador
     * @param request Dados do documento
     * @return Documento criado
     */
    DocumentoEntregadorResponse adicionarDocumento(
        UUID usuarioId,
        Long entregadorId,
        DocumentoEntregadorRequest request
    );
    
    /**
     * Lista todos os documentos de um entregador.
     * 
     * @param usuarioId ID do usuário que está listando
     * @param entregadorId ID do entregador
     * @return Lista de documentos
     */
    List<DocumentoEntregadorResponse> listarDocumentos(UUID usuarioId, Long entregadorId);
    
    /**
     * Busca documento específico.
     * 
     * @param usuarioId ID do usuário que está buscando
     * @param entregadorId ID do entregador
     * @param documentoId ID do documento
     * @return Dados do documento
     */
    DocumentoEntregadorResponse buscarDocumento(
        UUID usuarioId,
        Long entregadorId,
        Long documentoId
    );
    
    /**
     * Busca documento por tipo.
     * 
     * @param usuarioId ID do usuário que está buscando
     * @param entregadorId ID do entregador
     * @param tipoDocumento Tipo do documento
     * @return Dados do documento
     */
    DocumentoEntregadorResponse buscarDocumentoPorTipo(
        UUID usuarioId,
        Long entregadorId,
        TipoDocumento tipoDocumento
    );
    
    /**
     * Verifica ou remove verificação de documento.
     * 
     * @param usuarioId ID do usuário que está verificando
     * @param entregadorId ID do entregador
     * @param documentoId ID do documento
     * @param request Dados de verificação
     * @return Documento atualizado
     */
    DocumentoEntregadorResponse verificarDocumento(
        UUID usuarioId,
        Long entregadorId,
        Long documentoId,
        VerificarDocumentoRequest request
    );
    
    /**
     * Remove documento do cadastro.
     * 
     * @param usuarioId ID do usuário que está removendo
     * @param entregadorId ID do entregador
     * @param documentoId ID do documento
     */
    void removerDocumento(UUID usuarioId, Long entregadorId, Long documentoId);
    
    /**
     * Lista documentos vencidos.
     * 
     * @param usuarioId ID do usuário que está listando
     * @return Lista de documentos vencidos
     */
    List<DocumentoEntregadorResponse> listarDocumentosVencidos(UUID usuarioId);
    
    /**
     * Lista documentos próximos do vencimento (30 dias).
     * 
     * @param usuarioId ID do usuário que está listando
     * @return Lista de documentos próximos do vencimento
     */
    List<DocumentoEntregadorResponse> listarDocumentosProximosVencimento(UUID usuarioId);
    
    /**
     * Lista documentos não verificados.
     * 
     * @param usuarioId ID do usuário que está listando
     * @return Lista de documentos não verificados
     */
    List<DocumentoEntregadorResponse> listarDocumentosNaoVerificados(UUID usuarioId);
}
