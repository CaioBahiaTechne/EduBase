package com.edubase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.dto.DisciplinaRequest;
import com.edubase.dto.DisciplinaResponse;
import com.edubase.entity.Curso;
import com.edubase.entity.Disciplina;
import com.edubase.exception.ResourceNotFoundException;
import com.edubase.mapper.DisciplinaMapper;
import com.edubase.repository.DisciplinaRepository;

@Service
@Transactional
public class DisciplinaService {

	private final DisciplinaRepository disciplinaRepository;
	private final CursoService cursoService;

	public DisciplinaService(DisciplinaRepository disciplinaRepository, CursoService cursoService) {
		this.disciplinaRepository = disciplinaRepository;
		this.cursoService = cursoService;
	}

	@Transactional(readOnly = true)
	public List<DisciplinaResponse> listar(Long cursoId) {
		List<Disciplina> disciplinas = cursoId != null
				? disciplinaRepository.findByCursoId(cursoId)
				: disciplinaRepository.findAll();
		return disciplinas.stream().map(DisciplinaMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public DisciplinaResponse buscarPorId(Long id) {
		return DisciplinaMapper.toResponse(buscarEntidade(id));
	}

	public DisciplinaResponse criar(DisciplinaRequest request) {
		Curso curso = cursoService.buscarEntidade(request.getCursoId());
		Disciplina disciplina = DisciplinaMapper.toEntity(request, curso);
		return DisciplinaMapper.toResponse(disciplinaRepository.save(disciplina));
	}

	public DisciplinaResponse atualizar(Long id, DisciplinaRequest request) {
		Disciplina disciplina = buscarEntidade(id);
		Curso curso = cursoService.buscarEntidade(request.getCursoId());
		DisciplinaMapper.updateEntity(disciplina, request, curso);
		return DisciplinaMapper.toResponse(disciplinaRepository.save(disciplina));
	}

	public void excluir(Long id) {
		if (!disciplinaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Disciplina", id);
		}
		disciplinaRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Disciplina buscarEntidade(Long id) {
		return disciplinaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Disciplina", id));
	}
}
