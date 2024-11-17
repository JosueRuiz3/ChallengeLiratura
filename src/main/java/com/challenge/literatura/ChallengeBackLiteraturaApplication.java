package com.challenge.literatura;

import com.challenge.literatura.Libreria.Libreria;
import com.challenge.literatura.repository.iAutorRepository;
import com.challenge.literatura.repository.iLibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeBackLiteraturaApplication implements CommandLineRunner {

	@Autowired
	private iLibroRepository libroRepository;

	@Autowired
	private iAutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(ChallengeBackLiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Libreria libreria = new Libreria(libroRepository, autorRepository);
		libreria.consumo();
	}
}
