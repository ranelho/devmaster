package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.application.service.ClienteService;
import com.devmaster.domain.Cliente;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.ClienteRepository;
import com.devmaster.infrastructure.repository.EnderecoClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ClienteApplicationService implements ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final EnderecoClienteRepository enderecoRepository;
    
    @Override
    @Transactional
    public ClienteResponse criarCliente(ClienteRequest request) {
        // Verificar se já existe cliente com o telefone
        Cliente clienteExistente = clienteRepository.findByTelefone(request.telefone()).orElse(null);
        
        // Se não existe por telefone, verificar por CPF
        if (clienteExistente == null && request.cpf() != null) {
            clienteExistente = clienteRepository.findByCpf(request.cpf()).orElse(null);
        }
        
        // Se cliente já existe, retornar o cadastro existente
        if (clienteExistente != null) {
            // Se o cliente estava inativo, reativar
            if (!clienteExistente.getAtivo()) {
                clienteExistente.reativar();
                clienteRepository.save(clienteExistente);
            }
            
            // Se foi fornecido um novo endereço, adicionar
            if (request.endereco() != null) {
                criarEnderecoParaCliente(clienteExistente, request.endereco());
            }
            
            return mapToResponse(clienteExistente);
        }
        
        // Validar duplicação de email (apenas se for criar novo cliente)
        if (request.email() != null && clienteRepository.existsByEmail(request.email())) {
            throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado para outro cliente");
        }
        
        // Criar novo cliente
        Cliente cliente = Cliente.builder()
            .telefone(request.telefone())
            .email(request.email())
            .nomeCompleto(request.nomeCompleto())
            .cpf(request.cpf())
            .dataNascimento(request.dataNascimento())
            .ativo(true)
            .build();
        
        cliente = clienteRepository.save(cliente);
        
        // Criar endereço se fornecido
        if (request.endereco() != null) {
            criarEnderecoParaCliente(cliente, request.endereco());
        }
        
        return mapToResponse(cliente);
    }
    
    private void criarEnderecoParaCliente(Cliente cliente, EnderecoClienteRequest enderecoRequest) {
        com.devmaster.domain.EnderecoCliente endereco = com.devmaster.domain.EnderecoCliente.builder()
            .cliente(cliente)
            .rotulo(enderecoRequest.rotulo())
            .logradouro(enderecoRequest.logradouro())
            .numero(enderecoRequest.numero())
            .complemento(enderecoRequest.complemento())
            .bairro(enderecoRequest.bairro())
            .cidade(enderecoRequest.cidade())
            .estado(enderecoRequest.estado())
            .cep(enderecoRequest.cep())
            .latitude(enderecoRequest.latitude())
            .longitude(enderecoRequest.longitude())
            .padrao(true) // Primeiro endereço é sempre padrão
            .build();
        
        enderecoRepository.save(endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        
        return mapToResponse(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarClientePorTelefone(String telefone) {
        Cliente cliente = clienteRepository.findByTelefone(telefone)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        
        return mapToResponse(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponse> listarClientes(Boolean ativo, String nome, Pageable pageable) {
        Page<Cliente> clientes;
        
        if (nome != null && !nome.isBlank()) {
            clientes = clienteRepository.findByNomeCompletoContainingIgnoreCase(nome, pageable);
        } else if (ativo != null) {
            clientes = clienteRepository.findByAtivo(ativo, pageable);
        } else {
            clientes = clienteRepository.findAll(pageable);
        }
        
        return clientes.map(this::mapToResponse);
    }
    
    @Override
    @Transactional
    public ClienteResponse atualizarCliente(Long clienteId, AtualizarClienteRequest request) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        
        // Atualizar campos se fornecidos
        if (request.telefone() != null) {
            if (!request.telefone().equals(cliente.getTelefone()) && 
                clienteRepository.existsByTelefone(request.telefone())) {
                throw APIException.build(HttpStatus.CONFLICT, "Telefone já cadastrado");
            }
            cliente.setTelefone(request.telefone());
        }
        
        if (request.email() != null) {
            if (!request.email().equals(cliente.getEmail()) && 
                clienteRepository.existsByEmail(request.email())) {
                throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
            }
            cliente.setEmail(request.email());
        }
        
        if (request.nomeCompleto() != null) {
            cliente.setNomeCompleto(request.nomeCompleto());
        }
        
        if (request.cpf() != null) {
            if (!request.cpf().equals(cliente.getCpf()) && 
                clienteRepository.existsByCpf(request.cpf())) {
                throw APIException.build(HttpStatus.CONFLICT, "CPF já cadastrado");
            }
            cliente.setCpf(request.cpf());
        }
        
        if (request.dataNascimento() != null) {
            cliente.setDataNascimento(request.dataNascimento());
        }
        
        cliente = clienteRepository.save(cliente);
        
        return mapToResponse(cliente);
    }
    
    @Override
    @Transactional
    public void desativarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        
        cliente.desativar();
        clienteRepository.save(cliente);
    }
    
    @Override
    @Transactional
    public void reativarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        
        cliente.reativar();
        clienteRepository.save(cliente);
    }
    
    private ClienteResponse mapToResponse(Cliente cliente) {
        List<EnderecoClienteResponse> enderecos = enderecoRepository.findByClienteId(cliente.getId())
            .stream()
            .map(EnderecoClienteResponse::from)
            .collect(Collectors.toList());
        
        return ClienteResponse.from(cliente, enderecos);
    }
}
