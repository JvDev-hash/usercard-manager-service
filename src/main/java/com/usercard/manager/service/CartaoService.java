package com.usercard.manager.service;

import com.usercard.manager.DTO.CartaoDTO;
import com.usercard.manager.model.Cartao;
import com.usercard.manager.model.CartaoPage;
import com.usercard.manager.repository.CartaoRepository;
import com.usercard.manager.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CartaoService {

    private final CartaoRepository cartaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CartaoService(CartaoRepository cartaoRepository, UsuarioRepository usuarioRepository){
        this.cartaoRepository = cartaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Cartao cadastraCartao(Long usuarioId, CartaoDTO cartaoDTO){
        var usuario = usuarioRepository.findUsuarioById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with this id: " + usuarioId));

        var cartao = new Cartao();
        cartao.setNumeroCartao(cartaoDTO.getNumeroCartao());
        cartao.setTipoCartao(cartaoDTO.getTipoCartao());
        cartao.setNome(cartaoDTO.getNome());
        cartao.setStatus(cartaoDTO.getStatus());

        var cartoes = usuario.getCartoes();
        cartoes.add(cartao);

        usuario.setCartoes(cartoes);
        usuarioRepository.save(usuario);

        return cartao;
    }

    public Page<Cartao> listaCartoes(int pageNo, int pageSize){

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Cartao> aPage = cartaoRepository.listaCartoes(pageable);
        return new CartaoPage(aPage.getContent(), pageable, aPage.getContent().size());
    }

    public void deletaCartao(Long usuarioId){
        var cartao = cartaoRepository.findCartaoById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with this id: " + usuarioId));

        cartaoRepository.delete(cartao);
    }

    public Cartao updateCartao(Long usuarioId, Boolean status){
        var cartao = cartaoRepository.findCartaoById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with this id: " + usuarioId));
        cartao.setStatus(status);
        cartaoRepository.save(cartao);

        return cartao;
    }
}
