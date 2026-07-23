package com.edubase.academico.application.dto;

import com.edubase.academico.domain.model.turma.StatusTurma;

public class TurmaResponse {

	private Long id;
	private String nome;
	private Integer vagas;
	private StatusTurma status;
	private Long disciplinaId;
	private String disciplinaNome;

	public TurmaResponse() {
	}

	public TurmaResponse(
			Long id,
			String nome,
			Integer vagas,
			StatusTurma status,
			Long disciplinaId,
			String disciplinaNome) {
		this.id = id;
		this.nome = nome;
		this.vagas = vagas;
		this.status = status;
		this.disciplinaId = disciplinaId;
		this.disciplinaNome = disciplinaNome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getDisciplinaNome() {
		return disciplinaNome;
	}

	public void setDisciplinaNome(String disciplinaNome) {
		this.disciplinaNome = disciplinaNome;
	}
}
