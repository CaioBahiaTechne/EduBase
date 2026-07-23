package com.edubase.academico.application.dto;

import com.edubase.academico.domain.model.turma.StatusTurma;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TurmaRequest {

	@NotBlank(message = "nome é obrigatório")
	private String nome;

	@NotNull(message = "vagas é obrigatório")
	@Min(value = 1, message = "vagas deve ser maior que 0")
	private Integer vagas;

	@NotNull(message = "status é obrigatório")
	private StatusTurma status;

	@NotNull(message = "disciplinaId é obrigatório")
	private Long disciplinaId;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getVagas() {
		return vagas;
	}

	public void setVagas(Integer vagas) {
		this.vagas = vagas;
	}

	public StatusTurma getStatus() {
		return status;
	}

	public void setStatus(StatusTurma status) {
		this.status = status;
	}

	public Long getDisciplinaId() {
		return disciplinaId;
	}

	public void setDisciplinaId(Long disciplinaId) {
		this.disciplinaId = disciplinaId;
	}
}
