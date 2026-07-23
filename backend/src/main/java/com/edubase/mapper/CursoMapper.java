package com.edubase.mapper;

import com.edubase.dto.CursoRequest;
import com.edubase.dto.CursoResponse;
import com.edubase.entity.Curso;

public final class CursoMapper {

	private CursoMapper() {
	}

	public static Curso toEntity(CursoRequest request) {
		return new Curso(request.getNome());
	}

	public static void updateEntity(Curso curso, CursoRequest request) {
		curso.setNome(request.getNome());
	}

	public static CursoResponse toResponse(Curso curso) {
		return new CursoResponse(curso.getId(), curso.getNome());
	}
}
