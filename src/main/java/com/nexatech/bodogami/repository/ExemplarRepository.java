package com.nexatech.bodogami.repository;

import com.nexatech.bodogami.entity.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExemplarRepository extends JpaRepository<Exemplar, Integer> {

    Exemplar findFirstByJogoId(Integer idJogo);

    long countByJogoId(Integer idJogo);
}
