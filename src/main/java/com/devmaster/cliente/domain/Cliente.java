package com.devmaster.cliente.domain;

import com.devmaster.cliente.application.api.request.ClienteRequest;
import com.devmaster.cliente.application.api.request.ContatoUpdateRequest;
import com.devmaster.cliente.application.api.request.EditaClienteRequest;
import com.devmaster.cliente.domain.enums.EstadoCivil;
import com.devmaster.cliente.domain.enums.TipoPessoa;
import com.devmaster.cliente.domain.groups.ClienteGroupSequenceProvider;
import com.devmaster.cliente.domain.groups.PessoaFisica;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.group.GroupSequenceProvider;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@GroupSequenceProvider(value = ClienteGroupSequenceProvider.class)
@EntityListeners(AuditingEntityListener.class)
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idCliente;

    @Transient
    private TipoPessoa tipoPessoa = TipoPessoa.FISICA;
    @CPF(groups = PessoaFisica.class, message = "CPF inválido!")

    @Column(unique = true)
    private String cpf;
    @NotNull(message = "Campo Obrigatório!")
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private String naturalidade;
    private String nacionalidade;

    @Enumerated(EnumType.STRING)
    private EstadoCivil estadoCivil;

    @CreatedDate
    LocalDateTime createdAt;
    @LastModifiedDate
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();

    public Cliente(String cpf, ClienteRequest clienteRequest) {
        this.tipoPessoa = getTipoPessoa();
        this.cpf = cpf.isEmpty() ? clienteRequest.cpf() : cpf;
        this.nomeCompleto = clienteRequest.nomeCompleto().toUpperCase();
        this.dataNascimento = clienteRequest.dataNascimento();
        this.naturalidade = clienteRequest.naturalidade().toUpperCase();
        this.nacionalidade = clienteRequest.nacionalidade().toUpperCase();
        this.estadoCivil = clienteRequest.estadoCivil();
        this.contatos = List.of(new Contato(clienteRequest.contatoRequest(), this));
    }

    public void update(EditaClienteRequest editaClienteRequest) {
        this.nomeCompleto = editaClienteRequest.firstName().toUpperCase();
        this.dataNascimento = editaClienteRequest.dataNascimento();
        this.naturalidade = editaClienteRequest.naturalidade().toUpperCase();
        this.nacionalidade = editaClienteRequest.nacionalidade().toUpperCase();
        this.estadoCivil = editaClienteRequest.estadoCivil();
        
        if (editaClienteRequest.contatos() != null) {
            sincronizaContatos(editaClienteRequest.contatos());
        }
    }
    
    private void sincronizaContatos(List<ContatoUpdateRequest> novosContatos) {
        this.contatos.removeIf(contato -> 
            novosContatos.stream().noneMatch(nc -> contato.getIdContato().equals(nc.idContato()))
        );
        
        novosContatos.forEach(novoContato -> {
            if (novoContato.idContato() == null) {
                Contato contato = new Contato();
                contato.setTelefone(novoContato.telefone());
                contato.setCelular(novoContato.celular());
                contato.setEmail(novoContato.email());
                contato.setCliente(this);
                this.contatos.add(contato);
            } else {
                this.contatos.stream()
                    .filter(c -> c.getIdContato().equals(novoContato.idContato()))
                    .findFirst()
                    .ifPresent(c -> {
                        c.setTelefone(novoContato.telefone());
                        c.setCelular(novoContato.celular());
                        c.setEmail(novoContato.email());
                    });
            }
        });
    }

}