package com.usercard.manager.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioDTO {

    private String nome;

    private String email;

    private String senha;

    private String permissoes;

    public UsuarioDTO(String nome, String email, String senha, String permissoes) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.permissoes = permissoes;
    }

    public UsuarioDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }
}
