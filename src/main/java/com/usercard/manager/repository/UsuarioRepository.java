package com.usercard.manager.repository;

import com.usercard.manager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "SELECT * FROM tbl_users u WHERE u.id = :usuarioId", nativeQuery = true)
    Optional<Usuario> findUsuarioById(@Param("usuarioId") Long usuarioId);
}
