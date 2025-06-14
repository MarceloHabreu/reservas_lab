package com.fsu.reservas_lab.services;

import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioCreateRequest;
import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioResponse;
import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioResultResponse;
import com.fsu.reservas_lab.dtos.laboratorio.LaboratorioUpdateRequest;
import com.fsu.reservas_lab.entities.Laboratorio;
import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryActiveReservationsException;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryAlreadyExistsException;
import com.fsu.reservas_lab.exceptions.laboratorio.LaboratoryNotFoundException;
import com.fsu.reservas_lab.exceptions.usuario.UserNotFoundException;
import com.fsu.reservas_lab.repositories.LaboratorioRepository;
import com.fsu.reservas_lab.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LaboratorioService {
    private final LaboratorioRepository laboratorioRepository;
    private final UsuarioRepository usuarioRepository;

    public LaboratorioService(LaboratorioRepository laboratorioRepository, UsuarioRepository usuarioRepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ResponseEntity<LaboratorioResultResponse> saveLaboratory(LaboratorioCreateRequest dto) {
        if (laboratorioRepository.findByNome(dto.nome()).isPresent()) {
            throw new LaboratoryAlreadyExistsException();
        }

        // Buscar o coordenador do banco
        Usuario coordenador = usuarioRepository.findById(dto.coordenadorLab())
                .orElseThrow(() -> new UserNotFoundException("Coordenador não encontrado!"));

        // Buscar técnicos do banco
        List<Long> tecnicosIds = dto.tecnicos();
        List<Usuario> tecnicos = usuarioRepository.findAllById(tecnicosIds);

        if (tecnicos.size() != tecnicosIds.size()) {
            throw new UserNotFoundException("Um ou mais técnicos não foram encontrados!");
        }

        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setNome(dto.nome());
        laboratorio.setCapacidade(dto.capacidade());
        laboratorio.setLocalizacao(dto.localizacao());
        laboratorio.setTecnicos(tecnicos);
        laboratorio.setCoordenador(coordenador);

        laboratorio = laboratorioRepository.save(laboratorio);

        var response = new LaboratorioResultResponse("Laboratório criado com sucesso", LaboratorioResponse.fromEntity(laboratorio));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    public ResponseEntity<LaboratorioResponse> getLaboratoryById(Long id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(LaboratoryNotFoundException::new);
        return ResponseEntity.ok(LaboratorioResponse.fromEntity(laboratorio));
    }

    public ResponseEntity<List<LaboratorioResponse>> getAllLaboratories() {
        List<LaboratorioResponse> laboratorios = laboratorioRepository.findAll()
                .stream()
                .map(LaboratorioResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(laboratorios);
    }

    @Transactional
    public ResponseEntity<LaboratorioResultResponse> updateLaboratory(Long id, LaboratorioUpdateRequest dto) {
        // Buscar o laboratório existente
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new LaboratoryNotFoundException());

        // Garantir que o novo nome não pertence a outro laboratório
        if (!laboratorio.getNome().equals(dto.nome()) && laboratorioRepository.findByNome(dto.nome()).isPresent()) {
            throw new LaboratoryAlreadyExistsException();
        }

        // Buscar coordenador no banco
        Usuario coordenador = usuarioRepository.findById(dto.coordenadorLab())
                .orElseThrow(() -> new UserNotFoundException("Coordenador não encontrado!"));

        // Buscar técnicos e validar se todos os IDs existem
        List<Long> tecnicosIds = dto.tecnicos();
        List<Usuario> tecnicos = usuarioRepository.findAllById(tecnicosIds);

        if (tecnicos.size() != tecnicosIds.size()) {
            throw new UserNotFoundException("Um ou mais técnicos não foram encontrados!");
        }

        // Atualizar os dados do laboratório
        laboratorio.setNome(dto.nome());
        laboratorio.setCapacidade(dto.capacidade());
        laboratorio.setLocalizacao(dto.localizacao());
        laboratorio.setCoordenador(coordenador);
        laboratorio.setTecnicos(tecnicos);

        // Salvar as alterações
        laboratorio = laboratorioRepository.save(laboratorio);

        var response = new LaboratorioResultResponse("Laboratório atualizado com sucesso", LaboratorioResponse.fromEntity(laboratorio));
        return ResponseEntity.ok(response);
    }


    @Transactional
    public ResponseEntity<Map<String, String>> deleteLaboratory(Long id) {

        if (!laboratorioRepository.existsById(id)) {
            throw new LaboratoryNotFoundException();
        }
        if (laboratorioRepository.hasActiveReservations(id)) {
            throw new LaboratoryActiveReservationsException();
        }
        laboratorioRepository.deleteById(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Laboratorio deletado com sucesso");
        return ResponseEntity.ok(response);
    }

}
