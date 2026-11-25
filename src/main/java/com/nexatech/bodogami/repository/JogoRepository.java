package com.nexatech.bodogami.repository;

import com.nexatech.bodogami.entity.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Integer> {

    List<Jogo> findByNomeContainingIgnoreCase(String nome);
}
