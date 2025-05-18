package com.fsu.reservas_lab.entities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reserva")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_laboratorio", nullable = false)
    @NotNull
    private Laboratorio laboratorio;

    @ManyToOne
    @JoinColumn(name = "id_professor", nullable = false)
    @NotNull
    private Professor professor;

    @Column(name = "nome_disciplina", nullable = false)
    @NotNull
    private String nomeDisciplina;

    @Column(name = "periodo_reserva", nullable = false)
    @NotNull
    private String periodoReserva; // Armazena JSON como texto

    @Transient // Não persiste no banco
    private String[] periodoReservaArray;

    @Column(name = "dia_reserva", nullable = false)
    @NotNull
    private LocalDate diaReserva;

    @Column(name = "horario_reserva", nullable = false)
    @NotNull
    private LocalDateTime horarioReserva;

    @Column(name = "roteiros_reserva")
    private byte[] roteirosReserva;

    // Converte String[] para JSON antes de salvar
    @PrePersist
    @PreUpdate
    public void serializePeriodoReserva() {
        if (periodoReservaArray != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                periodoReserva = mapper.writeValueAsString(periodoReservaArray);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar periodo_reserva", e);
            }
        }
    }

    // Converte JSON para String[] após carregar
    @PostLoad
    public void deserializePeriodoReserva() {
        if (periodoReserva != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                periodoReservaArray = mapper.readValue(periodoReserva, String[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao desserializar periodo_reserva", e);
            }
        }
    }
}