package com.nexatech.bodogami.repository;

import com.nexatech.bodogami.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);
}