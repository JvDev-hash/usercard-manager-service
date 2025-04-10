package com.usercard.manager.service;

import com.usercard.manager.DTO.UsuarioDTO;
import com.usercard.manager.model.UserPermissions;
import com.usercard.manager.model.Usuario;
import com.usercard.manager.model.UsuarioPage;
import com.usercard.manager.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario inserirUsuario(UsuarioDTO usuarioDTO){
        var usuarioInserir = new Usuario();
        usuarioInserir.setEmail(usuarioDTO.getEmail());
        usuarioInserir.setNome(usuarioDTO.getNome());
        usuarioInserir.setPermissoes(UserPermissions.valueOf(usuarioDTO.getPermissoes()));
        usuarioInserir.setSenha(BCrypt.hashpw(usuarioDTO.getSenha(), BCrypt.gensalt(10)));

        return usuarioRepository.save(usuarioInserir);
    }

    public Usuario consultarUsuario(Long id){
       return usuarioRepository.findUsuarioById(id)
               .orElseThrow(() -> new EntityNotFoundException("There is no entity with this id: " + id));
    }

    public Usuario alterarUsuario(Long id, UsuarioDTO usuarioDTO){
        var usuarioAlterar = usuarioRepository.findUsuarioById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no entity with this id: " + id));

        usuarioAlterar.setEmail(usuarioDTO.getEmail());
        usuarioAlterar.setNome(usuarioDTO.getNome());
        usuarioAlterar.setPermissoes(UserPermissions.valueOf(usuarioDTO.getPermissoes()));
        if(usuarioDTO.getSenha() != null) {
            usuarioAlterar.setSenha(BCrypt.hashpw(usuarioDTO.getSenha(), BCrypt.gensalt(10)));
        }

        return usuarioRepository.save(usuarioAlterar);
    }

    public UsuarioPage listaUsuariosPagina(int pageNo, int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Usuario> aPage = usuarioRepository.findAll(pageable);
        return new UsuarioPage(aPage.getContent(), pageable, aPage.getContent().size());
    }

    public List<Usuario>listaUsuarios(){
        return usuarioRepository.findAll();
    }

    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }

}
