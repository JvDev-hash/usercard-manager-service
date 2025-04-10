package com.usercard.manager.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class UsuarioPage extends PageImpl<Usuario> {
    public UsuarioPage(List<Usuario> content, Pageable pageable, long total){
        super(content, pageable, total);
    }
}
