package com.fsu.reservas_lab.entities;

import com.fsu.reservas_lab.dtos.auth.LoginRequest;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.entities.converters.TipoUsuarioConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String matricula;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Convert(converter = TipoUsuarioConverter.class)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @CreationTimestamp
    @Column(nullable = false, name = "registrado_em")
    private LocalDateTime registradoEm;

    @UpdateTimestamp
    @Column(nullable = false, name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @OneToMany(mappedBy = "coordenador")
    private List<Laboratorio> laboratorios;

    @ManyToMany
    @JoinTable(
            name = "tecnicos_laboratorios",
            joinColumns = @JoinColumn(name = "tecnico_id"),
            inverseJoinColumns = @JoinColumn(name = "laboratorio_id")
    )
    private List<Laboratorio> laboratoriosComoTecnico;

    @OneToMany(mappedBy = "professor")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "tecnico")
    private List<ManutencaoLaboratorio> manutencoes;

    @OneToMany(mappedBy = "aprovador")
    private List<AprovacaoReserva> aprovacoes;

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.senha(), this.senha);
    }
}