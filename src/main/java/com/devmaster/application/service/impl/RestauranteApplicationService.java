package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AtualizarRestauranteRequest;
import com.devmaster.application.api.request.EnderecoRestauranteRequest;
import com.devmaster.application.api.request.HorarioRestauranteRequest;
import com.devmaster.application.api.request.RestauranteRequest;
import com.devmaster.application.api.response.EnderecoRestauranteResponse;
import com.devmaster.application.api.response.HorarioRestauranteResponse;
import com.devmaster.application.api.response.RestauranteResponse;
import com.devmaster.application.api.response.RestauranteResumoResponse;
import com.devmaster.application.service.RestauranteService;
import com.devmaster.domain.EnderecoRestaurante;
import com.devmaster.domain.HorarioRestaurante;
import com.devmaster.domain.Restaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.EnderecoRestauranteRepository;
import com.devmaster.infrastructure.repository.HorarioRestauranteRepository;
import com.devmaster.infrastructure.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class RestauranteApplicationService implements RestauranteService {
    
    private final RestauranteRepository restauranteRepository;
    private final EnderecoRestauranteRepository enderecoRepository;
    private final HorarioRestauranteRepository horarioRepository;
    
    @Override
    @Transactional
    public RestauranteResponse criarRestaurante(UUID usuarioId, RestauranteRequest request) {
        // Validar duplicação de slug
        if (restauranteRepository.existsBySlug(request.slug())) {
            throw APIException.build(HttpStatus.CONFLICT, "Slug já cadastrado");
        }
        
        // Validar duplicação de CNPJ
        if (request.cnpj() != null && restauranteRepository.existsByCnpj(request.cnpj())) {
            throw APIException.build(HttpStatus.CONFLICT, "CNPJ já cadastrado");
        }
        
        // Validar duplicação de email
        if (request.email() != null && restauranteRepository.existsByEmail(request.email())) {
            throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        
        // Criar restaurante
        Restaurante restaurante = Restaurante.builder()
            .nome(request.nome())
            .slug(request.slug())
            .descricao(request.descricao())
            .telefone(request.telefone())
            .email(request.email())
            .cnpj(request.cnpj())
            .logoUrl(request.logoUrl())
            .bannerUrl(request.bannerUrl())
            .ativo(true)
            .aberto(false)
            .avaliacao(BigDecimal.ZERO)
            .taxaEntrega(request.taxaEntrega() != null ? request.taxaEntrega() : BigDecimal.ZERO)
            .valorMinimoPedido(request.valorMinimoPedido() != null ? request.valorMinimoPedido() : BigDecimal.ZERO)
            .tempoMedioEntrega(request.tempoMedioEntrega())
            .build();
        
        restaurante = restauranteRepository.save(restaurante);
        
        // Criar endereço se fornecido
        EnderecoRestauranteResponse enderecoResponse = null;
        if (request.endereco() != null) {
            enderecoResponse = criarEndereco(restaurante, request.endereco());
        }
        
        return RestauranteResponse.from(restaurante, enderecoResponse);
    }
    
    private EnderecoRestauranteResponse criarEndereco(Restaurante restaurante, EnderecoRestauranteRequest request) {
        EnderecoRestaurante endereco = EnderecoRestaurante.builder()
            .restaurante(restaurante)
            .logradouro(request.logradouro())
            .numero(request.numero())
            .complemento(request.complemento())
            .bairro(request.bairro())
            .cidade(request.cidade())
            .estado(request.estado())
            .cep(request.cep())
            .latitude(request.latitude())
            .longitude(request.longitude())
            .build();
        
        endereco = enderecoRepository.save(endereco);
        return EnderecoRestauranteResponse.from(endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarRestaurante(UUID usuarioId, Long restauranteId) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        EnderecoRestauranteResponse endereco = enderecoRepository.findByRestauranteId(restauranteId)
            .map(EnderecoRestauranteResponse::from)
            .orElse(null);
        
        return RestauranteResponse.from(restaurante, endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarRestaurantePorSlug(UUID usuarioId, String slug) {
        Restaurante restaurante = restauranteRepository.findBySlug(slug)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        EnderecoRestauranteResponse endereco = enderecoRepository.findByRestauranteId(restaurante.getId())
            .map(EnderecoRestauranteResponse::from)
            .orElse(null);
        
        return RestauranteResponse.from(restaurante, endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RestauranteResumoResponse> listarRestaurantes(
        UUID usuarioId,
        Boolean ativo,
        Boolean aberto,
        String nome,
        Pageable pageable
    ) {
        Page<Restaurante> restaurantes;
        
        if (nome != null && !nome.isBlank()) {
            restaurantes = restauranteRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (ativo != null && aberto != null) {
            restaurantes = restauranteRepository.findByAtivoAndAberto(ativo, aberto, pageable);
        } else if (ativo != null) {
            restaurantes = restauranteRepository.findByAtivo(ativo, pageable);
        } else if (aberto != null) {
            restaurantes = restauranteRepository.findByAberto(aberto, pageable);
        } else {
            restaurantes = restauranteRepository.findAll(pageable);
        }
        
        return restaurantes.map(RestauranteResumoResponse::from);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResumoResponse> listarRestaurantesAbertosOrdenadosPorAvaliacao(
        UUID usuarioId,
        int limite
    ) {
        return restauranteRepository.findRestaurantesAbertosOrdenadosPorAvaliacao(PageRequest.of(0, limite))
            .stream()
            .map(RestauranteResumoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public RestauranteResponse atualizarRestaurante(
        UUID usuarioId,
        Long restauranteId,
        AtualizarRestauranteRequest request
    ) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        
        // Atualizar campos se fornecidos
        if (request.nome() != null) {
            restaurante.setNome(request.nome());
        }
        
        if (request.descricao() != null) {
            restaurante.setDescricao(request.descricao());
        }
        
        if (request.telefone() != null) {
            restaurante.setTelefone(request.telefone());
        }
        
        if (request.email() != null) {
            if (!request.email().equals(restaurante.getEmail()) && 
                restauranteRepository.existsByEmail(request.email())) {
                throw APIException.build(HttpStatus.CONFLICT, "Email já cadastrado");
            }
            restaurante.setEmail(request.email());
        }
        
        if (request.logoUrl() != null) {
            restaurante.setLogoUrl(request.logoUrl());
        }
        
        if (request.bannerUrl() != null) {
            restaurante.setBannerUrl(request.bannerUrl());
        }
        
        if (request.taxaEntrega() != null) {
            restaurante.setTaxaEntrega(request.taxaEntrega());
        }
        
        if (request.valorMinimoPedido() != null) {
            restaurante.setValorMinimoPedido(request.valorMinimoPedido());
        }
        
        if (request.tempoMedioEntrega() != null) {
            restaurante.setTempoMedioEntrega(request.tempoMedioEntrega());
        }
        
        restaurante = restauranteRepository.save(restaurante);
        
        EnderecoRestauranteResponse endereco = enderecoRepository.findByRestauranteId(restauranteId)
            .map(EnderecoRestauranteResponse::from)
            .orElse(null);
        
        return RestauranteResponse.from(restaurante, endereco);
    }
    
    @Override
    @Transactional
    public void ativarRestaurante(UUID usuarioId, Long restauranteId) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        restaurante.ativar();
        restauranteRepository.save(restaurante);
    }
    
    @Override
    @Transactional
    public void desativarRestaurante(UUID usuarioId, Long restauranteId) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        restaurante.desativar();
        restauranteRepository.save(restaurante);
    }
    
    @Override
    @Transactional
    public void abrirRestaurante(UUID usuarioId, Long restauranteId) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        restaurante.abrir();
        restauranteRepository.save(restaurante);
    }
    
    @Override
    @Transactional
    public void fecharRestaurante(UUID usuarioId, Long restauranteId) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        restaurante.fechar();
        restauranteRepository.save(restaurante);
    }
    
    @Override
    @Transactional
    public EnderecoRestauranteResponse adicionarEndereco(
        UUID usuarioId,
        Long restauranteId,
        EnderecoRestauranteRequest request
    ) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        
        // Verificar se já existe endereço
        if (enderecoRepository.findByRestauranteId(restauranteId).isPresent()) {
            throw APIException.build(HttpStatus.CONFLICT, "Restaurante já possui endereço cadastrado");
        }
        
        return criarEndereco(restaurante, request);
    }
    
    @Override
    @Transactional
    public EnderecoRestauranteResponse atualizarEndereco(
        UUID usuarioId,
        Long restauranteId,
        EnderecoRestauranteRequest request
    ) {
        buscarRestauranteOuFalhar(restauranteId);
        
        EnderecoRestaurante endereco = enderecoRepository.findByRestauranteId(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        
        // Atualizar campos
        endereco.setLogradouro(request.logradouro());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setBairro(request.bairro());
        endereco.setCidade(request.cidade());
        endereco.setEstado(request.estado());
        endereco.setCep(request.cep());
        endereco.setLatitude(request.latitude());
        endereco.setLongitude(request.longitude());
        
        endereco = enderecoRepository.save(endereco);
        return EnderecoRestauranteResponse.from(endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public EnderecoRestauranteResponse buscarEndereco(UUID usuarioId, Long restauranteId) {
        buscarRestauranteOuFalhar(restauranteId);
        
        EnderecoRestaurante endereco = enderecoRepository.findByRestauranteId(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Endereço não encontrado"));
        
        return EnderecoRestauranteResponse.from(endereco);
    }
    
    // Horários
    
    @Override
    @Transactional
    public HorarioRestauranteResponse adicionarHorario(
        UUID usuarioId,
        Long restauranteId,
        HorarioRestauranteRequest request
    ) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        
        // Verificar se já existe horário para este dia
        if (horarioRepository.existsByRestauranteIdAndDiaSemana(restauranteId, request.diaSemana())) {
            throw APIException.build(HttpStatus.CONFLICT, 
                "Já existe horário cadastrado para este dia da semana");
        }
        
        // Validar horários
        if (request.horarioAbertura().isAfter(request.horarioFechamento())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Horário de abertura deve ser anterior ao horário de fechamento");
        }
        
        HorarioRestaurante horario = HorarioRestaurante.builder()
            .restaurante(restaurante)
            .diaSemana(request.diaSemana())
            .horarioAbertura(request.horarioAbertura())
            .horarioFechamento(request.horarioFechamento())
            .fechado(request.fechado() != null ? request.fechado() : false)
            .build();
        
        horario = horarioRepository.save(horario);
        return HorarioRestauranteResponse.from(horario);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<HorarioRestauranteResponse> listarHorarios(UUID usuarioId, Long restauranteId) {
        buscarRestauranteOuFalhar(restauranteId);
        
        return horarioRepository.findByRestauranteIdOrderByDiaSemana(restauranteId)
            .stream()
            .map(HorarioRestauranteResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public HorarioRestauranteResponse atualizarHorario(
        UUID usuarioId,
        Long restauranteId,
        Integer diaSemana,
        HorarioRestauranteRequest request
    ) {
        buscarRestauranteOuFalhar(restauranteId);
        
        HorarioRestaurante horario = horarioRepository.findByRestauranteIdAndDiaSemana(restauranteId, diaSemana)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Horário não encontrado"));
        
        // Validar horários
        if (request.horarioAbertura().isAfter(request.horarioFechamento())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Horário de abertura deve ser anterior ao horário de fechamento");
        }
        
        horario.setHorarioAbertura(request.horarioAbertura());
        horario.setHorarioFechamento(request.horarioFechamento());
        
        if (request.fechado() != null) {
            if (request.fechado()) {
                horario.marcarComoFechado();
            } else {
                horario.marcarComoAberto();
            }
        }
        
        horario = horarioRepository.save(horario);
        return HorarioRestauranteResponse.from(horario);
    }
    
    @Override
    @Transactional
    public void removerHorario(UUID usuarioId, Long restauranteId, Integer diaSemana) {
        buscarRestauranteOuFalhar(restauranteId);
        
        HorarioRestaurante horario = horarioRepository.findByRestauranteIdAndDiaSemana(restauranteId, diaSemana)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Horário não encontrado"));
        
        horarioRepository.delete(horario);
    }
    
    private Restaurante buscarRestauranteOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
    }
    
    // ========================================
    // MÉTODOS PÚBLICOS (SEM AUTENTICAÇÃO)
    // ========================================
    
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarPorSlug(String slug) {
        Restaurante restaurante = restauranteRepository.findBySlug(slug)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        EnderecoRestauranteResponse endereco = enderecoRepository.findByRestauranteId(restaurante.getId())
            .map(EnderecoRestauranteResponse::from)
            .orElse(null);
        
        return RestauranteResponse.from(restaurante, endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarPorId(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        EnderecoRestauranteResponse endereco = enderecoRepository.findByRestauranteId(restauranteId)
            .map(EnderecoRestauranteResponse::from)
            .orElse(null);
        
        return RestauranteResponse.from(restaurante, endereco);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResumoResponse> listarRestaurantesAbertos(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return restauranteRepository.findByAtivoTrueAndAbertoTrueOrderByAvaliacaoDesc(pageable)
            .stream()
            .map(RestauranteResumoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResumoResponse> listarRestaurantesAtivos() {
        return restauranteRepository.findByAtivoTrueOrderByNomeAsc()
            .stream()
            .map(RestauranteResumoResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResumoResponse> buscarPorNome(String nome) {
        return restauranteRepository.findByAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(nome)
            .stream()
            .map(RestauranteResumoResponse::from)
            .collect(Collectors.toList());
    }
}
