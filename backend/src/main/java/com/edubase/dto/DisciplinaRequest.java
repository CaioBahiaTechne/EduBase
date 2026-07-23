package com.edubase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DisciplinaRequest {

	@NotBlank(message = "nome é obrigatório")
	private String nome;

	@NotNull(message = "cursoId é obrigatório")
	private Long cursoId;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCursoId() {
		return cursoId;
	}

	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}
}
