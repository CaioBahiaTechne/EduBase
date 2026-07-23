package com.edubase.academico.application.mapper;

import com.edubase.academico.application.dto.DisciplinaRequest;
import com.edubase.academico.application.dto.DisciplinaResponse;
import com.edubase.academico.domain.model.curso.Curso;
import com.edubase.academico.domain.model.curso.Disciplina;

public final class DisciplinaMapper {

	private DisciplinaMapper() {
	}

	public static Disciplina toEntity(DisciplinaRequest request, Curso curso) {
		return new Disciplina(request.getNome(), curso);
	}

	public static DisciplinaResponse toResponse(Disciplina disciplina) {
		Curso curso = disciplina.getCurso();
		return new DisciplinaResponse(
				disciplina.getId(),
				disciplina.getNome(),
				curso != null ? curso.getId() : null,
				curso != null ? curso.getNome() : null);
	}
}
