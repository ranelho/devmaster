package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequiredArgsConstructor
public class ClienteRestController implements ClienteAPI {
    
    private final ClienteService clienteService;
    
    @Override
    public ResponseEntity<ClienteResponse> criarCliente(ClienteRequest request) {
        ClienteResponse response = clienteService.criarCliente(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }
    
    @Override
    public ResponseEntity<ClienteResponse> buscarCliente(Long id) {
        ClienteResponse response = clienteService.buscarCliente(id);
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<ClienteResponse> buscarClientePorTelefone(String telefone) {
        ClienteResponse response = clienteService.buscarClientePorTelefone(telefone);
        return ResponseEntity.ok(response);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE', 'ATENDENTE')")
    public ResponseEntity<Page<ClienteResponse>> listarClientes(
        Boolean ativo,
        String nome,
        int page,
        int size
    ) {
        Page<ClienteResponse> response = clienteService.listarClientes(ativo, nome, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<ClienteResponse> atualizarCliente(Long id, AtualizarClienteRequest request) {
        ClienteResponse response = clienteService.atualizarCliente(id, request);
        return ResponseEntity.ok(response);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Void> desativarCliente(Long id) {
        clienteService.desativarCliente(id);
        return ResponseEntity.noContent().build();
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<MessageResponse> reativarCliente(Long id) {
        clienteService.reativarCliente(id);
        return ResponseEntity.ok(new MessageResponse("Cliente reativado com sucesso"));
    }
}
