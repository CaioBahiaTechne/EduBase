package com.edubase.academico.application.usecase.disciplina;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.domain.repository.DisciplinaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class ExcluirDisciplinaUseCase {

	private final DisciplinaRepository disciplinaRepository;

	public ExcluirDisciplinaUseCase(DisciplinaRepository disciplinaRepository) {
		this.disciplinaRepository = disciplinaRepository;
	}

	public void executar(Long id) {
		if (!disciplinaRepository.existsById(id)) {
			throw new NotFoundException("Disciplina", id);
		}
		disciplinaRepository.deleteById(id);
	}
}
