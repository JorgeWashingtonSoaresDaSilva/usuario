package com.jwss.studio.usuario.business;

import com.jwss.studio.usuario.business.converter.UsuarioConverter;
import com.jwss.studio.usuario.business.dto.UsuarioDTO;
import com.jwss.studio.usuario.infrastructure.entity.Usuario;
import com.jwss.studio.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return  usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
