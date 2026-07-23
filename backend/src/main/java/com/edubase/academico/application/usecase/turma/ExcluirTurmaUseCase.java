package com.edubase.academico.application.usecase.turma;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.domain.repository.TurmaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class ExcluirTurmaUseCase {

	private final TurmaRepository turmaRepository;

	public ExcluirTurmaUseCase(TurmaRepository turmaRepository) {
		this.turmaRepository = turmaRepository;
	}

	public void executar(Long id) {
		if (!turmaRepository.existsById(id)) {
			throw new NotFoundException("Turma", id);
		}
		turmaRepository.deleteById(id);
	}
}
