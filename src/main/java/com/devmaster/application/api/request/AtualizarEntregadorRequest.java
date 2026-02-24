package com.devmaster.application.api.request;

import com.devmaster.domain.enums.CategoriaCNH;
import com.devmaster.domain.enums.TipoVeiculo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de requisição para atualizar dados de um entregador.
 * Todos os campos são opcionais para permitir atualização parcial.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record AtualizarEntregadorRequest(
    @Size(max = 255, message = "Nome completo não pode exceder 255 caracteres")
    String nomeCompleto,
    
    @Pattern(
        regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
        message = "Telefone deve estar no formato (XX) 9XXXX-XXXX ou (XX) XXXX-XXXX"
    )
    String telefone,
    
    @Email(message = "Email deve ser válido")
    @Size(max = 255, message = "Email não pode exceder 255 caracteres")
    String email,
    
    @Pattern(
        regexp = "^\\d{11}$",
        message = "CNH deve conter 11 dígitos"
    )
    String cnh,
    
    CategoriaCNH categoriaCnh,
    
    TipoVeiculo tipoVeiculo,
    
    @Pattern(
        regexp = "^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$",
        message = "Placa deve estar no formato ABC-1234 (antigo) ou ABC1D23 (Mercosul)"
    )
    String placaVeiculo,
    
    @Size(max = 100, message = "Modelo do veículo não pode exceder 100 caracteres")
    String modeloVeiculo,
    
    @Size(max = 50, message = "Cor do veículo não pode exceder 50 caracteres")
    String corVeiculo,
    
    String fotoPerfilUrl,
    
    String fotoCnhUrl,
    
    String fotoVeiculoUrl
) {}
