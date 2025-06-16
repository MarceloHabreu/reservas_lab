package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.reserva.AprovacaoReservaDTO;
import com.fsu.reservas_lab.dtos.reserva.ReservaResponse;
import com.fsu.reservas_lab.entities.AprovacaoReserva;
import com.fsu.reservas_lab.entities.Reserva;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.StatusPedidoReserva;
import com.fsu.reservas_lab.entities.enums.StatusReserva;
import com.fsu.reservas_lab.entities.enums.TipoAprovacao;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import com.fsu.reservas_lab.exceptions.reserva.OrderApprovalInvalidatedException;
import com.fsu.reservas_lab.exceptions.reserva.ReservationInvalidException;
import com.fsu.reservas_lab.exceptions.reserva.ReservationNotFoundException;
import com.fsu.reservas_lab.exceptions.reserva.UnauthorizedApprovalException;
import com.fsu.reservas_lab.exceptions.usuario.UserNotFoundException;
import com.fsu.reservas_lab.repositories.AprovacaoReservaRepository;
import com.fsu.reservas_lab.repositories.ReservaRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AprovacaoReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AprovacaoReservaRepository aprovacaoReservaRepository;

    public AprovacaoReservaService(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, AprovacaoReservaRepository aprovacaoReservaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.aprovacaoReservaRepository = aprovacaoReservaRepository;
    }

    @Transactional
    public ResponseEntity<ReservaResponse> approveReservation(Long idReserva, AprovacaoReservaDTO dto, JwtAuthenticationToken token) {
        // Obtém usuário autenticado do JWT
        String email = token.getName();
        List<String> scopes = token.getToken().getClaimAsStringList("scope");
        Usuario aprovador = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Aprovador não encontrado!"));

        // Determina tipo de aprovação com base no scope
        TipoAprovacao tipoAprovacao = determinarTipoAprovacao(scopes, aprovador, idReserva);

        // Busca reserva
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(ReservationNotFoundException::new);

        // Valida estado
        if (reserva.getStatusPedidoReserva() == StatusPedidoReserva.REJEITADA ||
                reserva.getStatusReserva() == StatusReserva.CANCELADA) {
            throw new ReservationInvalidException("Não é possível aprovar/rejeitar reservas rejeitadas ou canceladas.");
        }

        // Valida permissões do aprovador
        validarAprovador(aprovador, tipoAprovacao, reserva);

        // Verifica aprovação pendente
        AprovacaoReserva aprovacaoPendente = reserva.getAprovacoes().stream()
                .filter(a -> a.getTipoAprovacao() == tipoAprovacao && !a.getAprovado() && a.getAprovador() == null)
                .findFirst()
                .orElseThrow(() -> new ReservationInvalidException("Não há aprovação pendente para esta etapa."));

        // Verifica se etapa já foi aprovada
        boolean jaAprovado = reserva.getAprovacoes().stream()
                .anyMatch(a -> a.getTipoAprovacao() == tipoAprovacao && a.getAprovado());
        if (jaAprovado) {
            throw new ReservationInvalidException("Esta etapa de aprovação já foi realizada.");
        }

        // Verifica ordem de aprovação
        List<TipoAprovacao> ordem = List.of(
                TipoAprovacao.COORDENADOR_LAB,
                TipoAprovacao.COORDENADOR_CURSO,
                TipoAprovacao.REITORIA
        );
        for (TipoAprovacao etapa : ordem) {
            boolean etapaAprovada = reserva.getAprovacoes().stream()
                    .anyMatch(a -> a.getTipoAprovacao() == etapa && a.getAprovado());
            if (!etapaAprovada) {
                if (etapa != tipoAprovacao) {
                    throw new OrderApprovalInvalidatedException("A reserva deve ser aprovada primeiro por: " + etapa.name());
                }
                break;
            }
        }

        // Atualiza aprovação
        aprovacaoPendente.setAprovado(dto.aprovado());
        aprovacaoPendente.setAprovador(aprovador);
        aprovacaoPendente.setObservacoes(dto.obs());
        aprovacaoPendente.setDataHoraAprovacao(LocalDateTime.now());
        aprovacaoReservaRepository.save(aprovacaoPendente);

        // Atualiza status da reserva
        if (!dto.aprovado()) {
            reserva.setStatusPedidoReserva(StatusPedidoReserva.REJEITADA);
            reserva.setStatusReserva(StatusReserva.CANCELADA);
            reservaRepository.save(reserva);
            return ResponseEntity.ok(ReservaResponse.fromEntity(reserva));
        }

        boolean todasAprovadas = ordem.stream()
                .allMatch(etapa -> reserva.getAprovacoes().stream()
                        .anyMatch(a -> a.getTipoAprovacao() == etapa && a.getAprovado()));

        if (todasAprovadas) {
            reserva.setStatusPedidoReserva(StatusPedidoReserva.APROVADO);
        } else {
            switch (tipoAprovacao) {
                case COORDENADOR_LAB:
                    reserva.setStatusPedidoReserva(StatusPedidoReserva.APROVADO_LABORATORIO);
                    break;
                case COORDENADOR_CURSO:
                    reserva.setStatusPedidoReserva(StatusPedidoReserva.APROVADO_CURSO);
                    break;
                case REITORIA:
                    break; // Não deve chegar aqui se não for a última
            }
        }

        reservaRepository.save(reserva);
        return ResponseEntity.ok(ReservaResponse.fromEntity(reserva));
    }

    @Transactional
    public ResponseEntity<ReservaResponse> cancelReservation(Long idReserva, String observacao, JwtAuthenticationToken token) {
        // Obtém usuário autenticado do JWT
        String email = token.getName();
        Usuario solicitante = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Solicitante não encontrado!"));

        // Busca reserva
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(ReservationNotFoundException::new);

        // Valida estado
        if (reserva.getStatusReserva() == StatusReserva.CANCELADA ||
                reserva.getStatusReserva() == StatusReserva.CONCLUIDA) {
            throw new ReservationInvalidException("Reserva já está cancelada ou concluída.");
        }

        // Valida permissões do solicitante
        boolean isReitoria = token.getToken().getClaimAsStringList("scope").contains("REITORIA");
        boolean isProfessor = token.getToken().getClaimAsStringList("scope").contains("PROFESSOR") &&
                solicitante.getId().equals(reserva.getSolicitante().getId());
        if (!isReitoria && !isProfessor) {
            throw new UnauthorizedApprovalException("Apenas o professor responsável ou reitoria podem cancelar a reserva.");
        }

        // Atualiza reserva
        reserva.setStatusReserva(StatusReserva.CANCELADA);
        reserva.setStatusPedidoReserva(StatusPedidoReserva.REJEITADA);
        reserva.setObservacoes(observacao);
        reservaRepository.save(reserva);

        return ResponseEntity.ok(ReservaResponse.fromEntity(reserva));
    }

    private TipoAprovacao determinarTipoAprovacao(List<String> scopes, Usuario usuario, Long idReserva) {
        if (scopes.contains("COORDENADOR_LAB")) {
            return TipoAprovacao.COORDENADOR_LAB;
        } else if (scopes.contains("COORDENADOR_CURSO")) {
            return TipoAprovacao.COORDENADOR_CURSO;
        } else if (scopes.contains("REITORIA")) {
            return TipoAprovacao.REITORIA;
        }
        throw new UnauthorizedApprovalException("Usuário não tem permissão para aprovar/rejeitar reservas.");
    }

    private void validarAprovador(Usuario aprovador, TipoAprovacao tipo, Reserva reserva) {
        switch (tipo) {
            case COORDENADOR_LAB:
                if (aprovador.getTipo() != TipoUsuario.COORDENADOR_LAB ||
                        !reserva.getLaboratorio().getCoordenador().getId().equals(aprovador.getId())) {
                    throw new UnauthorizedApprovalException("Aprovador não é o coordenador do laboratório.");
                }
                break;
            case COORDENADOR_CURSO:
                if (aprovador.getTipo() != TipoUsuario.COORDENADOR_CURSO ||
                        !reserva.getTurma().getCurso().getCoordenador().getId().equals(aprovador.getId())) {
                    throw new UnauthorizedApprovalException("Aprovador não é o coordenador do curso.");
                }
                break;
            case REITORIA:
                if (aprovador.getTipo() != TipoUsuario.REITORIA) {
                    throw new UnauthorizedApprovalException("Aprovador não é da pró-reitoria.");
                }
                break;
        }
    }
}

