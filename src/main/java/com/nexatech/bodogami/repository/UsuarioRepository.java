package com.nexatech.bodogami.repository;

import com.nexatech.bodogami.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    boolean existsByEmail(String email);
    Optional<Usuario> findByEmail(String email);
}