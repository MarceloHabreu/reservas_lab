package com.fsu.reservas_lab.entities;

import com.fsu.reservas_lab.entities.enums.TipoCurso;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCurso area;

    @ManyToOne
    @JoinColumn(name = "coordenador_id", nullable = false)
    private Usuario coordenador;

    @ManyToMany(mappedBy = "laboratoriosComoTecnico")
    private List<Usuario> tecnicos;

    @OneToMany(mappedBy = "laboratorio")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "laboratorio")
    private List<ManutencaoLaboratorio> manutencoes;
}
