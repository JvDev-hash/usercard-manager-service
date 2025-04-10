package com.usercard.manager.DTO;

import com.usercard.manager.model.TipoCartao;

public class CartaoDTO {

    private Long numeroCartao;

    private String nome;

    private Boolean status;

    private TipoCartao tipoCartao;

    public CartaoDTO() {
    }

    public CartaoDTO(Long numeroCartao, String nome, Boolean status, TipoCartao tipoCartao) {
        this.numeroCartao = numeroCartao;
        this.nome = nome;
        this.status = status;
        this.tipoCartao = tipoCartao;
    }

    public Long getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(Long numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public TipoCartao getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(TipoCartao tipoCartao) {
        this.tipoCartao = tipoCartao;
    }
}
