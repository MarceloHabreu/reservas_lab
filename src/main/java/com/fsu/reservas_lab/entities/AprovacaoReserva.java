package com.fsu.reservas_lab.entities;

import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AprovacaoReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAprovacao tipoAprovacao;

    @Column(nullable = false)
    private Boolean aprovado;

    @ManyToOne
    @JoinColumn(name = "aprovador_id", nullable = false)
    private Usuario aprovador;

    private String observacoes;

    @Column(nullable = false)
    private LocalDateTime  dataHoraAprovacao;
}