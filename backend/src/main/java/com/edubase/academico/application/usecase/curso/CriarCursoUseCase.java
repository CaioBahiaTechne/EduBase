package com.edubase.academico.application.usecase.curso;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.CursoRequest;
import com.edubase.academico.application.dto.CursoResponse;
import com.edubase.academico.application.mapper.CursoMapper;
import com.edubase.academico.domain.repository.CursoRepository;

@Service
@Transactional
public class CriarCursoUseCase {

	private final CursoRepository cursoRepository;

	public CriarCursoUseCase(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public CursoResponse executar(CursoRequest request) {
		return CursoMapper.toResponse(cursoRepository.save(CursoMapper.toEntity(request)));
	}
}
