package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.turma.TurmaCreateRequest;
import com.fsu.reservas_lab.dtos.turma.TurmaResponse;
import com.fsu.reservas_lab.dtos.turma.TurmaResultResponse;
import com.fsu.reservas_lab.dtos.turma.TurmaUpdateRequest;
import com.fsu.reservas_lab.entities.Curso;
import com.fsu.reservas_lab.entities.Turma;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.exceptions.curso.CourseNotFoundException;
import com.fsu.reservas_lab.exceptions.turma.ClassAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.turma.ClassNotFoundException;
import com.fsu.reservas_lab.exceptions.turma.TeacherNotAllowedException;
import com.fsu.reservas_lab.exceptions.usuario.UserNotFoundException;
import com.fsu.reservas_lab.repositories.CursoRepository;
import com.fsu.reservas_lab.repositories.TurmaRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public TurmaService(TurmaRepository turmaRepository, CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.turmaRepository = turmaRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ResponseEntity<TurmaResultResponse> saveClass(TurmaCreateRequest dto) {
        if (turmaRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new ClassAlreadyExistsException();
        }

        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(CourseNotFoundException::new);
        Usuario professor = usuarioRepository.findById(dto.professorId()).orElseThrow(() -> new UserNotFoundException("Professor não encontrado!"));
        if (!curso.getUsuarios().contains(professor)) {
            throw new TeacherNotAllowedException("Esse professor não pertence ao curso!");
        }
        Turma turma = new Turma();
        turma.setCodigo(dto.codigo());
        turma.setDisciplina(dto.disciplina());
        turma.setNumeroAlunos(dto.numeroAlunos());
        turma.setPeriodoLetivo(dto.periodoLetivo());
        turma.setCurso(curso);
        turma.setProfessor(professor);

        turma = turmaRepository.save(turma);

        var response = new TurmaResultResponse("Turma criada com sucesso", TurmaResponse.fromEntity(turma));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<TurmaResponse> getClassById(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(ClassNotFoundException::new);
        return ResponseEntity.ok(TurmaResponse.fromEntity(turma));
    }

    public ResponseEntity<List<TurmaResponse>> getAllClasses() {
        List<TurmaResponse> turmas = turmaRepository.findAll()
                .stream()
                .map(TurmaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(turmas);
    }

    @Transactional
    public ResponseEntity<TurmaResultResponse> updateClass(Long id, TurmaUpdateRequest dto) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(ClassNotFoundException::new);

        if (!turma.getCodigo().equals(dto.codigo()) &&
                turmaRepository.findByCodigo(dto.codigo()).isPresent()) {
            throw new ClassNotFoundException();
        }

        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(CourseNotFoundException::new);
        Usuario professor = usuarioRepository.findById(dto.professorId()).orElseThrow(() -> new UserNotFoundException("Professor não encontrado!"));
        if (!curso.getUsuarios().contains(professor)) {
            throw new TeacherNotAllowedException("Esse solicitante não pertence ao curso!");
        }

        turma.setCodigo(dto.codigo());
        turma.setDisciplina(dto.disciplina());
        turma.setNumeroAlunos(dto.numeroAlunos());
        turma.setPeriodoLetivo(dto.periodoLetivo());
        turma.setCurso(curso);
        turma.setProfessor(professor);

        turma = turmaRepository.save(turma);

        var response = new TurmaResultResponse("Turma atualizada com sucesso", TurmaResponse.fromEntity(turma));
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteClass(Long id) {
        if (!turmaRepository.existsById(id)) {
            throw new ClassNotFoundException();
        }

        turmaRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Turma deletada com sucesso");
        return ResponseEntity.ok(response);
    }
}
