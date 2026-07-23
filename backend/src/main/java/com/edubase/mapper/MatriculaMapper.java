package com.edubase.mapper;

import com.edubase.dto.MatriculaResponse;
import com.edubase.entity.Aluno;
import com.edubase.entity.Matricula;
import com.edubase.entity.Turma;

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
