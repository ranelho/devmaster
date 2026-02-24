package com.devmaster.application.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

/**
 * DTO de requisição para criar um novo cliente.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
public record ClienteRequest(
    
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
        regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
        message = "Telefone deve estar no formato (XX) 9XXXX-XXXX ou (XX) XXXX-XXXX"
    )
    String telefone,
    
    @Email(message = "Email deve ser válido")
    @Size(max = 255, message = "Email não pode exceder 255 caracteres")
    String email,
    
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(max = 255, message = "Nome completo não pode exceder 255 caracteres")
    String nomeCompleto,
    
    @Pattern(
        regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
        message = "CPF deve estar no formato XXX.XXX.XXX-XX"
    )
    @CPF
    String cpf,
    
    @Past(message = "Data de nascimento deve ser no passado")
    LocalDate dataNascimento,
    
    @Valid
    EnderecoClienteRequest endereco
) {}
