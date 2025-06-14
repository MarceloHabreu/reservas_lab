package com.fsu.reservas_lab.controllers;

import com.fsu.reservas_lab.dtos.usuario.UsuarioCreateRequest;
import com.fsu.reservas_lab.dtos.usuario.UsuarioResponse;
import com.fsu.reservas_lab.dtos.usuario.UsuarioResultResponse;
import com.fsu.reservas_lab.dtos.usuario.UsuarioUpdateRequest;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas-lab-facema/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<UsuarioResultResponse> createUsuario(@Valid @RequestBody UsuarioCreateRequest dto) {
        return usuarioService.createUsuario(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUsuario(@PathVariable Long id, JwtAuthenticationToken token, @RequestParam Optional<TipoUsuario> tipo) {
        return usuarioService.getUsuarioById(id, token.getName(), tipo);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios(
            JwtAuthenticationToken token,
            @RequestParam Optional<TipoUsuario> tipo) {
        return usuarioService.getAllUsuarios(token.getName(), tipo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_REITORIA')")
    public ResponseEntity<UsuarioResultResponse> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateRequest dto,
            JwtAuthenticationToken token) {
        return usuarioService.updateUsuario(id, dto, token.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<Map<String, String>> deleteUsuario(
            @PathVariable Long id,
            JwtAuthenticationToken token) {
        return usuarioService.deleteUsuario(id, token.getName());
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResultResponse> updateOwnAccount(@Valid @RequestBody UsuarioUpdateRequest dto,
                                                 JwtAuthenticationToken token) {
        return usuarioService.updateOwnAccount(dto, token.getName());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteOwnAccount(JwtAuthenticationToken token) {
        return usuarioService.deleteOwnAccount(token.getName());
    }
}