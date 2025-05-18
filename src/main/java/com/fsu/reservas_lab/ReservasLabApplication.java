package com.fsu.reservas_lab;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservasLabApplication {

	public static void main(String[] args) {
		// Carrega o arquivo .env
		Dotenv dotenv = Dotenv.configure()
				.directory("./") // Diretório onde o .env está (raiz do projeto)
				.load();
		// Injeta as variáveis no System.properties
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(ReservasLabApplication.class, args);
	}

}
