package com.fsu.reservas_lab.controllers;

import com.fsu.reservas_lab.dtos.auth.AuthResponse;
import com.fsu.reservas_lab.dtos.auth.LoginRequest;
import com.fsu.reservas_lab.dtos.auth.RegisterRequest;
import com.fsu.reservas_lab.services.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas-lab-facema/auth")
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return tokenService.login(loginRequest);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponse> cadastro(@RequestBody RegisterRequest registerRequest) {
        return tokenService.cadastro(registerRequest);
    }
}
