package com.testeEloTech.Service;

import com.testeEloTech.Model.PessoaModel;
import com.testeEloTech.Repository.PessoaDao;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Service
public class PessoaService {

    @Autowired
    private PessoaDao pessoa;
    @Autowired
    private PessoaDao pessoaRepository;
    final String SUCESSO = "Pessoa Criada com Sucesso.";
    final String ERRO = "Erro ao criar pessoa.";
    final String ERRONOME = "Nome nulo ou vazio";
    final String ERROCPF = "CPF nulo ou Vazio.";
    final String ERRODT_NASCIMENTO = "Data de Nascimento nula ou vazia";
    public String verificaCamposPessoa(PessoaModel pessoa) {
        if (pessoa == null) {
            return ERRO;
        }
            if(pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
                return ERRONOME;
            } else if (pessoa.getCPF() == null || pessoa.getCPF().isEmpty()) {
                return ERROCPF;
            } else if (pessoa.getDataNascimento() == null || pessoa.getDataNascimento().isEmpty()) {
                return ERRODT_NASCIMENTO;
            }

        return SUCESSO;
    }

    public Boolean pessoaPersistida (PessoaModel pessoaModel){
        String CPF = pessoaModel.getCPF();

        CPF = CPF.replace(".", "").trim();
        CPF = CPF.replace("-", "").trim();
        CPF = CPF.replace("/", "").trim();
        pessoaModel.setCPF(CPF);

        return pessoaRepository.findByCPFContains(CPF).isEmpty();
    }
    public Boolean isCpf(PessoaModel pessoaModel) {
        
        Long.parseLong(pessoaModel.getCPF());

        int d1, d2;
        int digito1, digito2, resto;
        int digitoCPF;
        String nDigResult;

        d1 = d2 = 0;
        digito1 = digito2 = resto = 0;

        for (int nCount = 1; nCount < pessoaModel.getCPF().length() - 1; nCount++) {
            digitoCPF = Integer.valueOf(pessoaModel.getCPF().substring(nCount - 1, nCount)).intValue();
            d1 = d1 + (11 - nCount) * digitoCPF;
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        ;

        resto = (d1 % 11);

        if (resto < 2)
            digito1 = 0;
        else
            digito1 = 11 - resto;

        d2 += 2 * digito1;
        resto = (d2 % 11);

        if (resto < 2)
            digito2 = 0;
        else
            digito2 = 11 - resto;

        String nDigVerific = pessoaModel.getCPF().substring(pessoaModel.getCPF().length() - 2, pessoaModel.getCPF().length());

        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
        return nDigVerific.equals(nDigResult);
    }

    public Boolean verificarDataNascimentoFutura(PessoaModel pessoaModel) {
        LocalDate dataNascimento = LocalDate.parse(pessoaModel.getDataNascimento(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate dataAtual = LocalDate.now();

       return !dataNascimento.isAfter(dataAtual);
    }
}

