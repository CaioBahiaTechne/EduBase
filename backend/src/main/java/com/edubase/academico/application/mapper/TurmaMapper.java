package com.edubase.academico.application.mapper;

import com.edubase.academico.application.dto.TurmaRequest;
import com.edubase.academico.application.dto.TurmaResponse;
import com.edubase.academico.domain.model.curso.Disciplina;
import com.edubase.academico.domain.model.turma.Turma;

public final class TurmaMapper {

	private TurmaMapper() {
	}

	public static Turma toEntity(TurmaRequest request, Disciplina disciplina) {
		return new Turma(request.getNome(), request.getVagas(), request.getStatus(), disciplina);
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
