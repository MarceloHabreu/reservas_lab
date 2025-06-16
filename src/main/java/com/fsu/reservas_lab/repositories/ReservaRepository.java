package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Reserva;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Verifica se já existe uma reserva para o mesmo laboratório e data,
     * onde o horário informado entra em um intervalo de tempo já reservado.
     *
     * Retorna 'true' se houver uma reserva com horário de início menor que o informado
     * e horário de fim maior que o informado, indicando que há sobreposição.
     */
    boolean existsByLaboratorioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThan(
            Long laboratorioId, LocalDate data, LocalTime horaFim, LocalTime horaInicio
    );

    /**
     * Verifica conflitos de reservas no mesmo laboratório e data,
     * ignorando uma reserva específica pelo ID.
     *
     * Útil ao atualizar uma reserva para garantir que não haja sobreposição com outras
     * reservas existentes, sem considerar a reserva que está sendo modificada.
     *
     * Retorna 'true' se houver uma reserva com horário de início menor que o informado
     * e horário de fim maior que o informado, indicando que há conflito.
     */
    boolean existsByLaboratorioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThanAndIdNot(
            Long laboratorioId, LocalDate data, LocalTime horaFim, LocalTime horaInicio, Long reservaId
    );

    List<Reserva> findByStatusReserva(StatusReserva status);

    List<Reserva> findBySolicitanteId(Long idSolicitante);

    List<Reserva> findByData(LocalDate data);

}
