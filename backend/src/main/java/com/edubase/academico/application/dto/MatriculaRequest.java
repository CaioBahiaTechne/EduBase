package com.edubase.academico.application.dto;

import jakarta.validation.constraints.NotNull;

public class MatriculaRequest {

	@NotNull(message = "alunoId é obrigatório")
	private Long alunoId;

	@NotNull(message = "turmaId é obrigatório")
	private Long turmaId;

	public Long getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(Long alunoId) {
		this.alunoId = alunoId;
	}

	public Long getTurmaId() {
		return turmaId;
	}

	public void setTurmaId(Long turmaId) {
		this.turmaId = turmaId;
	}
}
