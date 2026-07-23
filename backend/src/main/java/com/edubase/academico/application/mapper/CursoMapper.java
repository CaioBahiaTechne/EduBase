package com.edubase.academico.application.mapper;

import com.edubase.academico.application.dto.CursoRequest;
import com.edubase.academico.application.dto.CursoResponse;
import com.edubase.academico.domain.model.curso.Curso;

public final class CursoMapper {

	private CursoMapper() {
	}

	public static Curso toEntity(CursoRequest request) {
		return new Curso(request.getNome());
	}

	public static CursoResponse toResponse(Curso curso) {
		return new CursoResponse(curso.getId(), curso.getNome());
	}
}
