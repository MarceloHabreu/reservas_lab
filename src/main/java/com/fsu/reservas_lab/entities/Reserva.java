package com.fsu.reservas_lab.entities;
import com.fsu.reservas_lab.entities.converters.StatusPedidoReservaConverter;
import com.fsu.reservas_lab.entities.converters.StatusReservaConverter;
import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", nullable = false)
    private Usuario solicitante;

    @Column(nullable = false)
    private String atividade;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    @Convert(converter = StatusPedidoReservaConverter.class)
    @Column(nullable = false, name = "status_pedido_reserva")
    private StatusPedidoReserva statusPedidoReserva;

    @Convert(converter = StatusReservaConverter.class)
    @Column(nullable = false, name = "status_reserva")
    private StatusReserva statusReserva;

    private String observacoes;

    @CreationTimestamp
    @Column(nullable = false, name = "criado_em")
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AprovacaoReserva> aprovacoes = new ArrayList<>();
}