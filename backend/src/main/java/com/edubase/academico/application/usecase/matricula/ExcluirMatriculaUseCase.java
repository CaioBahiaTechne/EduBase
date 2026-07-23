package com.edubase.academico.application.usecase.matricula;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.domain.model.matricula.Matricula;
import com.edubase.academico.domain.repository.MatriculaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class ExcluirMatriculaUseCase {

	private final MatriculaRepository matriculaRepository;

	public ExcluirMatriculaUseCase(MatriculaRepository matriculaRepository) {
		this.matriculaRepository = matriculaRepository;
	}

	public void executar(Long id) {
		Matricula matricula = matriculaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Matrícula", id));
		matricula.prepararExclusao();
		matriculaRepository.delete(matricula);
	}
}
