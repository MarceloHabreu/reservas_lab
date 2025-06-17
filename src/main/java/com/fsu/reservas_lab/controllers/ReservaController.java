package com.fsu.reservas_lab.controllers;

import com.fsu.reservas_lab.dtos.reserva.*;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import com.fsu.reservas_lab.services.AprovacaoReservaService;
import com.fsu.reservas_lab.services.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas-lab-facema/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final AprovacaoReservaService aprovacaoReservaService;

    public ReservaController(ReservaService reservaService, AprovacaoReservaService aprovacaoReservaService) {
        this.reservaService = reservaService;
        this.aprovacaoReservaService = aprovacaoReservaService;
    }

    @PostMapping("/{laboratorioId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_PROFESSOR', 'SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<ReservaResultResponse> createReserva(@PathVariable Long laboratorioId, @RequestBody ReservaCreateRequest request, JwtAuthenticationToken userAuthenticated) {
        return reservaService.createReservation(laboratorioId, request, userAuthenticated.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> getReserva(@PathVariable Long id) {
        return reservaService.getReservation(id);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> getAllReservas() {
        return reservaService.getAllReservas();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_PROFESSOR', 'SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<ReservaResultResponse> updateReserva(@PathVariable Long id, @RequestBody ReservaUpdateRequest request, JwtAuthenticationToken userAuthenticated) {
        return reservaService.updateReservation(id, request, userAuthenticated.getName());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_PROFESSOR', 'SCOPE_REITORIA')")
    public ResponseEntity<Map<String, String>> deleteReserva(@PathVariable Long id) {
        return reservaService.deleteReservation(id);
    }

    // ======== AÇÕES ADMINISTRATIVAS ========

    @PostMapping("/{id}/aprovar")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_COORDENADOR_CURSO', 'SCOPE_REITORIA')")
    public ResponseEntity<ReservaResponse> approveReserva(
            @PathVariable Long id,
            @RequestBody AprovacaoReservaDTO dto, JwtAuthenticationToken token) {
        return aprovacaoReservaService.approveReservation(id, dto, token);
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_COORDENADOR_CURSO', 'SCOPE_REITORIA')")
    public ResponseEntity<ReservaResponse> cancelReserva(
            @PathVariable Long id,
            @RequestParam(required = false) String observacao, JwtAuthenticationToken token) {
        return aprovacaoReservaService.cancelReservation(id, observacao, token);
    }

    // ======== FILTROS E CONSULTAS ESPECÍFICAS ========

    @GetMapping("/por-status")
    public ResponseEntity<List<ReservaResponse>> findByStatus(@RequestParam StatusReserva status) {
        return reservaService.findReservationsByStatus(status);
    }

    @GetMapping("/por-solicitante/{idSolicitante}")
    public ResponseEntity<List<ReservaResponse>> findByApplicant(@PathVariable Long idSolicitante) {
        return reservaService.findReservationsByApplicant(idSolicitante);
    }

    @GetMapping("/por-data")
    public ResponseEntity<List<ReservaResponse>> findByDate(@RequestParam String data) {
        return reservaService.findReservationsByDate(LocalDate.parse(data));
    }
}
