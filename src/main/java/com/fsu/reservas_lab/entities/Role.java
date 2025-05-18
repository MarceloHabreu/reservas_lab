package com.fsu.reservas_lab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private Long idRole;

    @Column(nullable = false, unique = true)
    private String nome;

    public enum Values {
        PROFESSOR(1L),
        TECNICO_LABORATORIO(2L);

        public long getIdRole() {
            return idRole;
        }

        long idRole;

        Values(long roleId) {
            this.idRole = roleId;
        }
    }
}

