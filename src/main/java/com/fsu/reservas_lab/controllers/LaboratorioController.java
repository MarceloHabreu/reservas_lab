package com.fsu.reservas_lab.controllers;

import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioCreateRequest;
import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioResponse;
import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioResultResponse;
import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioUpdateRequest;
import com.fsu.reservas_lab.services.LaboratorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas-lab-facema/laboratorios")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<LaboratorioResultResponse> createLaboratory(@RequestBody LaboratorioCreateRequest dto) {
        return laboratorioService.saveLaboratory(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratorioResponse> getLaboratory(@PathVariable Long id) {
        return laboratorioService.getLaboratoryById(id);
    }

    @GetMapping
    public ResponseEntity<List<LaboratorioResponse>> getAllLaboratories() {
        return laboratorioService.getAllLaboratories();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<LaboratorioResultResponse> updateLaboratory(@PathVariable Long id, @RequestBody LaboratorioUpdateRequest dto) {
        return laboratorioService.updateLaboratory(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<Map<String, String>> deleteLaboratory(@PathVariable Long id) {
        return laboratorioService.deleteLaboratory(id);
    }
}
