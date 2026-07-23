package com.edubase.academico.application.usecase.turma;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.TurmaRequest;
import com.edubase.academico.application.dto.TurmaResponse;
import com.edubase.academico.application.mapper.TurmaMapper;
import com.edubase.academico.domain.model.curso.Disciplina;
import com.edubase.academico.domain.model.turma.Turma;
import com.edubase.academico.domain.repository.DisciplinaRepository;
import com.edubase.academico.domain.repository.TurmaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class AtualizarTurmaUseCase {

	private final TurmaRepository turmaRepository;
	private final DisciplinaRepository disciplinaRepository;

	public AtualizarTurmaUseCase(TurmaRepository turmaRepository, DisciplinaRepository disciplinaRepository) {
		this.turmaRepository = turmaRepository;
		this.disciplinaRepository = disciplinaRepository;
	}

	public TurmaResponse executar(Long id, TurmaRequest request) {
		Turma turma = turmaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Turma", id));
		Disciplina disciplina = disciplinaRepository.findById(request.getDisciplinaId())
				.orElseThrow(() -> new NotFoundException("Disciplina", request.getDisciplinaId()));
		turma.atualizar(request.getNome(), request.getVagas(), request.getStatus(), disciplina);
		return TurmaMapper.toResponse(turmaRepository.save(turma));
	}
}
