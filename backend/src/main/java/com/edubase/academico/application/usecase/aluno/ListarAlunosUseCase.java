package com.edubase.academico.application.usecase.aluno;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.AlunoResponse;
import com.edubase.academico.application.mapper.AlunoMapper;
import com.edubase.academico.domain.repository.AlunoRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class ListarAlunosUseCase {

	private final AlunoRepository alunoRepository;

	public ListarAlunosUseCase(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public List<AlunoResponse> executar(String nome, String email) {
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

	public AlunoResponse buscarPorId(Long id) {
		return alunoRepository.findById(id)
				.map(AlunoMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("Aluno", id));
	}
}
