package com.devmaster.application.api;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.application.api.response.MessageResponse;
import com.devmaster.application.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para gestão de clientes.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class ClienteRestController implements ClienteAPI {
    
    private final ClienteService clienteService;
    
    @Override
    public ClienteResponse criarCliente(ClienteRequest request) {
        return clienteService.criarCliente(request);
    }
    
    @Override
    public ClienteResponse buscarCliente(Long id) {
        return clienteService.buscarCliente(id);
    }
    
    @Override
    public ClienteResponse buscarClientePorTelefone(String telefone) {
        return clienteService.buscarClientePorTelefone(telefone);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'GERENTE', 'ATENDENTE')")
    public Page<ClienteResponse> listarClientes(
        Boolean ativo,
        String nome,
        int page,
        int size
    ) {
        return clienteService.listarClientes(ativo, nome, PageRequest.of(page, size));
    }
    
    @Override
    public ClienteResponse atualizarCliente(Long id, AtualizarClienteRequest request) {
        return clienteService.atualizarCliente(id, request);
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public MessageResponse desativarCliente(Long id) {
        clienteService.desativarCliente(id);
        return new MessageResponse("Cliente desativado com sucesso");
    }
    
    @Override
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public MessageResponse reativarCliente(Long id) {
        clienteService.reativarCliente(id);
        return new MessageResponse("Cliente reativado com sucesso");
    }
}
