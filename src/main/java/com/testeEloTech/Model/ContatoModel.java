package com.testeEloTech.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "Contato")
public class ContatoModel {

    @Id
    @Column
    private Long id;

    @Column
    private String nome;
    @Column
    private String telefone;
    @Column
    private String email;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private PessoaModel pessoa;

    @PrePersist
    public void generateId() {
        this.id = System.currentTimeMillis();
    }
}
