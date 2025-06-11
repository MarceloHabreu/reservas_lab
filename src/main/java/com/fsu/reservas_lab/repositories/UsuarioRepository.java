package com.fsu.reservas_lab.repositories;

import com.fsu.reservas_lab.entities.Usuario;
import com.fsu.reservas_lab.entities.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNome(String nome);
    Optional<Usuario> findByMatricula(String matricula);
    List<Usuario> findByTipo(TipoUsuario tipo);
}
