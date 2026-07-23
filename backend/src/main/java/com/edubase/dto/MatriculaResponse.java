package com.edubase.dto;

import com.edubase.entity.StatusMatricula;

public class MatriculaResponse {

	private Long id;
	private Long alunoId;
	private String alunoNome;
	private Long turmaId;
	private String turmaNome;
	private StatusMatricula status;

	public MatriculaResponse() {
	}

	public MatriculaResponse(
			Long id,
			Long alunoId,
			String alunoNome,
			Long turmaId,
			String turmaNome,
			StatusMatricula status) {
		this.id = id;
		this.alunoId = alunoId;
		this.alunoNome = alunoNome;
		this.turmaId = turmaId;
		this.turmaNome = turmaNome;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(Long alunoId) {
		this.alunoId = alunoId;
	}

	public String getAlunoNome() {
		return alunoNome;
	}

	public void setAlunoNome(String alunoNome) {
		this.alunoNome = alunoNome;
	}

	public Long getTurmaId() {
		return turmaId;
	}

	public void setTurmaId(Long turmaId) {
		this.turmaId = turmaId;
	}

	public String getTurmaNome() {
		return turmaNome;
	}

	public void setTurmaNome(String turmaNome) {
		this.turmaNome = turmaNome;
	}

	public StatusMatricula getStatus() {
		return status;
	}

	public void setStatus(StatusMatricula status) {
		this.status = status;
	}
}
