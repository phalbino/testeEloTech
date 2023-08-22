package com.testeEloTech.Service;

import com.testeEloTech.Model.ContatoModel;
import com.testeEloTech.Repository.ContatoDao;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Service
public class ContatoService {
    @Autowired
    private ContatoDao contatoDao;
    final String SUCESSO = "Contato Criado com Sucesso.";
    final String ERRO = "Erro ao criar Contato.";
    final String ERRONOME = "Nome nulo ou vazio";
    final String ERROTELEFONE = "Telefone nulo ou Vazio.";
    final String ERROEMAIL = "Enderço de Email nulo ou Vazio.";

        public String verificaCamposContatos(ContatoModel contato) {
            if (contato == null) {
                return ERRO;
            }

                if (contato.getNome() == null || contato.getNome().isEmpty()) {
                    return ERRONOME;
                } else if (contato.getTelefone() == null || contato.getTelefone().isEmpty()) {
                    return ERROTELEFONE;
                } else if (contato.getEmail() == null || contato.getEmail().isEmpty()) {
                    return ERROEMAIL;
                }

            return SUCESSO;
        }

    public Boolean validarSintaxeEmail(ContatoModel contatoModel) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        return !contatoModel.getEmail().matches(regex);
    }

}


