package com.fsu.reservas_lab.entities;

import com.fsu.reservas_lab.entities.enums.TipoCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "laboratorios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Laboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private String localizacao;


    @ManyToOne
    @JoinColumn(name = "coordenador_id", nullable = false)
    private Usuario coordenador;

    @ManyToMany
    @JoinTable(
            name = "tecnicos_laboratorios",
            joinColumns = @JoinColumn(name = "laboratorio_id"),
            inverseJoinColumns = @JoinColumn(name = "tecnico_id")
    )
    private List<Usuario> tecnicos;

    @OneToMany(mappedBy = "laboratorio")
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "laboratorio")
    private List<ManutencaoLaboratorio> manutencoes;
}
