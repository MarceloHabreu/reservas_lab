-- Remover a coluna 'area' da tabela 'laboratorios'
ALTER TABLE laboratorios
DROP COLUMN area;

-- Renomear a coluna 'status' para 'status_pedido_reserva' na tabela 'reservas'
ALTER TABLE reservas
CHANGE COLUMN status status_pedido_reserva ENUM('pendente', 'aprovado_laboratorio', 'aprovado_curso', 'aprovado', 'rejeitada') NOT NULL DEFAULT 'pendente';

-- Adicionar a nova coluna 'status_reserva' para indicar se a reserva está ativa ou inativa
ALTER TABLE reservas
ADD COLUMN status_reserva ENUM('ativa', 'inativa') NOT NULL DEFAULT 'inativa';
