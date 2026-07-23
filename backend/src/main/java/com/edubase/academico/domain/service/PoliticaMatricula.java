package com.edubase.academico.domain.service;

import com.edubase.academico.domain.repository.MatriculaRepository;
import com.edubase.shared.domain.exception.DomainException;

/**
 * Política de domínio que cruza agregados / persistência (RN003).
 */
public final class PoliticaMatricula {

	private PoliticaMatricula() {
	}

	/** RN003 — um aluno não pode ter duas matrículas na mesma turma. */
	public static void garantirMatriculaUnica(
			MatriculaRepository matriculaRepository,
			Long alunoId,
			Long turmaId,
			Long idAtual) {
		matriculaRepository.findByAlunoIdAndTurmaId(alunoId, turmaId)
				.filter(existente -> idAtual == null || !existente.getId().equals(idAtual))
				.ifPresent(existente -> {
					throw new DomainException("Já existe matrícula deste aluno nesta turma");
				});
	}
}
