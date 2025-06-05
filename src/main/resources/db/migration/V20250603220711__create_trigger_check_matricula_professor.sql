DELIMITER //
CREATE TRIGGER check_matricula_professor
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
    IF NEW.tipo = 'professor' AND NEW.matricula IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Matrícula é obrigatória para professores.';
    END IF;
END //
DELIMITER ;