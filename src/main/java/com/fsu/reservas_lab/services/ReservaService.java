package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.reserva.ReservaCreateRequest;
import com.fsu.reservas_lab.dtos.reserva.ReservaResponse;
import com.fsu.reservas_lab.dtos.reserva.ReservaResultResponse;
import com.fsu.reservas_lab.dtos.reserva.ReservaUpdateRequest;
import com.fsu.reservas_lab.entities.*;
import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryNotFoundException;
import com.fsu.reservas_lab.exceptions.reserva.*;
import com.fsu.reservas_lab.exceptions.turma.ClassNotFoundException;
import com.fsu.reservas_lab.exceptions.usuario.UserNotFoundException;
import com.fsu.reservas_lab.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final TurmaRepository turmaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AprovacaoReservaRepository aprovacaoReservaRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            LaboratorioRepository laboratorioRepository,
            TurmaRepository turmaRepository,
            UsuarioRepository usuarioRepository,
            AprovacaoReservaRepository aprovacaoReservaRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.turmaRepository = turmaRepository;
        this.usuarioRepository = usuarioRepository;
        this.aprovacaoReservaRepository = aprovacaoReservaRepository;
    }

    @Transactional
    public ResponseEntity<ReservaResultResponse> createReservation(Long laboratorioId, ReservaCreateRequest dto, String currentUserEmail) {

        // 1. Verificar conflitos de horário
        boolean conflito = reservaRepository.existsByLaboratorioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThan(
                laboratorioId, dto.data(), dto.horaFim(), dto.horaInicio());

        if (conflito) {
            throw new ReservationTimeConflictException();
        }

        // 2. Validar entidades relacionadas
        Laboratorio lab = laboratorioRepository.findById(laboratorioId)
                .orElseThrow(LaboratoryNotFoundException::new);

        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(ClassNotFoundException::new);

        Usuario solicitante = usuarioRepository.findById(dto.solicitanteId())
                .orElseThrow(UserNotFoundException::new);

        // 3. Validar quem esta solicitando a reserva
        Usuario currentUser = usuarioRepository.findByEmail(currentUserEmail).orElseThrow(UserNotFoundException::new);

        if (currentUser.getTipo() == TipoUsuario.PROFESSOR &&
                !currentUser.getId().equals(solicitante.getId())) {
            throw new ReservationInvalidException("Professores só podem reservar para si mesmos.");
        }

        // 4. Validar tamanho turma
        if (turma.getNumeroAlunos() > lab.getCapacidade()) {
            throw new ReservationInvalidException("O laboratório não suporta o número de alunos da turma.");
        }

        // 3. Criar a reserva
        Reserva reserva = new Reserva();
        reserva.setLaboratorio(lab);
        reserva.setTurma(turma);
        reserva.setSolicitante(solicitante);
        reserva.setAtividade(dto.atividade());
        reserva.setData(dto.data());
        reserva.setHoraInicio(dto.horaInicio());
        reserva.setHoraFim(dto.horaFim());
        reserva.setObservacoes(dto.observacoes());
        reserva.setStatusPedidoReserva(StatusPedidoReserva.PENDENTE);
        reserva.setStatusReserva(StatusReserva.PENDENTE);

        reserva = reservaRepository.save(reserva);

        // 4. Criar 3 aprovações pendentes (ordem: laboratório → curso → reitoria)
        List<TipoAprovacao> aprovacoes = List.of(
                TipoAprovacao.COORDENADOR_LAB,
                TipoAprovacao.COORDENADOR_CURSO,
                TipoAprovacao.REITORIA
        );

        var response = new ReservaResultResponse("Reserva criada, aguardando aprovação.", ReservaResponse.fromEntity(reserva));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ReservaResponse> getReservation(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);
        return ResponseEntity.ok(ReservaResponse.fromEntity(reserva));
    }

    public ResponseEntity<List<ReservaResponse>> getAllReservas() {
        List<ReservaResponse> reservas = reservaRepository.findAll()
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(reservas);
    }

    @Transactional
    public ResponseEntity<ReservaResultResponse> updateReservation(Long id, ReservaUpdateRequest dto, String currentUserEmail) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);

        var usuario = usuarioRepository.findByEmail(currentUserEmail).orElseThrow(UserNotFoundException::new);

        boolean isDonoDaReserva = reserva.getSolicitante().getId().equals(usuario.getId());
        boolean isPermissaoEspecial = usuario.getTipo() == TipoUsuario.COORDENADOR_LAB
                || usuario.getTipo() == TipoUsuario.REITORIA;

        if (!isDonoDaReserva && !isPermissaoEspecial) {
            throw new ReservationCannotBeUpdatedException("Você não tem permissão para editar esta reserva.");
        }

        // Atualização só permitida se ainda estiver pendente
        if (reserva.getStatusReserva() != StatusReserva.PENDENTE) {
            throw new ReservationCannotBeUpdatedException();
        }

        boolean conflito = reservaRepository.existsByLaboratorioIdAndDataAndHoraInicioLessThanAndHoraFimGreaterThanAndIdNot(
                dto.laboratorioId(), dto.data(), dto.horaFim(), dto.horaInicio(), id);

        if (conflito) {
            throw new ReservationTimeConflictException();
        }

        Laboratorio lab = laboratorioRepository.findById(dto.laboratorioId())
                .orElseThrow(LaboratoryNotFoundException::new);
        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(ClassNotFoundException::new);

        if (turma.getNumeroAlunos() > lab.getCapacidade()) {
            throw new ReservationInvalidException("O laboratório não suporta o número de alunos da turma.");
        }

        reserva.setAtividade(dto.atividade());
        reserva.setData(dto.data());
        reserva.setHoraInicio(dto.horaInicio());
        reserva.setHoraFim(dto.horaFim());
        reserva.setObservacoes(dto.observacoes());

        reserva.setLaboratorio(lab);
        reserva.setTurma(turma);

        reserva = reservaRepository.save(reserva);
        var response = new ReservaResultResponse("Reserva atualizada, aguardando aprovação.", ReservaResponse.fromEntity(reserva));
        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteReservation(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(ReservationNotFoundException::new);

        if (reserva.getStatusReserva() != StatusReserva.PENDENTE) {
            throw new ReservationCannotBeDeletedException();
        }

        reservaRepository.delete(reserva);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reserva excluída com sucesso");
        return ResponseEntity.ok(response);
    }

    // Buscar por status
    public ResponseEntity<List<ReservaResponse>> findReservationsByStatus(StatusReserva status) {
        List<Reserva> reservas = reservaRepository.findByStatusReserva(status);
        List<ReservaResponse> responses = reservas.stream()
                .map(ReservaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Buscar por solicitante
    public ResponseEntity<List<ReservaResponse>> findReservationsByApplicant(Long idSolicitante) {
        List<ReservaResponse> reservas = reservaRepository.findBySolicitanteId(idSolicitante)
                .stream()
                .map(ReservaResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(reservas);
    }


    // Buscar por data
    public ResponseEntity<List<ReservaResponse>> findReservationsByDate(LocalDate data) {
        List<Reserva> reservas = reservaRepository.findByData(data);
        List<ReservaResponse> responses = reservas.stream()
                .map(ReservaResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
