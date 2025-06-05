DELIMITER //
CREATE TRIGGER check_conflito_reserva
BEFORE INSERT ON reservas
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1
        FROM reservas
        WHERE laboratorio_id = NEW.laboratorio_id
        AND data = NEW.data
        AND horario = NEW.horario
        AND status != 'Rejeitada'
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conflito de horário: o laboratório já está reservado para este horário.';
    END IF;
END //
DELIMITER ;