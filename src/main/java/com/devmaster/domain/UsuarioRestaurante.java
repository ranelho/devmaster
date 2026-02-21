package com.devmaster.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa o vínculo entre um usuário e um restaurante.
 * Define a role (ADMIN, GERENTE, ATENDENTE) do usuário no restaurante.
 * 
 * @author DevMaster Team
 * @since 1.0.0
 */
@Entity
@Table(name = "usuarios_restaurante")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRestaurante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RoleRestaurante role;
    
    @Column(nullable = false)
    private Boolean ativo;
    
    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;
    
    @Column(name = "criado_por")
    private UUID criadoPor;
    
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
    
    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        if (ativo == null) {
            ativo = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
    
    /**
     * Ativa o vínculo do usuário com o restaurante.
     */
    public void ativar() {
        this.ativo = true;
    }
    
    /**
     * Desativa o vínculo do usuário com o restaurante.
     */
    public void desativar() {
        this.ativo = false;
    }
    
    /**
     * Verifica se o usuário é ADMIN do restaurante.
     */
    public boolean isAdmin() {
        return role == RoleRestaurante.ADMIN;
    }
    
    /**
     * Verifica se o usuário é GERENTE do restaurante.
     */
    public boolean isGerente() {
        return role == RoleRestaurante.GERENTE;
    }
    
    /**
     * Verifica se o usuário é ATENDENTE do restaurante.
     */
    public boolean isAtendente() {
        return role == RoleRestaurante.ATENDENTE;
    }
    
    /**
     * Verifica se o usuário pode gerenciar outros usuários.
     * ADMIN pode gerenciar GERENTE e ATENDENTE.
     * GERENTE pode gerenciar apenas ATENDENTE.
     */
    public boolean podeGerenciarUsuarios() {
        return role == RoleRestaurante.ADMIN || role == RoleRestaurante.GERENTE;
    }
    
    /**
     * Verifica se o usuário pode gerenciar produtos.
     * ADMIN e GERENTE podem gerenciar produtos.
     */
    public boolean podeGerenciarProdutos() {
        return role == RoleRestaurante.ADMIN || role == RoleRestaurante.GERENTE;
    }
    
    /**
     * Enum que define os papéis de um usuário em um restaurante.
     */
    public enum RoleRestaurante {
        /**
         * Administrador do restaurante.
         * Pode gerenciar tudo no restaurante, incluindo criar GERENTE e ATENDENTE.
         */
        ADMIN,
        
        /**
         * Gerente do restaurante.
         * Pode gerenciar produtos, categorias, cupons e criar ATENDENTE.
         */
        GERENTE,
        
        /**
         * Atendente do restaurante.
         * Pode visualizar e atualizar pedidos, disponibilizar produtos.
         */
        ATENDENTE
    }
}
