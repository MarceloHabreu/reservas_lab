package com.fsu.reservas_lab.entities;
import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private LocalTime horario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status_pedido_reserva")
    private StatusPedidoReserva statusPedidoReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status_reserva")
    private StatusReserva statusReserva;

    private String observacoes;

    @OneToMany(mappedBy = "reserva")
    private List<AprovacaoReserva> aprovacoes;
}