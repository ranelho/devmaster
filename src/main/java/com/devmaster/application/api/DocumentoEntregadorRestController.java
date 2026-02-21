package com.devmaster.application.api;

import com.devmaster.application.api.request.DocumentoEntregadorRequest;
import com.devmaster.application.api.request.VerificarDocumentoRequest;
import com.devmaster.application.api.response.DocumentoEntregadorResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.DocumentoEntregadorService;
import com.devmaster.domain.enums.TipoDocumento;
import com.devmaster.handler.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gestão de documentos de entregadores.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class DocumentoEntregadorRestController implements DocumentoEntregadorAPI {
    
    private final DocumentoEntregadorService documentoService;
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public DocumentoEntregadorResponse adicionarDocumento(
        Authentication authentication,
        Long entregadorId,
        DocumentoEntregadorRequest request
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return documentoService.adicionarDocumento(usuarioId, entregadorId, request);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public List<DocumentoEntregadorResponse> listarDocumentos(
        Authentication authentication,
        Long entregadorId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return documentoService.listarDocumentos(usuarioId, entregadorId);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public DocumentoEntregadorResponse buscarDocumento(
        Authentication authentication,
        Long entregadorId,
        Long documentoId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return documentoService.buscarDocumento(usuarioId, entregadorId, documentoId);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public DocumentoEntregadorResponse buscarDocumentoPorTipo(
        Authentication authentication,
        Long entregadorId,
        TipoDocumento tipoDocumento
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return documentoService.buscarDocumentoPorTipo(usuarioId, entregadorId, tipoDocumento);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public DocumentoEntregadorResponse verificarDocumento(
        Authentication authentication,
        Long entregadorId,
        Long documentoId,
        VerificarDocumentoRequest request
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        return documentoService.verificarDocumento(usuarioId, entregadorId, documentoId, request);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public MessageResponse removerDocumento(
        Authentication authentication,
        Long entregadorId,
        Long documentoId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        documentoService.removerDocumento(usuarioId, entregadorId, documentoId);
        return new MessageResponse("Documento removido com sucesso");
    }
    
    private void validarAutenticacao(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
    }
}
