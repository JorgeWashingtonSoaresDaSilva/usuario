package com.jwss.studio.usuario.business;

import com.jwss.studio.usuario.business.converter.UsuarioConverter;
import com.jwss.studio.usuario.business.dto.EnderecoDTO;
import com.jwss.studio.usuario.business.dto.TelefoneDTO;
import com.jwss.studio.usuario.business.dto.UsuarioDTO;
import com.jwss.studio.usuario.infrastructure.entity.Endereco;
import com.jwss.studio.usuario.infrastructure.entity.Telefone;
import com.jwss.studio.usuario.infrastructure.entity.Usuario;
import com.jwss.studio.usuario.infrastructure.excepitios.ConflictExcepition;
import com.jwss.studio.usuario.infrastructure.excepitios.ResourceNotFoundExcepition;
import com.jwss.studio.usuario.infrastructure.repository.EnderecoRepository;
import com.jwss.studio.usuario.infrastructure.repository.TelefoneRepository;
import com.jwss.studio.usuario.infrastructure.repository.UsuarioRepository;
import com.jwss.studio.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final EnderecoRepository enderecoRepository;
    private  final TelefoneRepository telefoneRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
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
    public UsuarioDTO buscaUsuarioPorEmail(String email){
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundExcepition("Email não encontrado" + email)
            ));

        }catch (ResourceNotFoundExcepition e){
            throw new ResourceNotFoundExcepition("Email não encontrado " + email);
        }
    }

    public void deletaUsuarioPorEmail(String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizacoesUsuario(String token, UsuarioDTO dto){
        // aqui buscamos o email do usuario atraves do token ( para tirar obrigatóriedade do email)
        String email = jwtUtil.exttraiEmailToken(token.substring(7));
        // verificamos se senha foi alterada para podermos criptogafar, caso null não fazemos nada
        dto.setSenha(dto.getSenha() != null ? passwordEncoder.encode(dto.getSenha()) : null);
        // buscamos dados do usuario do banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow( () ->
        new ResourceNotFoundExcepition("Email não localizado"));
        // mesclamos os dados que recebemos na riquisição DTO com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(dto,usuarioEntity);

        // salvamos os dados do usuario convertido e depois pegamos o retorno e covertemos para usuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(
                ()->new ResourceNotFoundExcepition("Id não encontrado "+ idEndereco));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO,entity);
       return usuarioConverter.paraEnderecoDTO( enderecoRepository.save(endereco));
    }
    public TelefoneDTO atualizaTelefone (Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                ()->new ResourceNotFoundExcepition("Id não encontrado "+ idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO,entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }



}
