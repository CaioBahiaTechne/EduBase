package com.edubase.academico.application.usecase.turma;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.TurmaResponse;
import com.edubase.academico.application.mapper.TurmaMapper;
import com.edubase.academico.domain.model.turma.StatusTurma;
import com.edubase.academico.domain.model.turma.Turma;
import com.edubase.academico.domain.repository.TurmaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class ListarTurmasUseCase {

	private final TurmaRepository turmaRepository;

	public ListarTurmasUseCase(TurmaRepository turmaRepository) {
		this.turmaRepository = turmaRepository;
	}

	public List<TurmaResponse> executar(Long disciplinaId, StatusTurma status) {
		List<Turma> turmas;
		if (disciplinaId != null) {
			turmas = turmaRepository.findByDisciplinaId(disciplinaId);
		} else if (status != null) {
			turmas = turmaRepository.findByStatus(status);
		} else {
			turmas = turmaRepository.findAll();
		}
		return turmas.stream().map(TurmaMapper::toResponse).toList();
	}

	public TurmaResponse buscarPorId(Long id) {
		return turmaRepository.findById(id)
				.map(TurmaMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("Turma", id));
	}
}
