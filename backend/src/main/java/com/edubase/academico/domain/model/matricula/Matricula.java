package com.edubase.academico.domain.model.matricula;

import com.edubase.academico.domain.model.aluno.Aluno;
import com.edubase.academico.domain.model.turma.Turma;
import com.edubase.shared.domain.exception.DomainException;

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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
		name = "matriculas",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_matricula_aluno_turma",
				columnNames = {"aluno_id", "turma_id"}
		)
)
public class Matricula {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "aluno_id", nullable = false)
	private Aluno aluno;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "turma_id", nullable = false)
	private Turma turma;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatusMatricula status = StatusMatricula.PENDENTE;

	protected Matricula() {
	}

	private Matricula(Aluno aluno, Turma turma) {
		this.aluno = aluno;
		this.turma = turma;
		this.status = StatusMatricula.PENDENTE;
	}

	/**
	 * Factory: cria matrícula PENDENTE após validar turma (RN001, RN002).
	 * Unicidade aluno+turma (RN003) fica a cargo de {@code PoliticaMatricula}.
	 */
	public static Matricula matricular(Aluno aluno, Turma turma) {
		turma.garantirAptaParaMatricula();
		return new Matricula(aluno, turma);
	}

	public void alterarVinculo(Aluno aluno, Turma turma) {
		if (status != StatusMatricula.PENDENTE) {
			throw new DomainException(
					"Somente matrículas PENDENTE podem ser editadas; use confirmar ou cancelar para alterar o status");
		}
		turma.garantirAptaParaMatricula();
		this.aluno = aluno;
		this.turma = turma;
	}

	/** RN005 */
	public void confirmar() {
		if (status != StatusMatricula.PENDENTE) {
			throw new DomainException("Somente matrículas PENDENTE podem ser confirmadas");
		}
		turma.garantirAptaParaMatricula();
		turma.consumirVaga();
		this.status = StatusMatricula.CONFIRMADA;
	}

	/** RN006 */
	public void cancelar() {
		if (status == StatusMatricula.CANCELADA) {
			throw new DomainException("Matrícula já está CANCELADA");
		}
		if (status == StatusMatricula.CONFIRMADA) {
			turma.devolverVaga();
		}
		this.status = StatusMatricula.CANCELADA;
	}

	/** Devolve vaga se estava confirmada, antes da exclusão física. */
	public void prepararExclusao() {
		if (status == StatusMatricula.CONFIRMADA) {
			turma.devolverVaga();
		}
	}

	public Long getId() {
		return id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public Turma getTurma() {
		return turma;
	}

	public StatusMatricula getStatus() {
		return status;
	}
}
