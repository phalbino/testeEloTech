package com.testeEloTech.Repository;
import com.testeEloTech.Model.ContatoModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ContatoDao extends CrudRepository<ContatoModel, Long> {
       List<ContatoModel> findByIdContains(Long Id);

    Optional<ContatoModel> findById(Long Id);

}
