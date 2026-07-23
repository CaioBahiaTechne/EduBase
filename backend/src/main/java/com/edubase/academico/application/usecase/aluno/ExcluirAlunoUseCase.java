package com.edubase.academico.application.usecase.aluno;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.domain.repository.AlunoRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class ExcluirAlunoUseCase {

	private final AlunoRepository alunoRepository;

	public ExcluirAlunoUseCase(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public void executar(Long id) {
		if (!alunoRepository.existsById(id)) {
			throw new NotFoundException("Aluno", id);
		}
		alunoRepository.deleteById(id);
	}
}
