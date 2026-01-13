package com.devmaster.cliente.domain;

import com.devmaster.cliente.application.api.ContatoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contato")
@EntityListeners(AuditingEntityListener.class)
public class Contato {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idContato;
    private String telefone;
    private String celular;
    private String email;

    @CreatedDate
    LocalDateTime createdAt;
    @LastModifiedDate
    LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    Cliente cliente;

    public Contato(ContatoRequest contatoRequest, Cliente cliente) {
        this.telefone = contatoRequest.telefone();
        this.celular = contatoRequest.celular();
        this.email = contatoRequest.email();
        this.cliente = cliente;
    }
}