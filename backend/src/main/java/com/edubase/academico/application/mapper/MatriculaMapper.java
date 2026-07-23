package com.edubase.academico.application.mapper;

import com.edubase.academico.application.dto.MatriculaResponse;
import com.edubase.academico.domain.model.aluno.Aluno;
import com.edubase.academico.domain.model.matricula.Matricula;
import com.edubase.academico.domain.model.turma.Turma;

public final class MatriculaMapper {

	private MatriculaMapper() {
	}

	public static MatriculaResponse toResponse(Matricula matricula) {
		Aluno aluno = matricula.getAluno();
		Turma turma = matricula.getTurma();
		return new MatriculaResponse(
				matricula.getId(),
				aluno != null ? aluno.getId() : null,
				aluno != null ? aluno.getNome() : null,
				turma != null ? turma.getId() : null,
				turma != null ? turma.getNome() : null,
				matricula.getStatus());
	}
}
