package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "escola")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Escola {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_escola")
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;
}
