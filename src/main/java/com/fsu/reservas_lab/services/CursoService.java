package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.curso.CursoResultResponse;
import com.fsu.reservas_lab.dtos.curso.CursoCreateRequest;
import com.fsu.reservas_lab.dtos.curso.CursoResponse;
import com.fsu.reservas_lab.dtos.curso.CursoUpdateRequest;
import com.fsu.reservas_lab.entities.Curso;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.exceptions.curso.CourseAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.curso.CourseNotFoundException;
import com.fsu.reservas_lab.exceptions.usuario.UserNotFoundException;
import com.fsu.reservas_lab.repositories.CursoRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public CursoService(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ResponseEntity<CursoResultResponse> saveCourse(CursoCreateRequest dto) {
        if (cursoRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new CourseAlreadyExistsException();
        }

        // Buscar o coordenador do banco
        Usuario coordenador = usuarioRepository.findById(dto.coordenadorCurso())
                .orElseThrow(() -> new UserNotFoundException("Coordenador não encontrado!"));

        Curso curso = new Curso();
        curso.setNome(dto.nome());
        curso.setCodigo(dto.codigo());
        curso.setCoordenador(coordenador);
        curso = cursoRepository.save(curso);

        var response = new CursoResultResponse("Curso criado com sucesso", CursoResponse.fromEntity(curso));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    public ResponseEntity<CursoResponse> getCourseById(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);
        return ResponseEntity.ok(CursoResponse.fromEntity(curso));
    }

    public ResponseEntity<List<CursoResponse>> getAllCourses() {
        List<CursoResponse> cursos = cursoRepository.findAll()
                .stream()
                .map(CursoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(cursos);
    }

    @Transactional
    public ResponseEntity<CursoResultResponse> updateCourse(Long id, CursoUpdateRequest dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);
        if (!curso.getCodigo().equals(dto.codigo()) && cursoRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new CourseAlreadyExistsException();
        }

        // Buscar o coordenador no banco
        Usuario coordenador = usuarioRepository.findById(dto.coordenadorCurso())
                .orElseThrow(() -> new UserNotFoundException("Coordenador não encontrado!"));

        curso.setNome(dto.nome());
        curso.setCodigo(dto.codigo());
        curso.setCoordenador(coordenador);
        curso = cursoRepository.save(curso);

        var response = new CursoResultResponse("Curso atualizado com sucesso", CursoResponse.fromEntity(curso));
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteCourse(Long id) {

        if (!cursoRepository.existsById(id)) {
            throw new CourseNotFoundException();
        }
        cursoRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Curso deletado com sucesso");
        return ResponseEntity.ok(response);
    }
}