package com.usercard.manager.repository;

import com.usercard.manager.model.Cartao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    @Query(value = "SELECT * FROM tbl_cards ORDER BY id DESC", nativeQuery = true)
    Page<Cartao> listaCartoes(Pageable pageable);

    @Query(value = "SELECT * FROM tbl_cards c WHERE c.id = :id", nativeQuery = true)
    Optional<Cartao> findCartaoById(@Param("id") Long id);
}
