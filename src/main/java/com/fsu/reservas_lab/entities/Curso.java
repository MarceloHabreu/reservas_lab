package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String codigo;

    @OneToMany(mappedBy = "curso")
    private List<Usuario> usuarios;

    @OneToMany(mappedBy = "curso")
    private List<Turma> turmas;
}
