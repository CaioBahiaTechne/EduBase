package com.edubase.academico.application.usecase.curso;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.domain.repository.CursoRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class ExcluirCursoUseCase {

	private final CursoRepository cursoRepository;

	public ExcluirCursoUseCase(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public void executar(Long id) {
		if (!cursoRepository.existsById(id)) {
			throw new NotFoundException("Curso", id);
		}
		cursoRepository.deleteById(id);
	}
}
