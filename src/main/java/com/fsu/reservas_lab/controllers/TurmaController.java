package com.fsu.reservas_lab.controllers;

import com.fsu.reservas_lab.dtos.TurmaCreateRequest;
import com.fsu.reservas_lab.dtos.TurmaResponse;
import com.fsu.reservas_lab.dtos.TurmaResultResponse;
import com.fsu.reservas_lab.dtos.TurmaUpdateRequest;
import com.fsu.reservas_lab.services.TurmaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas-lab-facema/turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<TurmaResultResponse> createClass(@RequestBody TurmaCreateRequest dto) {
        return turmaService.saveClass(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TurmaResponse> getClass(@PathVariable Long id) {
        return turmaService.getClassById(id);
    }

    @GetMapping
    public ResponseEntity<List<TurmaResponse>> getAllClass() {
        return turmaService.getAllClasses();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<TurmaResultResponse> updateClass(@PathVariable Long id, @RequestBody TurmaUpdateRequest dto) {
        return turmaService.updateClass(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<Map<String, String>> deleteClass(@PathVariable Long id) {
        return turmaService.deleteClass(id);
    }
}
