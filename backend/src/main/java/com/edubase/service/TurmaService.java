package com.edubase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.dto.TurmaRequest;
import com.edubase.dto.TurmaResponse;
import com.edubase.entity.Disciplina;
import com.edubase.entity.StatusTurma;
import com.edubase.entity.Turma;
import com.edubase.exception.ResourceNotFoundException;
import com.edubase.mapper.TurmaMapper;
import com.edubase.repository.TurmaRepository;

@Service
@Transactional
public class TurmaService {

	private final TurmaRepository turmaRepository;
	private final DisciplinaService disciplinaService;

	public TurmaService(TurmaRepository turmaRepository, DisciplinaService disciplinaService) {
		this.turmaRepository = turmaRepository;
		this.disciplinaService = disciplinaService;
	}

	@Transactional(readOnly = true)
	public List<TurmaResponse> listar(Long disciplinaId, StatusTurma status) {
		List<Turma> turmas;
		if (disciplinaId != null) {
			turmas = turmaRepository.findByDisciplinaId(disciplinaId);
		} else if (status != null) {
			turmas = turmaRepository.findByStatus(status);
		} else {
			turmas = turmaRepository.findAll();
		}
		return turmas.stream().map(TurmaMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public TurmaResponse buscarPorId(Long id) {
		return TurmaMapper.toResponse(buscarEntidade(id));
	}

	public TurmaResponse criar(TurmaRequest request) {
		Disciplina disciplina = disciplinaService.buscarEntidade(request.getDisciplinaId());
		Turma turma = TurmaMapper.toEntity(request, disciplina);
		return TurmaMapper.toResponse(turmaRepository.save(turma));
	}

	public TurmaResponse atualizar(Long id, TurmaRequest request) {
		Turma turma = buscarEntidade(id);
		Disciplina disciplina = disciplinaService.buscarEntidade(request.getDisciplinaId());
		TurmaMapper.updateEntity(turma, request, disciplina);
		return TurmaMapper.toResponse(turmaRepository.save(turma));
	}

	public void excluir(Long id) {
		if (!turmaRepository.existsById(id)) {
			throw new ResourceNotFoundException("Turma", id);
		}
		turmaRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Turma buscarEntidade(Long id) {
		return turmaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Turma", id));
	}
}
