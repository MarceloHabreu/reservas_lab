package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String disciplina;

    @Column(nullable = false)
    private String horario;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private String periodoLetivo;

    @OneToMany(mappedBy = "turma")
    private List<Reserva> reservas;
}