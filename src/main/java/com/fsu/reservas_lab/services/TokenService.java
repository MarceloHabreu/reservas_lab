package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.auth.AuthResponse;
import com.fsu.reservas_lab.dtos.auth.LoginRequest;
import com.fsu.reservas_lab.dtos.auth.RegisterRequest;
import com.fsu.reservas_lab.dtos.auth.TokenDetails;
import com.fsu.reservas_lab.entities.Curso;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.exceptions.curso.CourseNotFoundException;
import com.fsu.reservas_lab.exceptions.usuario.CustomBadCredentialsException;
import com.fsu.reservas_lab.exceptions.usuario.EmailAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.usuario.EnrollmentAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.usuario.NameAlreadyExistsException;
import com.fsu.reservas_lab.repositories.CursoRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenService(JwtEncoder jwtEncoder, UsuarioRepository usuarioRepository, CursoRepository cursoRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private TokenDetails generateToken(Usuario usuario) {
        var now = Instant.now();
        var expiresIn = 86400L; // 24 horas

        var claims = JwtClaimsSet.builder()
                .issuer("reservas-lab-facema")
                .subject(usuario.getEmail())
                .audience(List.of("reservas-laboratorios-facema-api"))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .id(UUID.randomUUID().toString())
                .claim("scope", usuario.getTipo().name())
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenDetails(jwtValue, expiresIn);
    }

    @Transactional
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginRequest.email());

        if (usuario.isEmpty() || !usuario.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new CustomBadCredentialsException();
        }

        TokenDetails tokenDetails = generateToken(usuario.get());
        return ResponseEntity.ok(new AuthResponse("Login bem-sucedido", tokenDetails.token(), tokenDetails.expiresIn()));
    }

    @Transactional
    public ResponseEntity<AuthResponse> cadastro(RegisterRequest registerRequest) {
        var emailFromDb = usuarioRepository.findByEmail(registerRequest.email());
        var matriculaFromdb = usuarioRepository.findByMatricula(registerRequest.matricula());
        var nomeFromDb = usuarioRepository.findByNome(registerRequest.nome());

        if (emailFromDb.isPresent()) {
            throw new EmailAlreadyExistsException();
        } else if (matriculaFromdb.isPresent()) {
            throw new EnrollmentAlreadyExistsException();
        }else if (nomeFromDb.isPresent()) {
            throw new NameAlreadyExistsException();
        }

        Optional<Curso> curso = cursoRepository.findByCodigo(registerRequest.curso().getDbValue());
        if (curso.isEmpty()) {
            throw new CourseNotFoundException();
        }
        var novoUsuario = new Usuario();
        novoUsuario.setNome(registerRequest.nome());
        novoUsuario.setEmail(registerRequest.email());
        novoUsuario.setSenha(passwordEncoder.encode(registerRequest.senha()));
        novoUsuario.setTipo(TipoUsuario.PROFESSOR);
        novoUsuario.setMatricula(registerRequest.matricula());
        novoUsuario.setCurso(curso.get());

        usuarioRepository.save(novoUsuario);

        TokenDetails tokenDetails = generateToken(novoUsuario);

        // Criando uri absoluta para o response entity
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/reservas-lab-facema/usuarios/{id}")
                .buildAndExpand(novoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(new AuthResponse("Cadastro bem-sucedido", tokenDetails.token(), tokenDetails.expiresIn()));
    }
}
