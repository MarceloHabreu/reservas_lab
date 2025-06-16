ALTER TABLE reservas
ADD COLUMN capacidade_alunos INT NOT NULL DEFAULT 0,
ADD COLUMN criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN hora_fim TIME,
CHANGE COLUMN horario hora_inicio TIME,
MODIFY COLUMN status_reserva ENUM('ativa', 'pendente', 'cancelada', 'concluida') NOT NULL DEFAULT 'pendente';
