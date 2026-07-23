package com.edubase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.dto.AlunoRequest;
import com.edubase.dto.AlunoResponse;
import com.edubase.entity.Aluno;
import com.edubase.exception.BusinessException;
import com.edubase.exception.ResourceNotFoundException;
import com.edubase.mapper.AlunoMapper;
import com.edubase.repository.AlunoRepository;

@Service
@Transactional
public class AlunoService {

	private final AlunoRepository alunoRepository;

	public AlunoService(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	@Transactional(readOnly = true)
	public List<AlunoResponse> listar(String nome, String email) {
		if (email != null && !email.isBlank()) {
			return alunoRepository.findByEmailIgnoreCase(email.trim()).stream()
					.map(AlunoMapper::toResponse)
					.toList();
		}
		if (nome != null && !nome.isBlank()) {
			return alunoRepository.findByNomeContainingIgnoreCase(nome.trim()).stream()
					.map(AlunoMapper::toResponse)
					.toList();
		}
		return alunoRepository.findAll().stream().map(AlunoMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	public AlunoResponse buscarPorId(Long id) {
		return AlunoMapper.toResponse(buscarEntidade(id));
	}

	public AlunoResponse criar(AlunoRequest request) {
		validarEmailUnico(request.getEmail(), null);
		Aluno aluno = AlunoMapper.toEntity(request);
		return AlunoMapper.toResponse(alunoRepository.save(aluno));
	}

	public AlunoResponse atualizar(Long id, AlunoRequest request) {
		Aluno aluno = buscarEntidade(id);
		validarEmailUnico(request.getEmail(), id);
		AlunoMapper.updateEntity(aluno, request);
		return AlunoMapper.toResponse(alunoRepository.save(aluno));
	}

	public void excluir(Long id) {
		if (!alunoRepository.existsById(id)) {
			throw new ResourceNotFoundException("Aluno", id);
		}
		alunoRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Aluno buscarEntidade(Long id) {
		return alunoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Aluno", id));
	}

	private void validarEmailUnico(String email, Long idAtual) {
		boolean existe = idAtual == null
				? alunoRepository.existsByEmailIgnoreCase(email)
				: alunoRepository.existsByEmailIgnoreCaseAndIdNot(email, idAtual);
		if (existe) {
			throw new BusinessException("Já existe aluno com o email: " + email);
		}
	}
}
