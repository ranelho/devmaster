package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.CriarUsuarioRestauranteRequest;
import com.devmaster.application.api.request.VincularUsuarioRestauranteRequest;
import com.devmaster.application.api.response.UsuarioRestauranteResponse;
import com.devmaster.application.service.AuthIntegrationService;
import com.devmaster.application.service.UsuarioRestauranteService;
import com.devmaster.domain.Restaurante;
import com.devmaster.domain.UsuarioRestaurante;
import com.devmaster.domain.UsuarioRestaurante.RoleRestaurante;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.RestauranteRepository;
import com.devmaster.infrastructure.repository.UsuarioRestauranteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de gerenciamento de vínculos usuário-restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioRestauranteApplicationService implements UsuarioRestauranteService {
    
    private final UsuarioRestauranteRepository usuarioRestauranteRepository;
    private final RestauranteRepository restauranteRepository;
    private final AuthIntegrationService authIntegrationService;
    
    @Override
    @Transactional
    public UsuarioRestauranteResponse criarEVincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        CriarUsuarioRestauranteRequest request
    ) {
        log.info("Criando e vinculando usuário: email={}, restaurante={}, role={}", 
            request.email(), restauranteId, request.role());
        
        // Validar se restaurante existe
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        // Validar permissões do usuário autenticado
        validarPermissaoParaVincular(usuarioAutenticado, restauranteId, request.role());
        
        // Criar usuário no Auth Service
        // Se a role for ADMIN, criar como ADMIN no Auth Service
        boolean isAdmin = request.role() == RoleRestaurante.ADMIN;
        UUID novoUsuarioId = authIntegrationService.criarUsuario(
            request.nome(),
            request.email(),
            request.senha(),
            isAdmin
        );
        
        log.info("Usuário criado no Auth Service: id={}, isAdmin={}", novoUsuarioId, isAdmin);
        
        // Verificar se já existe vínculo
        if (usuarioRestauranteRepository.existsByUsuarioIdAndRestauranteId(
            novoUsuarioId, restauranteId)) {
            throw APIException.build(HttpStatus.CONFLICT, 
                "Usuário já está vinculado a este restaurante");
        }
        
        // Criar vínculo
        UsuarioRestaurante vinculo = UsuarioRestaurante.builder()
            .usuarioId(novoUsuarioId)
            .restaurante(restaurante)
            .role(request.role())
            .ativo(true)
            .criadoPor(usuarioAutenticado)
            .build();
        
        vinculo = usuarioRestauranteRepository.save(vinculo);
        
        log.info("Usuário vinculado com sucesso ao restaurante: usuarioId={}, restauranteId={}", 
            novoUsuarioId, restauranteId);
        
        return UsuarioRestauranteResponse.from(vinculo);
    }
    
    @Override
    @Transactional
    public UsuarioRestauranteResponse vincularUsuario(
        UUID usuarioAutenticado,
        Long restauranteId,
        VincularUsuarioRestauranteRequest request
    ) {
        log.info("Vinculando usuário {} ao restaurante {} com role {}", 
            request.usuarioId(), restauranteId, request.role());
        
        // Validar se restaurante existe
        Restaurante restaurante = restauranteRepository.findById(restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        
        // Validar permissões do usuário autenticado
        validarPermissaoParaVincular(usuarioAutenticado, restauranteId, request.role());
        
        // Verificar se já existe vínculo
        if (usuarioRestauranteRepository.existsByUsuarioIdAndRestauranteId(
            request.usuarioId(), restauranteId)) {
            throw APIException.build(HttpStatus.CONFLICT, 
                "Usuário já está vinculado a este restaurante");
        }
        
        // Criar vínculo
        UsuarioRestaurante vinculo = UsuarioRestaurante.builder()
            .usuarioId(request.usuarioId())
            .restaurante(restaurante)
            .role(request.role())
            .ativo(true)
            .criadoPor(usuarioAutenticado)
            .build();
        
        vinculo = usuarioRestauranteRepository.save(vinculo);
        
        log.info("Usuário {} vinculado com sucesso ao restaurante {}", 
            request.usuarioId(), restauranteId);
        
        return UsuarioRestauranteResponse.from(vinculo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRestauranteResponse> listarUsuariosDoRestaurante(
        UUID usuarioAutenticado,
        Long restauranteId
    ) {
        // Validar acesso ao restaurante
        validarAcessoAoRestaurante(usuarioAutenticado, restauranteId);
        
        return usuarioRestauranteRepository.findByRestauranteIdAndAtivoTrue(restauranteId)
            .stream()
            .map(UsuarioRestauranteResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioRestauranteResponse> listarUsuariosPorRole(
        UUID usuarioAutenticado,
        Long restauranteId,
        RoleRestaurante role
    ) {
        // Validar acesso ao restaurante
        validarAcessoAoRestaurante(usuarioAutenticado, restauranteId);
        
        return usuarioRestauranteRepository.findByRestauranteIdAndRoleAndAtivoTrue(restauranteId, role)
            .stream()
            .map(UsuarioRestauranteResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public UsuarioRestauranteResponse buscarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        // Validar acesso ao restaurante
        validarAcessoAoRestaurante(usuarioAutenticado, restauranteId);
        
        UsuarioRestaurante vinculo = usuarioRestauranteRepository
            .findByUsuarioIdAndRestauranteIdAndAtivoTrue(usuarioId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, 
                "Vínculo não encontrado"));
        
        return UsuarioRestauranteResponse.from(vinculo);
    }
    
    @Override
    @Transactional
    public void desativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        log.info("Desativando vínculo do usuário {} com restaurante {}", usuarioId, restauranteId);
        
        // Validar permissões
        validarPermissaoParaGerenciarUsuarios(usuarioAutenticado, restauranteId);
        
        UsuarioRestaurante vinculo = usuarioRestauranteRepository
            .findByUsuarioIdAndRestauranteIdAndAtivoTrue(usuarioId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, 
                "Vínculo não encontrado"));
        
        vinculo.desativar();
        usuarioRestauranteRepository.save(vinculo);
        
        log.info("Vínculo desativado com sucesso");
    }
    
    @Override
    @Transactional
    public void ativarVinculo(
        UUID usuarioAutenticado,
        Long restauranteId,
        UUID usuarioId
    ) {
        log.info("Ativando vínculo do usuário {} com restaurante {}", usuarioId, restauranteId);
        
        // Validar permissões
        validarPermissaoParaGerenciarUsuarios(usuarioAutenticado, restauranteId);
        
        List<UsuarioRestaurante> vinculos = usuarioRestauranteRepository
            .findByUsuarioId(usuarioId);
        
        UsuarioRestaurante vinculo = vinculos.stream()
            .filter(v -> v.getRestaurante().getId().equals(restauranteId))
            .findFirst()
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, 
                "Vínculo não encontrado"));
        
        vinculo.ativar();
        usuarioRestauranteRepository.save(vinculo);
        
        log.info("Vínculo ativado com sucesso");
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean temAcessoAoRestaurante(UUID usuarioId, Long restauranteId) {
        return usuarioRestauranteRepository
            .existsByUsuarioIdAndRestauranteIdAndAtivoTrue(usuarioId, restauranteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public RoleRestaurante buscarRole(UUID usuarioId, Long restauranteId) {
        return usuarioRestauranteRepository
            .findRoleByUsuarioIdAndRestauranteId(usuarioId, restauranteId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, 
                "Usuário não vinculado ao restaurante"));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long buscarRestauranteIdDoUsuario(UUID usuarioId) {
        return usuarioRestauranteRepository
            .findRestauranteIdByUsuarioId(usuarioId)
            .orElse(null);
    }
    
    // ========================================
    // MÉTODOS PRIVADOS DE VALIDAÇÃO
    // ========================================
    
    private void validarPermissaoParaVincular(
        UUID usuarioAutenticado,
        Long restauranteId,
        RoleRestaurante roleAVincular
    ) {
        // Verificar se é SUPER_ADMIN (não está vinculado a nenhum restaurante)
        boolean isSuperAdmin = !temAcessoAoRestaurante(usuarioAutenticado, restauranteId) &&
                               buscarRestauranteIdDoUsuario(usuarioAutenticado) == null;
        
        // SUPER_ADMIN pode vincular qualquer role a qualquer restaurante
        if (isSuperAdmin) {
            log.info("SUPER_ADMIN detectado, permitindo vinculação");
            return;
        }
        
        // Para outros usuários, validar se tem acesso ao restaurante
        if (!temAcessoAoRestaurante(usuarioAutenticado, restauranteId)) {
            throw APIException.build(HttpStatus.FORBIDDEN, 
                "Você não tem permissão para vincular usuários a este restaurante");
        }
        
        RoleRestaurante roleAutenticado = buscarRole(usuarioAutenticado, restauranteId);
        
        // ADMIN pode criar GERENTE e ATENDENTE
        if (roleAutenticado == RoleRestaurante.ADMIN) {
            if (roleAVincular == RoleRestaurante.ADMIN) {
                throw APIException.build(HttpStatus.FORBIDDEN, 
                    "Apenas SUPER_ADMIN pode criar outros ADMINs");
            }
            return;
        }
        
        // GERENTE pode criar apenas ATENDENTE
        if (roleAutenticado == RoleRestaurante.GERENTE) {
            if (roleAVincular != RoleRestaurante.ATENDENTE) {
                throw APIException.build(HttpStatus.FORBIDDEN, 
                    "GERENTE pode criar apenas ATENDENTEs");
            }
            return;
        }
        
        // ATENDENTE não pode criar ninguém
        throw APIException.build(HttpStatus.FORBIDDEN, 
            "Você não tem permissão para vincular usuários");
    }
    
    private void validarAcessoAoRestaurante(UUID usuarioId, Long restauranteId) {
        // TODO: Verificar se usuário é SUPER_ADMIN (consultar serviço de auth)
        
        if (!temAcessoAoRestaurante(usuarioId, restauranteId)) {
            throw APIException.build(HttpStatus.FORBIDDEN, 
                "Você não tem acesso a este restaurante");
        }
    }
    
    private void validarPermissaoParaGerenciarUsuarios(UUID usuarioId, Long restauranteId) {
        if (!temAcessoAoRestaurante(usuarioId, restauranteId)) {
            throw APIException.build(HttpStatus.FORBIDDEN, 
                "Você não tem acesso a este restaurante");
        }
        
        RoleRestaurante role = buscarRole(usuarioId, restauranteId);
        
        if (role == RoleRestaurante.ATENDENTE) {
            throw APIException.build(HttpStatus.FORBIDDEN, 
                "Você não tem permissão para gerenciar usuários");
        }
    }
}
