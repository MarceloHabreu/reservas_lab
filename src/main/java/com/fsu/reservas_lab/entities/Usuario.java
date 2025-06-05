package com.fsu.reservas_lab.entities;

import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuario")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

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
}