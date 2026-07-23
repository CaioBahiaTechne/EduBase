package com.edubase.academico.application.usecase.matricula;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.MatriculaResponse;
import com.edubase.academico.application.mapper.MatriculaMapper;
import com.edubase.academico.domain.model.matricula.Matricula;
import com.edubase.academico.domain.repository.MatriculaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class CancelarMatriculaUseCase {

	private final MatriculaRepository matriculaRepository;

	public CancelarMatriculaUseCase(MatriculaRepository matriculaRepository) {
		this.matriculaRepository = matriculaRepository;
	}

	public MatriculaResponse executar(Long id) {
		Matricula matricula = matriculaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Matrícula", id));
		matricula.cancelar();
		return MatriculaMapper.toResponse(matriculaRepository.save(matricula));
	}
}
