CREATE TABLE escola (
    id_escola BINARY(16) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT
);

CREATE TABLE usuario (
    id_usuario BINARY(16) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE professor (
    id_usuario BINARY(16) PRIMARY KEY,
    escola_professor BINARY(16) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (escola_professor) REFERENCES escola(id_escola)
);

CREATE TABLE tecnico_laboratorio (
    id_usuario BINARY(16) PRIMARY KEY,
    escola_tecnico BINARY(16) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (escola_tecnico) REFERENCES escola(id_escola)
);

CREATE TABLE roles (
    id_role INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios_roles (
    id_usuario BINARY(16),
    id_role INT,
    PRIMARY KEY (id_usuario, id_role),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    FOREIGN KEY (id_role) REFERENCES roles(id_role)
);

INSERT INTO roles (id_role, nome) VALUES (1, 'PROFESSOR');
INSERT INTO roles (id_role, nome) VALUES (2, 'TECNICO_LABORATORIO');

CREATE TABLE laboratorio (
    id_laboratorio BINARY(16) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    tipo VARCHAR(50) NOT NULL
);

CREATE TABLE reserva (
    id_reserva BINARY(16) PRIMARY KEY,
    id_laboratorio BINARY(16) NOT NULL,
    id_professor BINARY(16) NOT NULL,
    nome_disciplina VARCHAR(100) NOT NULL,
    periodo_reserva JSON NOT NULL,
    dia_reserva DATE NOT NULL,
    horario_reserva DATETIME NOT NULL,
    roteiros_reserva BLOB,
    FOREIGN KEY (id_laboratorio) REFERENCES laboratorio(id_laboratorio),
    FOREIGN KEY (id_professor) REFERENCES professor(id_usuario)
);
