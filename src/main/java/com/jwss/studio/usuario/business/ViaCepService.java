package com.jwss.studio.usuario.business;

import com.jwss.studio.usuario.infrastructure.clients.ViaCepClient;
import com.jwss.studio.usuario.infrastructure.clients.ViaCepDTO;
import com.jwss.studio.usuario.infrastructure.exceptios.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient viaCepClient;

    public ViaCepDTO buscarDadosEndereco(String cep){
        try {
            return viaCepClient.buscaDadosEndereco(processarCep(cep));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Erro: "+ e);
        }
    }

    private  String processarCep(String cep){
        String cepFormatado = cep.replace(" ", "").replace("-","");
        if (!cepFormatado.matches("\\d+")
                || !Objects.equals(cepFormatado.length(), 8 )){
            throw new IllegalArgumentException("O cep contém carecteres invalidos, favor verificar");

        }
        return cepFormatado;
    }
}
