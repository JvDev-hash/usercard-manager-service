package com.usercard.manager.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CartaoPage extends PageImpl<Cartao> {
    public CartaoPage(List<Cartao> content, Pageable pageable, long total){
        super(content, pageable, total);
    }
}
