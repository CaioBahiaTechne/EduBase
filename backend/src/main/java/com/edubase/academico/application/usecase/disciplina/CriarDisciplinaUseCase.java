package com.edubase.academico.application.usecase.disciplina;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.DisciplinaRequest;
import com.edubase.academico.application.dto.DisciplinaResponse;
import com.edubase.academico.application.mapper.DisciplinaMapper;
import com.edubase.academico.domain.model.curso.Curso;
import com.edubase.academico.domain.model.curso.Disciplina;
import com.edubase.academico.domain.repository.CursoRepository;
import com.edubase.academico.domain.repository.DisciplinaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class CriarDisciplinaUseCase {

	private final DisciplinaRepository disciplinaRepository;
	private final CursoRepository cursoRepository;

	public CriarDisciplinaUseCase(DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository) {
		this.disciplinaRepository = disciplinaRepository;
		this.cursoRepository = cursoRepository;
	}

	public DisciplinaResponse executar(DisciplinaRequest request) {
		Curso curso = cursoRepository.findById(request.getCursoId())
				.orElseThrow(() -> new NotFoundException("Curso", request.getCursoId()));
		Disciplina disciplina = DisciplinaMapper.toEntity(request, curso);
		return DisciplinaMapper.toResponse(disciplinaRepository.save(disciplina));
	}
}
