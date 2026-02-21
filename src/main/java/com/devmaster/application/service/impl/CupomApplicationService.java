package com.devmaster.application.service.impl;

import com.devmaster.application.api.request.AtualizarCupomRequest;
import com.devmaster.application.api.request.CupomRequest;
import com.devmaster.application.api.request.ValidarCupomRequest;
import com.devmaster.application.api.response.CupomResponse;
import com.devmaster.application.api.response.ValidacaoCupomResponse;
import com.devmaster.application.service.CupomService;
import com.devmaster.domain.Cupom;
import com.devmaster.handler.APIException;
import com.devmaster.infrastructure.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Cupom.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class CupomApplicationService implements CupomService {
    
    private final CupomRepository cupomRepository;
    
    @Override
    @Transactional
    public CupomResponse criarCupom(UUID usuarioId, CupomRequest request) {
        // Validar duplicação de código
        if (cupomRepository.existsByCodigo(request.codigo())) {
            throw APIException.build(HttpStatus.CONFLICT, "Já existe um cupom com este código");
        }
        
        // Validar datas
        if (request.validoDe().isAfter(request.validoAte())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Data de início deve ser anterior à data de fim");
        }
        
        Cupom cupom = Cupom.builder()
            .codigo(request.codigo().toUpperCase())
            .descricao(request.descricao())
            .tipoDesconto(request.tipoDesconto())
            .valorDesconto(request.valorDesconto())
            .valorMinimoPedido(request.valorMinimoPedido() != null ? request.valorMinimoPedido() : BigDecimal.ZERO)
            .descontoMaximo(request.descontoMaximo())
            .limiteUso(request.limiteUso())
            .quantidadeUsada(0)
            .validoDe(request.validoDe())
            .validoAte(request.validoAte())
            .ativo(true)
            .build();
        
        cupom = cupomRepository.save(cupom);
        return CupomResponse.from(cupom);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CupomResponse buscarCupom(UUID usuarioId, Long cupomId) {
        Cupom cupom = buscarCupomOuFalhar(cupomId);
        return CupomResponse.from(cupom);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CupomResponse buscarCupomPorCodigo(UUID usuarioId, String codigo) {
        Cupom cupom = cupomRepository.findByCodigo(codigo.toUpperCase())
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cupom não encontrado"));
        
        return CupomResponse.from(cupom);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CupomResponse> listarCupons(UUID usuarioId, Boolean ativo, Boolean validos, Pageable pageable) {
        Page<Cupom> cupons;
        
        if (validos != null && validos) {
            cupons = cupomRepository.findCuponsValidosComPaginacao(LocalDateTime.now(), pageable);
        } else if (ativo != null) {
            cupons = cupomRepository.findByAtivo(ativo, pageable);
        } else {
            cupons = cupomRepository.findAll(pageable);
        }
        
        return cupons.map(CupomResponse::from);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CupomResponse> listarCuponsValidos(UUID usuarioId) {
        return cupomRepository.findCuponsValidos(LocalDateTime.now())
            .stream()
            .map(CupomResponse::from)
            .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CupomResponse atualizarCupom(UUID usuarioId, Long cupomId, AtualizarCupomRequest request) {
        Cupom cupom = buscarCupomOuFalhar(cupomId);
        
        // Atualizar campos se fornecidos
        if (request.descricao() != null) {
            cupom.setDescricao(request.descricao());
        }
        
        if (request.valorDesconto() != null) {
            cupom.setValorDesconto(request.valorDesconto());
        }
        
        if (request.valorMinimoPedido() != null) {
            cupom.setValorMinimoPedido(request.valorMinimoPedido());
        }
        
        if (request.descontoMaximo() != null) {
            cupom.setDescontoMaximo(request.descontoMaximo());
        }
        
        if (request.limiteUso() != null) {
            cupom.setLimiteUso(request.limiteUso());
        }
        
        if (request.validoDe() != null) {
            cupom.setValidoDe(request.validoDe());
        }
        
        if (request.validoAte() != null) {
            cupom.setValidoAte(request.validoAte());
        }
        
        // Validar datas
        if (cupom.getValidoDe().isAfter(cupom.getValidoAte())) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Data de início deve ser anterior à data de fim");
        }
        
        cupom = cupomRepository.save(cupom);
        return CupomResponse.from(cupom);
    }
    
    @Override
    @Transactional
    public void ativarCupom(UUID usuarioId, Long cupomId) {
        Cupom cupom = buscarCupomOuFalhar(cupomId);
        cupom.ativar();
        cupomRepository.save(cupom);
    }
    
    @Override
    @Transactional
    public void desativarCupom(UUID usuarioId, Long cupomId) {
        Cupom cupom = buscarCupomOuFalhar(cupomId);
        cupom.desativar();
        cupomRepository.save(cupom);
    }
    
    @Override
    @Transactional
    public void removerCupom(UUID usuarioId, Long cupomId) {
        Cupom cupom = buscarCupomOuFalhar(cupomId);
        
        // Verificar se o cupom já foi usado
        if (cupom.getQuantidadeUsada() > 0) {
            throw APIException.build(HttpStatus.BAD_REQUEST, 
                "Não é possível remover cupom que já foi utilizado");
        }
        
        cupomRepository.delete(cupom);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ValidacaoCupomResponse validarCupom(UUID usuarioId, ValidarCupomRequest request) {
        // Buscar cupom
        Cupom cupom = cupomRepository.findByCodigo(request.codigo().toUpperCase())
            .orElse(null);
        
        if (cupom == null) {
            return ValidacaoCupomResponse.erro("Cupom não encontrado");
        }
        
        // Verificar se está ativo
        if (!cupom.getAtivo()) {
            return ValidacaoCupomResponse.erro("Cupom inativo");
        }
        
        // Verificar validade
        if (!cupom.isValido()) {
            if (cupom.isExpirado()) {
                return ValidacaoCupomResponse.erro("Cupom expirado");
            }
            if (cupom.atingiuLimiteUso()) {
                return ValidacaoCupomResponse.erro("Cupom atingiu o limite de uso");
            }
            return ValidacaoCupomResponse.erro("Cupom inválido");
        }
        
        // Calcular desconto
        try {
            BigDecimal desconto = cupom.calcularDesconto(request.valorPedido());
            BigDecimal valorFinal = request.valorPedido().subtract(desconto);
            
            return ValidacaoCupomResponse.sucesso(
                desconto,
                valorFinal,
                CupomResponse.from(cupom)
            );
        } catch (IllegalArgumentException e) {
            return ValidacaoCupomResponse.erro(e.getMessage());
        }
    }
    
    private Cupom buscarCupomOuFalhar(Long cupomId) {
        return cupomRepository.findById(cupomId)
            .orElseThrow(() -> APIException.build(HttpStatus.NOT_FOUND, "Cupom não encontrado"));
    }
}
