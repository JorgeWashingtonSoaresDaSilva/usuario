package com.jwss.studio.usuario.infrastructure.exceptios;

import javax.naming.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String mensagem) {
        super(mensagem);
    }
    public UnauthorizedException(String mensagem, Throwable throwable) {
        super(mensagem);
    }
}
