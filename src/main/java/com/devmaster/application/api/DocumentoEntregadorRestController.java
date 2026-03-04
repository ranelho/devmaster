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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public ResponseEntity<DocumentoEntregadorResponse> adicionarDocumento(
        Authentication authentication,
        Long entregadorId,
        DocumentoEntregadorRequest request
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        DocumentoEntregadorResponse response = documentoService.adicionarDocumento(usuarioId, entregadorId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<DocumentoEntregadorResponse>> listarDocumentos(
        Authentication authentication,
        Long entregadorId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        List<DocumentoEntregadorResponse> response = documentoService.listarDocumentos(usuarioId, entregadorId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<DocumentoEntregadorResponse> buscarDocumento(
        Authentication authentication,
        Long entregadorId,
        Long documentoId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        DocumentoEntregadorResponse response = documentoService.buscarDocumento(usuarioId, entregadorId, documentoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<DocumentoEntregadorResponse> buscarDocumentoPorTipo(
        Authentication authentication,
        Long entregadorId,
        TipoDocumento tipoDocumento
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        DocumentoEntregadorResponse response = documentoService.buscarDocumentoPorTipo(usuarioId, entregadorId, tipoDocumento);
        return ResponseEntity.ok(response);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<DocumentoEntregadorResponse> verificarDocumento(
        Authentication authentication,
        Long entregadorId,
        Long documentoId,
        VerificarDocumentoRequest request
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        DocumentoEntregadorResponse response = documentoService.verificarDocumento(usuarioId, entregadorId, documentoId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> removerDocumento(
        Authentication authentication,
        Long entregadorId,
        Long documentoId
    ) {
        validarAutenticacao(authentication);
        UUID usuarioId = UUID.fromString(authentication.getName());
        documentoService.removerDocumento(usuarioId, entregadorId, documentoId);
        return ResponseEntity.noContent().build();
    }
    
    private void validarAutenticacao(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw APIException.build(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }
    }
}
