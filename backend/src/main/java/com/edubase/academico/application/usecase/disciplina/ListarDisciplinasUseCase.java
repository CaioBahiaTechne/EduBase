package com.edubase.academico.application.usecase.disciplina;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.DisciplinaResponse;
import com.edubase.academico.application.mapper.DisciplinaMapper;
import com.edubase.academico.domain.model.curso.Disciplina;
import com.edubase.academico.domain.repository.DisciplinaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class ListarDisciplinasUseCase {

	private final DisciplinaRepository disciplinaRepository;

	public ListarDisciplinasUseCase(DisciplinaRepository disciplinaRepository) {
		this.disciplinaRepository = disciplinaRepository;
	}

	public List<DisciplinaResponse> executar(Long cursoId) {
		List<Disciplina> disciplinas = cursoId != null
				? disciplinaRepository.findByCursoId(cursoId)
				: disciplinaRepository.findAll();
		return disciplinas.stream().map(DisciplinaMapper::toResponse).toList();
	}

	public DisciplinaResponse buscarPorId(Long id) {
		return disciplinaRepository.findById(id)
				.map(DisciplinaMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("Disciplina", id));
	}
}
