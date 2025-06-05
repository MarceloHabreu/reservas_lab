package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ManutencaoLaboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Usuario tecnico;

    @Column(nullable = false)
    private LocalDate dataManutencao;

    @Column(nullable = false)
    private String status;

    private String descricao;
}
