package com.testeEloTech.Controller;

import com.testeEloTech.Model.ContatoModel;
import com.testeEloTech.Model.PessoaModel;

import java.util.List;

public class CadastroRequest {
    private String nome;
    private String cpf;
    private String dataNascimento;
    private List<ContatoModel> contatos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<ContatoModel> getContatos() {
        return contatos;
    }

    public void setContatos(List<ContatoModel> contatos) {
        this.contatos = contatos;
    }

    public CadastroRequest() {
    }

    public PessoaModel getPessoaModel() {
        PessoaModel pessoa = new PessoaModel();
        pessoa.setNome(nome);
        pessoa.setCPF(cpf);
        pessoa.setDataNascimento(dataNascimento);
        pessoa.setContatos(contatos);
        return pessoa;
    }


}
