package com.edubase.mapper;

import com.edubase.dto.TurmaRequest;
import com.edubase.dto.TurmaResponse;
import com.edubase.entity.Disciplina;
import com.edubase.entity.Turma;

public final class TurmaMapper {

	private TurmaMapper() {
	}

	public static Turma toEntity(TurmaRequest request, Disciplina disciplina) {
		return new Turma(request.getNome(), request.getVagas(), request.getStatus(), disciplina);
	}

	public static void updateEntity(Turma turma, TurmaRequest request, Disciplina disciplina) {
		turma.setNome(request.getNome());
		turma.setVagas(request.getVagas());
		turma.setStatus(request.getStatus());
		turma.setDisciplina(disciplina);
	}

	public static TurmaResponse toResponse(Turma turma) {
		Disciplina disciplina = turma.getDisciplina();
		return new TurmaResponse(
				turma.getId(),
				turma.getNome(),
				turma.getVagas(),
				turma.getStatus(),
				disciplina != null ? disciplina.getId() : null,
				disciplina != null ? disciplina.getNome() : null);
	}
}
