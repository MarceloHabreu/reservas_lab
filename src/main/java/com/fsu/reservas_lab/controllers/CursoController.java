package com.fsu.reservas_lab.controllers;

import com.fsu.reservas_lab.dtos.CursoCreateRequest;
import com.fsu.reservas_lab.dtos.CursoResultResponse;
import com.fsu.reservas_lab.dtos.CursoResponse;
import com.fsu.reservas_lab.dtos.CursoUpdateRequest;
import com.fsu.reservas_lab.services.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas-lab-facema/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<CursoResultResponse> createCourse(@RequestBody CursoCreateRequest dto) {
        return cursoService.saveCourse(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> getCourse(@PathVariable Long id) {
        return cursoService.getCourseById(id);
    }

    @GetMapping
    public ResponseEntity<List<CursoResponse>> getAllCourses() {
        return cursoService.getAllCourses();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<CursoResultResponse> updateCourse(@PathVariable Long id, @RequestBody CursoUpdateRequest dto) {
        return cursoService.updateCourse(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_COORDENADOR_LAB', 'SCOPE_REITORIA')")
    public ResponseEntity<Map<String, String>> deleteCourse(@PathVariable Long id) {
        return cursoService.deleteCourse(id);
    }
}
