package com.fsu.reservas_lab.entities;

import com.fsu.reservas_lab.entities.converters.StatusReservaConverter;
import com.fsu.reservas_lab.entities.converters.TipoAprovacaoConverter;
import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "aprovacoes_reservas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AprovacaoReserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reserva reserva;

    @Convert(converter = TipoAprovacaoConverter.class)
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