package com.testeEloTech.Repository;

import com.testeEloTech.Model.PessoaModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PessoaDao extends CrudRepository<PessoaModel, String> {
    List<PessoaModel> findByCPFContains(String CPF);

    Page<PessoaModel> findByNomeContainingIgnoreCase(String filtro, Pageable pageable);

    Object findAll(Pageable pageable);

    Optional<PessoaModel> findByCPF(String s);
}
