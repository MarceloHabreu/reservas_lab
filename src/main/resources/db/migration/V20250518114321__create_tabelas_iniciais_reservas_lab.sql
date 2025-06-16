-- Tabela Cursos
CREATE TABLE cursos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    codigo VARCHAR(100) UNIQUE NOT NULL
);

-- Tabela Usuarios
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    matricula VARCHAR(50) UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    tipo ENUM('professor', 'coordenador_lab', 'coordenador_curso', 'tecnico', 'reitoria', 'auditor') NOT NULL,
    curso_id BIGINT,
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

ALTER TABLE cursos ADD COLUMN coordenador_id BIGINT;
ALTER TABLE cursos ADD CONSTRAINT fk_coordenador FOREIGN KEY (coordenador_id) REFERENCES usuarios(id);


-- Tabela Laboratorios
CREATE TABLE laboratorios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL,
    localizacao VARCHAR(100) NOT NULL,
    area ENUM('ads', 'arquitetura_urbanismo', 'enfermagem', 'radiologia', 'administracao', 'biomedicina', 'design_moda', 'direito', 'educacao_fisica', 'engenharia_civil', 'engenharia_eletrica', 'estetica_cosmetica', 'farmacia', 'odontologia') NOT NULL,
    coordenador_id BIGINT NOT NULL,
    FOREIGN KEY (coordenador_id) REFERENCES usuarios(id)
);

-- Tabela Tecnicos_Laboratorios
CREATE TABLE tecnicos_laboratorios (
    laboratorio_id BIGINT NOT NULL,
    tecnico_id BIGINT NOT NULL,
    PRIMARY KEY (laboratorio_id, tecnico_id),
    FOREIGN KEY (laboratorio_id) REFERENCES laboratorios(id),
    FOREIGN KEY (tecnico_id) REFERENCES usuarios(id)
);

-- Tabela Turmas
CREATE TABLE turmas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    disciplina VARCHAR(100) NOT NULL,
    horario VARCHAR(50) NOT NULL,
    curso_id BIGINT NOT NULL,
    periodo_letivo VARCHAR(20) NOT NULL,
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

-- Tabela Reservas
CREATE TABLE reservas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    laboratorio_id BIGINT NOT NULL,
    turma_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    data DATE NOT NULL,
    horario TIME NOT NULL,
    status ENUM('pendente', 'aprovado_laboratorio', 'aprovado_curso', 'aprovado', 'rejeitada') NOT NULL DEFAULT 'pendente',
    observacoes TEXT,
    FOREIGN KEY (laboratorio_id) REFERENCES laboratorios(id),
    FOREIGN KEY (turma_id) REFERENCES turmas(id),
    FOREIGN KEY (professor_id) REFERENCES usuarios(id)
);

-- Tabela Manutencao_Laboratorios
CREATE TABLE manutencao_laboratorios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    laboratorio_id BIGINT NOT NULL,
    tecnico_id BIGINT NOT NULL,
    data_manutencao DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    descricao TEXT,
    FOREIGN KEY (laboratorio_id) REFERENCES laboratorios(id),
    FOREIGN KEY (tecnico_id) REFERENCES usuarios(id)
);

-- Tabela Aprovações
CREATE TABLE aprovacoes_reservas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reserva_id BIGINT NOT NULL,
    tipo_aprovacao ENUM('coordenador_lab', 'coordenador_curso', 'reitoria') NOT NULL,
    aprovado TINYINT NOT NULL,
    aprovador_id BIGINT NOT NULL,
    observacoes TEXT,
    data_hora_aprovacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reserva_id) REFERENCES reservas(id),
    FOREIGN KEY (aprovador_id) REFERENCES usuarios(id)
);

-- View para Auditores
CREATE VIEW historico_reservas AS
SELECT
    r.id,
    l.nome AS laboratorio,
    t.codigo AS turma,
    u.nome AS professor,
    r.data,
    r.horario,
    r.status,
    r.observacoes
FROM reservas r
JOIN laboratorios l ON r.laboratorio_id = l.id
JOIN turmas t ON r.turma_id = t.id
JOIN usuarios u ON r.professor_id = u.id;

-- Índices
CREATE INDEX idx_reservas_laboratorio_data_horario ON reservas (laboratorio_id, data, horario);
CREATE INDEX idx_usuarios_tipo ON usuarios (tipo);