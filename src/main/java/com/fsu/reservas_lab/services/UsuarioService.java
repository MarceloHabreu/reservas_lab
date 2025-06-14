package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.usuario.UsuarioCreateRequest;
import com.fsu.reservas_lab.dtos.usuario.UsuarioResponse;
import com.fsu.reservas_lab.dtos.usuario.UsuarioResultResponse;
import com.fsu.reservas_lab.dtos.usuario.UsuarioUpdateRequest;
import com.fsu.reservas_lab.entities.Curso;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.exceptions.curso.CourseNotFoundException;
import com.fsu.reservas_lab.exceptions.usuario.CustomAccessDeniedException;
import com.fsu.reservas_lab.exceptions.usuario.EmailAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.usuario.EnrollmentAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.usuario.UserNotFoundException;
import com.fsu.reservas_lab.repositories.CursoRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, CursoRepository cursoRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<UsuarioResultResponse> createUsuario(UsuarioCreateRequest dto) {
        validateUniqueFields(dto.email(), dto.matricula());
        validateUsuarioTypeAndFields(dto.tipo(), dto.matricula(), dto.cursoId());
        Curso curso = null;
        if (dto.cursoId() != null) {
            curso = cursoRepository.findById(dto.cursoId())
                    .orElseThrow(() -> new CourseNotFoundException());
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setTipo(dto.tipo());
        usuario.setMatricula(dto.matricula());
        usuario.setCurso(curso);
        usuario = usuarioRepository.save(usuario);

        var response = new UsuarioResultResponse("Usuário criado com sucesso.", UsuarioResponse.fromEntity(usuario));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    public ResponseEntity<UsuarioResponse> getUsuarioById(Long id, String currentUserEmail, Optional<TipoUsuario> tipo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        validateViewPermission(usuario, currentUserEmail);
        tipo.ifPresent(t -> {
            if (!usuario.getTipo().equals(t)) {
                throw new CustomAccessDeniedException();
            }
        });
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }

    public ResponseEntity<List<UsuarioResponse>> getAllUsuarios(String currentUserEmail, Optional<TipoUsuario> tipo) {
        Usuario currentUser = usuarioRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getTipo() == TipoUsuario.PROFESSOR) {
            // Professores só veem outros professores
            return ResponseEntity.ok(
                    usuarioRepository.findByTipo(TipoUsuario.PROFESSOR)
                            .stream()
                            .map(UsuarioResponse::fromEntity)
                            .toList()
            );
        }
        // Reitoria e Coord_Lab veem todos, com filtro opcional por tipo
        List<Usuario> usuarios = tipo.isPresent()
                ? usuarioRepository.findByTipo(tipo.get())
                : usuarioRepository.findAll();
        return ResponseEntity.ok(
                usuarios.stream()
                        .map(UsuarioResponse::fromEntity)
                        .toList()
        );
    }

    @Transactional
    public ResponseEntity<UsuarioResultResponse> updateUsuario(Long id, UsuarioUpdateRequest dto, String currentUserEmail) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        validateUpdatePermission(usuario, currentUserEmail);
        if (!usuario.getEmail().equals(dto.email()) && usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        if (dto.matricula() != null && !dto.matricula().equals(usuario.getMatricula()) &&
                usuarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new EnrollmentAlreadyExistsException();
        }
        Curso curso = null;
        if (dto.cursoId() != null) {
            curso = cursoRepository.findById(dto.cursoId())
                    .orElseThrow(() -> new CourseNotFoundException());
        }
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }
        usuario.setMatricula(dto.matricula());
        usuario.setCurso(curso);
        usuario = usuarioRepository.save(usuario);

        var response = new UsuarioResultResponse("Usuário atualizado com sucesso.", UsuarioResponse.fromEntity(usuario));

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String,String>> deleteUsuario(Long id, String currentUserEmail) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        validateDeletePermission(usuario, currentUserEmail);
        usuarioRepository.delete(usuario);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário deletado com sucesso");
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<UsuarioResultResponse> updateOwnAccount(UsuarioUpdateRequest dto, String currentUserEmail) {
        Usuario usuario = usuarioRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);

        // Verificar se há alteração no email antes de validar duplicidade
        if (dto.email() != null && !dto.email().equals(usuario.getEmail())) {
            if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
                throw new EmailAlreadyExistsException();
            }
            usuario.setEmail(dto.email()); // Só altera se for diferente
        }

        // Verificar duplicidade de matrícula
        if (dto.matricula() != null && !dto.matricula().equals(usuario.getMatricula()) &&
                usuarioRepository.findByMatricula(dto.matricula()).isPresent()) {
            throw new EnrollmentAlreadyExistsException();
        }

        // Atualizar curso, se fornecido
        Curso curso = null;
        if (dto.cursoId() != null) {
            curso = cursoRepository.findById(dto.cursoId())
                    .orElseThrow(() -> new CourseNotFoundException());
        }

        usuario.setNome(dto.nome());

        // Atualizar senha apenas se for enviada e não estiver vazia
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        usuario.setMatricula(dto.matricula());
        usuario.setCurso(curso);

        usuario = usuarioRepository.save(usuario);

        var response = new UsuarioResultResponse("Perfil atualizado com sucesso.", UsuarioResponse.fromEntity(usuario));

        return ResponseEntity.ok(response);
    }


    @Transactional
    public ResponseEntity<Void> deleteOwnAccount(String currentUserEmail) {
        Usuario usuario = usuarioRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);
        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }

    private void validateUniqueFields(String email, String matricula) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        if (matricula != null && !matricula.isBlank() && usuarioRepository.findByMatricula(matricula).isPresent()) {
            throw new EnrollmentAlreadyExistsException();
        }
    }

    private void validateUsuarioTypeAndFields(TipoUsuario tipo, String matricula, Long cursoId) {
        if (tipo == TipoUsuario.PROFESSOR) {
            if (matricula == null || matricula.isBlank()) {
                throw new IllegalArgumentException("Matrícula é obrigatória para professores.");
            }
            if (cursoId == null) {
                throw new IllegalArgumentException("Curso é obrigatório para professores.");
            }
        }
    }

    private void validateViewPermission(Usuario usuario, String currentUserEmail) {
        Usuario currentUser = usuarioRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getTipo() == TipoUsuario.PROFESSOR &&
                usuario.getTipo() != TipoUsuario.PROFESSOR) {
            throw new CustomAccessDeniedException();
        }
        if (currentUser.getTipo() != TipoUsuario.REITORIA &&
                currentUser.getTipo() != TipoUsuario.COORDENADOR_LAB &&
                !currentUser.getEmail().equals(usuario.getEmail())) {
            throw new CustomAccessDeniedException();
        }
    }

    private void validateUpdatePermission(Usuario usuario, String currentUserEmail) {
        Usuario currentUser = usuarioRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getTipo() != TipoUsuario.REITORIA &&
                currentUser.getTipo() != TipoUsuario.COORDENADOR_LAB &&
                !currentUser.getEmail().equals(usuario.getEmail())) {
            throw new CustomAccessDeniedException();
        }
    }

    private void validateDeletePermission(Usuario usuario, String currentUserEmail) {
        Usuario currentUser = usuarioRepository.findByEmail(currentUserEmail)
                .orElseThrow(UserNotFoundException::new);
        if (currentUser.getTipo() != TipoUsuario.REITORIA &&
                currentUser.getTipo() != TipoUsuario.COORDENADOR_LAB &&
                !currentUser.getEmail().equals(usuario.getEmail())) {
            throw new CustomAccessDeniedException();
        }
    }
}