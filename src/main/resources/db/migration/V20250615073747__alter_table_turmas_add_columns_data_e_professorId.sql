ALTER TABLE turmas
ADD COLUMN data DATE NOT NULL;

ALTER TABLE turmas
ADD COLUMN professor_id BIGINT NOT NULL,
ADD CONSTRAINT fk_professor FOREIGN KEY (professor_id) REFERENCES usuarios(id);
