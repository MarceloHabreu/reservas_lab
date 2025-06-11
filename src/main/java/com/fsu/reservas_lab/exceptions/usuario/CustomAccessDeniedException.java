package com.fsu.reservas_lab.exceptions.usuario;

import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {
    public CustomAccessDeniedException() {
        super("Você não tem permissões suficiente para esta ação.");
    }
}
