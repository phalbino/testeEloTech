package com.testeEloTech.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@Data
@Entity
@Table(name = "Pessoa")
public class PessoaModel {

    @Id
    @Column
    private Long Id;

    @Column
    private String Nome;

    @Column
    private String CPF;

    @Column
    private String DataNascimento;

    @Transient
    @JsonIgnore
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContatoModel> contatos;

    @PrePersist
    public void generateId() {
        this.Id = new Date().getTime();
    }

}
