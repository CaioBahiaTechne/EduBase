package com.edubase.academico.application.mapper;

import com.edubase.academico.application.dto.AlunoRequest;
import com.edubase.academico.application.dto.AlunoResponse;
import com.edubase.academico.domain.model.aluno.Aluno;

public final class AlunoMapper {

	private AlunoMapper() {
	}

	public static Aluno toEntity(AlunoRequest request) {
		return new Aluno(request.getNome(), request.getEmail());
	}

	public static AlunoResponse toResponse(Aluno aluno) {
		return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getEmail());
	}
}
