package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "laboratorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Laboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_laboratorio")
    private UUID id;

    private String nome;

    private int capacidade;

    private String tipo;
}
