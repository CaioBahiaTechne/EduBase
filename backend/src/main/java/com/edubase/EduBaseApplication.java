package com.edubase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.edubase.academico.domain.model")
@EnableJpaRepositories(basePackages = "com.edubase.academico.infrastructure.persistence")
public class EduBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduBaseApplication.class, args);
	}

}
