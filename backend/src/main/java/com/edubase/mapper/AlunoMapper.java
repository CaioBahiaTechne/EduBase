package com.edubase.mapper;

import com.edubase.dto.AlunoRequest;
import com.edubase.dto.AlunoResponse;
import com.edubase.entity.Aluno;

public final class AlunoMapper {

	private AlunoMapper() {
	}

	public static Aluno toEntity(AlunoRequest request) {
		return new Aluno(request.getNome(), request.getEmail());
	}

	public static void updateEntity(Aluno aluno, AlunoRequest request) {
		aluno.setNome(request.getNome());
		aluno.setEmail(request.getEmail());
	}

	public static AlunoResponse toResponse(Aluno aluno) {
		return new AlunoResponse(aluno.getId(), aluno.getNome(), aluno.getEmail());
	}
}
