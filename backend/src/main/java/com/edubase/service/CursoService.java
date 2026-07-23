package com.edubase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.dto.CursoRequest;
import com.edubase.dto.CursoResponse;
import com.edubase.entity.Curso;
import com.edubase.exception.ResourceNotFoundException;
import com.edubase.mapper.CursoMapper;
import com.edubase.repository.CursoRepository;

@Service
@Transactional
public class CursoService {

	private final CursoRepository cursoRepository;

	public CursoService(CursoRepository cursoRepository) {
		this.cursoRepository = cursoRepository;
	}

	@Transactional(readOnly = true)
	public List<CursoResponse> listar() {
		return cursoRepository.findAll().stream().map(CursoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public CursoResponse buscarPorId(Long id) {
		return CursoMapper.toResponse(buscarEntidade(id));
	}

	public CursoResponse criar(CursoRequest request) {
		Curso curso = CursoMapper.toEntity(request);
		return CursoMapper.toResponse(cursoRepository.save(curso));
	}

	public CursoResponse atualizar(Long id, CursoRequest request) {
		Curso curso = buscarEntidade(id);
		CursoMapper.updateEntity(curso, request);
		return CursoMapper.toResponse(cursoRepository.save(curso));
	}

	public void excluir(Long id) {
		if (!cursoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Curso", id);
		}
		cursoRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Curso buscarEntidade(Long id) {
		return cursoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Curso", id));
	}
}
