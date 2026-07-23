package com.edubase.academico.domain.model.curso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false, length = 150)
	private String nome;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "curso_id", nullable = false)
	private Curso curso;

	protected Disciplina() {
	}

	public Disciplina(String nome, Curso curso) {
		atualizar(nome, curso);
	}

	public void atualizar(String nome, Curso curso) {
		this.nome = nome;
		this.curso = curso;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Curso getCurso() {
		return curso;
	}
}
