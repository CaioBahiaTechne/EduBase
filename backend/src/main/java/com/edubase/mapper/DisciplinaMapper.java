package com.edubase.mapper;

import com.edubase.dto.DisciplinaRequest;
import com.edubase.dto.DisciplinaResponse;
import com.edubase.entity.Curso;
import com.edubase.entity.Disciplina;

public final class DisciplinaMapper {

	private DisciplinaMapper() {
	}

	public static Disciplina toEntity(DisciplinaRequest request, Curso curso) {
		return new Disciplina(request.getNome(), curso);
	}

	public static void updateEntity(Disciplina disciplina, DisciplinaRequest request, Curso curso) {
		disciplina.setNome(request.getNome());
		disciplina.setCurso(curso);
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
