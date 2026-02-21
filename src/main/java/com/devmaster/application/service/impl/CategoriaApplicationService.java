package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AtualizarCategoriaRequest;
import com.devmaster.application.api.request.CategoriaRequest;
import com.devmaster.application.api.response.CategoriaResponse;
import com.devmaster.application.service.CategoriaService;
import com.devmaster.domain.Categoria;
import com.devmaster.domain.Restaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.CategoriaRepository;
import com.devmaster.infrastructure.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Categoria.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CategoriaApplicationService implements CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final RestauranteRepository restauranteRepository;
    
    @Override
    @Transactional
    public CategoriaResponse criarCategoria(UUID usuarioId, Long restauranteId, CategoriaRequest request) {
        Restaurante restaurante = buscarRestauranteOuFalhar(restauranteId);
        
        // Validar duplicação de nome
        if (categoriaRepository.existsByRestauranteIdAndNome(restauranteId, request.nome())) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe uma categoria com este nome");
        }
        
        Categoria categoria = Categoria.builder()
            .restaurante(restaurante)
            .nome(request.nome())
            .descricao(request.descricao())
            .ordemExibicao(request.ordemExibicao() != null ? request.ordemExibicao() : 0)
            .ativo(true)
            .build();
        
        categoria = categoriaRepository.save(categoria);
        return CategoriaResponse.from(categoria);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse buscarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        Categoria categoria = buscarCategoriaOuFalhar(restauranteId, categoriaId);
        return CategoriaResponse.from(categoria);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategorias(UUID usuarioId, Long restauranteId, Boolean ativo) {
        buscarRestauranteOuFalhar(restauranteId);
        
        List<Categoria> categorias = ativo != null
            ? categoriaRepository.findByRestauranteIdAndAtivoOrderByOrdemExibicao(restauranteId, ativo)
            : categoriaRepository.findByRestauranteIdOrderByOrdemExibicao(restauranteId);
        
        return categorias.stream()
            .map(CategoriaResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaResponse> listarCategoriasComPaginacao(
        UUID usuarioId,
        Long restauranteId,
        Pageable pageable
    ) {
        buscarRestauranteOuFalhar(restauranteId);
        
        return categoriaRepository.findByRestauranteId(restauranteId, pageable)
            .map(CategoriaResponse::from);
    }
    
    @Override
    @Transactional
    public CategoriaResponse atualizarCategoria(
        UUID usuarioId,
        Long restauranteId,
        Long categoriaId,
        AtualizarCategoriaRequest request
    ) {
        Categoria categoria = buscarCategoriaOuFalhar(restauranteId, categoriaId);
        
        // Atualizar campos se fornecidos
        if (request.nome() != null) {
            // Validar duplicação de nome (exceto se for o mesmo nome)
            if (!request.nome().equals(categoria.getNome()) && 
                categoriaRepository.existsByRestauranteIdAndNome(restauranteId, request.nome())) {
                throw APIException.build(HttpStatus.CONFLICT, "Já existe uma categoria com este nome");
            }
            categoria.setNome(request.nome());
        }
        
        if (request.descricao() != null) {
            categoria.setDescricao(request.descricao());
        }
        
        if (request.ordemExibicao() != null) {
            categoria.setOrdemExibicao(request.ordemExibicao());
        }
        
        categoria = categoriaRepository.save(categoria);
        return CategoriaResponse.from(categoria);
    }
    
    @Override
    @Transactional
    public void ativarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        Categoria categoria = buscarCategoriaOuFalhar(restauranteId, categoriaId);
        categoria.ativar();
        categoriaRepository.save(categoria);
    }
    
    @Override
    @Transactional
    public void desativarCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        Categoria categoria = buscarCategoriaOuFalhar(restauranteId, categoriaId);
        categoria.desativar();
        categoriaRepository.save(categoria);
    }
    
    @Override
    @Transactional
    public void removerCategoria(UUID usuarioId, Long restauranteId, Long categoriaId) {
        Categoria categoria = buscarCategoriaOuFalhar(restauranteId, categoriaId);
        
        // TODO: Verificar se existem produtos vinculados antes de remover
        
        categoriaRepository.delete(categoria);
    }
    
    private Restaurante buscarRestauranteOuFalhar(Long restauranteId) {
        return restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
    }
    
    private Categoria buscarCategoriaOuFalhar(Long restauranteId, Long categoriaId) {
        return categoriaRepository.findByIdAndRestauranteId(categoriaId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }
    
    // ========================================
    // MÉTODOS PÚBLICOS (SEM AUTENTICAÇÃO)
    // ========================================
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarPorRestaurante(Long restauranteId) {
        // Validar restaurante existe
        if (!restauranteRepository.existsById(restauranteId)) {
            throw APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado");
        }
        
        return categoriaRepository.findByRestauranteIdAndAtivoTrueOrderByOrdemExibicaoAsc(restauranteId)
            .stream()
            .map(CategoriaResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
        
        return CategoriaResponse.from(categoria);
    }
}
