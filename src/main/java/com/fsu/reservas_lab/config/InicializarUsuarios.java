package com.fsu.reservas_lab.config;

import com.fsu.reservas_lab.entities.Curso;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.TipoCurso;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.exceptions.curso.CourseNotFoundException;
import com.fsu.reservas_lab.repositories.CursoRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class InicializarUsuarios implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public InicializarUsuarios(UsuarioRepository usuarioRepository, CursoRepository cursoRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {

        var curso = cursoRepository.findByCodigo("biomedicina").orElseThrow(CourseNotFoundException::new);

        // Lista de usuários a serem criados
        criarUsuarioSeNaoExistir("professorOne@facema.edu.br", "Professor One", "12345678", TipoUsuario.PROFESSOR, "31552964", curso);
        criarUsuarioSeNaoExistir("coordenador_lab@facema.edu.br", "Coordenador Lab", "12345678", TipoUsuario.COORDENADOR_LAB, null, null);
        criarUsuarioSeNaoExistir("coordenador_curso@facema.edu.br", "Coordenador Curso", "12345678", TipoUsuario.COORDENADOR_CURSO, null, null);
        criarUsuarioSeNaoExistir("tecnico@facema.edu.br", "Técnico Exemplo", "12345678", TipoUsuario.TECNICO, null, null);
        criarUsuarioSeNaoExistir("reitoria@facema.edu.br", "Reitoria", "12345678", TipoUsuario.REITORIA, null, null);
        criarUsuarioSeNaoExistir("auditor@facema.edu.br", "Auditor Exemplo", "12345678", TipoUsuario.AUDITOR, null, null);
    }

    private void criarUsuarioSeNaoExistir(String email, String nome, String senha, TipoUsuario tipoUsuario, String matricula, Curso curso) {
        if (usuarioRepository.findByEmail(email).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNome(nome);
            usuario.setSenha(passwordEncoder.encode(senha));
            usuario.setTipo(tipoUsuario);
            usuario.setMatricula(matricula);
            usuario.setCurso(curso);
            usuarioRepository.save(usuario);
            System.out.println("Usuário criado: " + email);
        } else {
            System.out.println("Usuário já existe: " + email);
        }
    }
}