package com.devmaster.application.api;

import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.EnderecoClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controller REST para gestão de endereços de clientes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class EnderecoClienteRestController implements EnderecoClienteAPI {
    
    private final EnderecoClienteService enderecoService;
    
    @Override
    public ResponseEntity<EnderecoClienteResponse> adicionarEndereco(Long clienteId, EnderecoClienteRequest request) {
        EnderecoClienteResponse response = enderecoService.adicionarEndereco(clienteId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<List<EnderecoClienteResponse>> listarEnderecos(Long clienteId) {
        List<EnderecoClienteResponse> response = enderecoService.listarEnderecos(clienteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EnderecoClienteResponse> buscarEndereco(Long clienteId, Long enderecoId) {
        EnderecoClienteResponse response = enderecoService.buscarEndereco(clienteId, enderecoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EnderecoClienteResponse> buscarEnderecoPadrao(Long clienteId) {
        EnderecoClienteResponse response = enderecoService.buscarEnderecoPadrao(clienteId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EnderecoClienteResponse> atualizarEndereco(
        Long clienteId,
        Long enderecoId,
        EnderecoClienteRequest request
    ) {
        EnderecoClienteResponse response = enderecoService.atualizarEndereco(clienteId, enderecoId, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<EnderecoClienteResponse> definirEnderecoPadrao(Long clienteId, Long enderecoId) {
        EnderecoClienteResponse response = enderecoService.definirEnderecoPadrao(clienteId, enderecoId);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<Void> removerEndereco(Long clienteId, Long enderecoId) {
        enderecoService.removerEndereco(clienteId, enderecoId);
        return ResponseEntity.noContent().build();
    }
}
