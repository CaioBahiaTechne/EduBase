package com.edubase.academico.application.usecase.curso;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.CursoResponse;
import com.edubase.academico.application.mapper.CursoMapper;
import com.edubase.academico.domain.repository.CursoRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class ListarCursosUseCase {

	private final CursoRepository cursoRepository;

	public ListarCursosUseCase(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	public List<CursoResponse> executar() {
		return cursoRepository.findAll().stream().map(CursoMapper::toResponse).toList();
	}

	public CursoResponse buscarPorId(Long id) {
		return cursoRepository.findById(id)
				.map(CursoMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("Curso", id));
	}
}
