package com.jwss.studio.usuario.business;

import com.jwss.studio.usuario.business.converter.UsuarioConverter;
import com.jwss.studio.usuario.business.dto.UsuarioDTO;
import com.jwss.studio.usuario.infrastructure.entity.Usuario;
import com.jwss.studio.usuario.infrastructure.excepitios.ConflictExcepition;
import com.jwss.studio.usuario.infrastructure.excepitios.ResourceNotFoundExcepition;
import com.jwss.studio.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(PasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return  usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email){
        try {
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new RuntimeException("Email já cadastrado "+ email);
            }
        }catch (ConflictExcepition e){
            throw new ConflictExcepition("Email já cadastrado"+ e.getCause());
        }
    }
    public boolean verificaEmailExistente(String email ){
        return usuarioRepository.existsByEmail(email);

    }
    public Usuario buscaUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundExcepition("Email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }
}
