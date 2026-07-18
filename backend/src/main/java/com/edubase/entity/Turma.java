package com.edubase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "turmas")
public class Turma {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(nullable = false, length = 150)
	private String nome;

	/**
	 * Vagas ainda disponíveis para confirmação de matrícula.
	 * Na criação deve ser &gt; 0; após confirmações pode chegar a 0.
	 */
	@NotNull
	@Min(0)
	@Column(nullable = false)
	private Integer vagas;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusTurma status;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "disciplina_id", nullable = false)
	private Disciplina disciplina;

	public Turma() {
	}

	public Turma(String nome, Integer vagas, StatusTurma status, Disciplina disciplina) {
		this.nome = nome;
		this.vagas = vagas;
		this.status = status;
		this.disciplina = disciplina;
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

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

	public boolean isAberta() {
		return StatusTurma.ABERTA.equals(status);
	}

	public boolean possuiVagas() {
		return vagas != null && vagas > 0;
	}

	public void consumirVaga() {
		if (!possuiVagas()) {
			throw new IllegalStateException("Turma sem vagas disponíveis");
		}
		this.vagas = this.vagas - 1;
	}

	public void devolverVaga() {
		if (this.vagas == null) {
			this.vagas = 0;
		}
		this.vagas = this.vagas + 1;
	}
}
