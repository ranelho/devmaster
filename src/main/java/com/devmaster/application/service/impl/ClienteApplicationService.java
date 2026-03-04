package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AtualizarClienteRequest;
import com.devmaster.application.api.request.ClienteRequest;
import com.devmaster.application.api.request.EnderecoClienteRequest;
import com.devmaster.application.api.response.ClienteResponse;
import com.devmaster.application.api.response.EnderecoClienteResponse;
import com.devmaster.application.service.ClienteService;
import com.devmaster.domain.Cliente;
import com.devmaster.domain.EnderecoCliente;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.ClienteRepository;
import com.devmaster.infrastructure.repository.EnderecoClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteApplicationService implements ClienteService {

    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado";
    private final ClienteRepository clienteRepository;
    private final EnderecoClienteRepository enderecoRepository;

    @Override
    @Transactional
    public ClienteResponse criarCliente(ClienteRequest request) {
        return clienteRepository.findByTelefone(request.telefone())
                .or(() -> Optional.ofNullable(request.cpf()).flatMap(clienteRepository::findByCpf))
                .map(existente -> reativarSeNecessario(existente, request))
                .orElseGet(() -> criarNovoCliente(request));
    }

    private ClienteResponse reativarSeNecessario(Cliente cliente, ClienteRequest request) {
        if (Boolean.FALSE.equals(cliente.getAtivo())) {
            cliente.reativar();
            clienteRepository.save(cliente);
        }
        Optional.ofNullable(request.endereco())
                .ifPresent(endereco -> criarEnderecoInicial(cliente, endereco));
        return mapToResponse(cliente);
    }

    private ClienteResponse criarNovoCliente(ClienteRequest request) {
        Optional.ofNullable(request.email())
                .filter(clienteRepository::existsByEmail)
                .ifPresent(email -> {
                    throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado para outro cliente");
                });

        Cliente cliente = clienteRepository.save(Cliente.builder()
                .telefone(request.telefone())
                .email(request.email())
                .nomeCompleto(request.nomeCompleto())
                .cpf(request.cpf())
                .dataNascimento(request.dataNascimento())
                .ativo(true)
                .build());

        Optional.ofNullable(request.endereco())
                .ifPresent(endereco -> criarEnderecoInicial(cliente, endereco));

        return mapToResponse(cliente);
    }

    private void criarEnderecoInicial(Cliente cliente, EnderecoClienteRequest dto) {
        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setCliente(cliente);
        mapDtoToEndereco(endereco, dto);
        endereco.setPadrao(true); // Endereço inicial sempre é padrão
        enderecoRepository.save(endereco);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));

        return mapToResponse(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarClientePorTelefone(String telefone) {
        String telefoneNormalizado = telefone.replaceAll("\\D", "");

        Cliente cliente = clienteRepository.findByTelefone(telefoneNormalizado)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));

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
        Cliente cliente = findClienteOrThrow(clienteId);

        processTelefone(cliente, request);
        processEmail(cliente, request);
        processNome(cliente, request);
        processCpf(cliente, request);

        Optional.ofNullable(request.dataNascimento()).ifPresent(cliente::setDataNascimento);

        processEnderecos(cliente, request.enderecos());

        cliente = saveCliente(cliente);

        return mapToResponse(cliente);
    }

    private void processEnderecos(Cliente cliente, List<EnderecoClienteRequest> novosEnderecos) {
        if (novosEnderecos == null) {
            return;
        }

        // Busca endereços atuais
        List<EnderecoCliente> enderecosAtuais = enderecoRepository.findByClienteId(cliente.getId());
        
        // Mapeia IDs dos novos endereços para identificar quais manter/atualizar
        List<Long> idsParaManter = novosEnderecos.stream()
                .map(EnderecoClienteRequest::id)
                .filter(Objects::nonNull)
                .toList();

        // 1. Remove endereços que não estão na nova lista
        List<EnderecoCliente> paraRemover = enderecosAtuais.stream()
                .filter(end -> !idsParaManter.contains(end.getId()))
                .toList();

        if (!paraRemover.isEmpty()) {
            enderecoRepository.deleteAll(paraRemover);
            enderecosAtuais.removeAll(paraRemover);
        }

        // 2. Atualiza existentes ou Cria novos
        for (EnderecoClienteRequest dto : novosEnderecos) {
            if (dto.id() != null) {
                // Atualizar existente
                enderecosAtuais.stream()
                    .filter(end -> end.getId().equals(dto.id()))
                    .findFirst()
                    .ifPresent(endereco -> atualizarEnderecoExistente(endereco, dto));
            } else {
                // Criar novo
                criarEnderecoParaCliente(cliente, dto);
            }
        }
    }

    private void atualizarEnderecoExistente(EnderecoCliente endereco, EnderecoClienteRequest dto) {
        mapDtoToEndereco(endereco, dto);
        enderecoRepository.save(endereco);
    }

    private void criarEnderecoParaCliente(Cliente cliente, EnderecoClienteRequest dto) {
        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setCliente(cliente);
        mapDtoToEndereco(endereco, dto);
        
        // Se for criação via atualização (padrao pode ser null), define como false se não informado
        // Se for criação inicial (via criarCliente), padrao era true fixo no método antigo, 
        // mas agora o DTO controla. Vamos manter a lógica do DTO ou false padrão.
        if (dto.padrao() == null) {
            endereco.setPadrao(false); 
        }
        
        enderecoRepository.save(endereco);
    }
    
    private void mapDtoToEndereco(EnderecoCliente endereco, EnderecoClienteRequest dto) {
        endereco.setRotulo(dto.rotulo());
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setEstado(dto.estado());
        endereco.setCep(dto.cep());
        endereco.setLatitude(dto.latitude());
        endereco.setLongitude(dto.longitude());
        if (dto.padrao() != null) {
            endereco.setPadrao(dto.padrao());
        }
    }

    private Cliente findClienteOrThrow(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));
    }

    private Cliente saveCliente(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw APIException.build(HttpStatus.CONFLICT, "Violação de integridade: dados duplicados ou inválidos");
        }
    }

    private void processTelefone(Cliente cliente, AtualizarClienteRequest request) {
        if (request.telefone() == null) return;
        String novoTelefone = request.telefone().replaceAll("\\D+", "");
        String telefoneAtual = Optional.ofNullable(cliente.getTelefone()).orElse("");
        if (!novoTelefone.equals(telefoneAtual)) {
            if (clienteRepository.existsByTelefone(novoTelefone)) {
                throw APIException.build(HttpStatus.CONFLICT, "Telefone já cadastrado");
            }
            cliente.setTelefone(novoTelefone);
        }
    }

    private void processEmail(Cliente cliente, AtualizarClienteRequest request) {
        if (request.email() == null) return;
        String novoEmail = request.email().trim();
        String emailAtual = Optional.ofNullable(cliente.getEmail()).orElse("");
        if (!novoEmail.equals(emailAtual)) {
            if (clienteRepository.existsByEmail(novoEmail)) {
                throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
            }
            cliente.setEmail(novoEmail);
        }
    }

    private void processNome(Cliente cliente, AtualizarClienteRequest request) {
        if (request.nomeCompleto() == null) return;
        String novoNome = request.nomeCompleto().trim();
        if (!novoNome.isBlank()) {
            cliente.setNomeCompleto(novoNome);
        }
    }

    private void processCpf(Cliente cliente, AtualizarClienteRequest request) {
        if (request.cpf() == null) return;
        String novoCpf = request.cpf().replaceAll("\\D+", "");
        if (novoCpf.isBlank()) {
            throw APIException.build(HttpStatus.BAD_REQUEST, "CPF inválido");
        }
        String cpfAtual = Optional.ofNullable(cliente.getCpf()).orElse("");
        if (!novoCpf.equals(cpfAtual)) {
            if (clienteRepository.existsByCpf(novoCpf)) {
                throw APIException.build(HttpStatus.CONFLICT, "CPF já cadastrado");
            }
            cliente.setCpf(novoCpf);
        }
    }

    @Override
    @Transactional
    public void desativarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));

        cliente.desativar();
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void reativarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO));

        cliente.reativar();
        clienteRepository.save(cliente);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        List<EnderecoClienteResponse> enderecos = enderecoRepository.findByClienteId(cliente.getId())
                .stream()
                .map(EnderecoClienteResponse::from)
                .toList();

        return ClienteResponse.from(cliente, enderecos);
    }
}
