DROP TRIGGER IF EXISTS check_conflito_reserva;
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
        AND status_pedido_reserva != 'REJEITADA'
        AND (
            (hora_inicio < NEW.hora_fim AND hora_fim > NEW.hora_inicio)
        )
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Conflito de horário: o laboratório já está reservado neste intervalo.';
    END IF;
END //

DELIMITER ;
