package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tecnico_laboratorio")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tecnico {
    @Id
    @Column(name = "id_usuario")
    private UUID idUsuario;

    @OneToOne
    @MapsId  // mapeia a chave primaria da classe Usuario como chave primaria do tecnico
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "escola_tecnico", nullable = false)
    private Escola escola;
}
