package com.fsu.reservas_lab.config;

import com.fsu.reservas_lab.entities.Curso;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.repositories.CursoRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Configuration
public class InicializarInsercoes implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public InicializarInsercoes(UsuarioRepository usuarioRepository, CursoRepository cursoRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 🔥 Criando os três coordenadores de curso
        criarUsuarioSeNaoExistir("coord_ads@facema.edu.br", "Coordenador ADS", "12345678", TipoUsuario.COORDENADOR_CURSO, null, null);
        criarUsuarioSeNaoExistir("coord_arquitetura@facema.edu.br", "Coordenador Arquitetura", "12345678", TipoUsuario.COORDENADOR_CURSO, null, null);
        criarUsuarioSeNaoExistir("coord_enfermagem@facema.edu.br", "Coordenador Enfermagem", "12345678", TipoUsuario.COORDENADOR_CURSO, null, null);

        // 🔥 Criando os três cursos vinculados aos coordenadores
        criarCursoSeNaoExistir("Análise e Desenvolvimento de Sistemas", "ads", "coord_ads@facema.edu.br");
        criarCursoSeNaoExistir("Arquitetura e Urbanismo", "arquitetura_urbanismo", "coord_arquitetura@facema.edu.br");
        criarCursoSeNaoExistir("Enfermagem", "enfermagem", "coord_enfermagem@facema.edu.br");

        List<Curso> cursos = cursoRepository.findAll(); // 🔥 Pegando todos os cursos para atribuição aleatória

        // 🔥 Criando usuários essenciais com curso associado aleatoriamente
        criarUsuarioSeNaoExistir("professorOne@facema.edu.br", "Professor One", "12345678", TipoUsuario.PROFESSOR, "31552964", cursoAleatorio(cursos));
        criarUsuarioSeNaoExistir("coordenador_curso@facema.edu.br", "Coordenador Curso", "12345678", TipoUsuario.COORDENADOR_CURSO, null, cursoAleatorio(cursos));

        // 🔥 Criando usuários SEM curso associado (exceto coordenador_lab, reitoria, técnico)
        criarUsuarioSeNaoExistir("coordenador_lab@facema.edu.br", "Coordenador Lab", "12345678", TipoUsuario.COORDENADOR_LAB, null, null);
        criarUsuarioSeNaoExistir("tecnico@facema.edu.br", "Técnico Exemplo", "12345678", TipoUsuario.TECNICO, null, null);
        criarUsuarioSeNaoExistir("reitoria@facema.edu.br", "Reitoria", "12345678", TipoUsuario.REITORIA, null, null);
    }

    private Curso cursoAleatorio(List<Curso> cursos) {
        return cursos.isEmpty() ? null : cursos.get(random.nextInt(cursos.size())); // 🔥 Retorna um curso aleatório ou `null`
    }

    private void criarUsuarioSeNaoExistir(String email, String nome, String senha, TipoUsuario tipoUsuario, String matricula, Curso curso) {
        if (usuarioRepository.findByEmail(email).isEmpty()) {
            Usuario usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNome(nome);
            usuario.setSenha(passwordEncoder.encode(senha)); // 🔒 Senha encriptada com BCrypt
            usuario.setTipo(tipoUsuario);
            usuario.setMatricula(matricula);
            usuario.setCurso(curso);
            usuarioRepository.save(usuario);
            System.out.println("Usuário criado: " + email);
        } else {
            System.out.println("Usuário já existe: " + email);
        }
    }

    private void criarCursoSeNaoExistir(String nome, String codigo, String emailCoordenador) {
        if (cursoRepository.findByCodigo(codigo).isEmpty()) {
            Usuario coordenador = usuarioRepository.findByEmail(emailCoordenador)
                    .orElseThrow(() -> new RuntimeException("Coordenador não encontrado: " + emailCoordenador));

            Curso curso = new Curso();
            curso.setNome(nome);
            curso.setCodigo(codigo);
            curso.setCoordenador(coordenador);
            cursoRepository.save(curso);
            System.out.println("Curso criado: " + nome);
        } else {
            System.out.println("Curso já existe: " + nome);
        }
    }
}
