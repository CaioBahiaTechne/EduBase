package com.edubase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AlunoRequest {

	@NotBlank(message = "nome é obrigatório")
	private String nome;

	@NotBlank(message = "email é obrigatório")
	@Email(message = "email inválido")
	private String email;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
