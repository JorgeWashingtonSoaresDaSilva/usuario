package com.jwss.studio.usuario.business;

import com.jwss.studio.usuario.infrastructure.clients.ViaCepClient;
import com.jwss.studio.usuario.infrastructure.clients.ViaCepDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient viaCepClient;

    public ViaCepDTO buscarDadosEndereco(String cep){

        return viaCepClient.buscaDadosEndereco(processarCep(cep));

    }

    private  String processarCep(String cep){
        String cepFormatado = cep.replace(" ", "").replace("-","");
        if (!cepFormatado.matches("\\d+")
                || !Objects.equals(cepFormatado.length(), 8 )){
            throw new IllegalArgumentException("o cep contém carecteres invalidos, favor verificar");

        }
        return cepFormatado;
    }
}
