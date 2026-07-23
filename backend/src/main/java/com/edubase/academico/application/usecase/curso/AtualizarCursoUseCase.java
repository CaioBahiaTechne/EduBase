package com.edubase.academico.application.usecase.curso;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.CursoRequest;
import com.edubase.academico.application.dto.CursoResponse;
import com.edubase.academico.application.mapper.CursoMapper;
import com.edubase.academico.domain.model.curso.Curso;
import com.edubase.academico.domain.repository.CursoRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class AtualizarCursoUseCase {

	private final CursoRepository cursoRepository;

	public AtualizarCursoUseCase(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public CursoResponse executar(Long id, CursoRequest request) {
		Curso curso = cursoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Curso", id));
		curso.renomear(request.getNome());
		return CursoMapper.toResponse(cursoRepository.save(curso));
	}
}
