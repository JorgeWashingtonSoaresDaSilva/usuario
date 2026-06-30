package com.jwss.studio.usuario.infrastructure.exceptios;

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }
}
