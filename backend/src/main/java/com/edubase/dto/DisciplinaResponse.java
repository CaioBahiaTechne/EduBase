package com.edubase.dto;

public class DisciplinaResponse {

	private Long id;
	private String nome;
	private Long cursoId;
	private String cursoNome;

	public DisciplinaResponse() {
	}

	public DisciplinaResponse(Long id, String nome, Long cursoId, String cursoNome) {
		this.id = id;
		this.nome = nome;
		this.cursoId = cursoId;
		this.cursoNome = cursoNome;
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

	public Long getCursoId() {
		return cursoId;
	}

	public void setCursoId(Long cursoId) {
		this.cursoId = cursoId;
	}

	public String getCursoNome() {
		return cursoNome;
	}

	public void setCursoNome(String cursoNome) {
		this.cursoNome = cursoNome;
	}
}
