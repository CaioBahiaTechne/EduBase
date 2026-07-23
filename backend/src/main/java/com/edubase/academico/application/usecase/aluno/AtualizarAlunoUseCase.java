package com.edubase.academico.application.usecase.aluno;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.AlunoRequest;
import com.edubase.academico.application.dto.AlunoResponse;
import com.edubase.academico.application.mapper.AlunoMapper;
import com.edubase.academico.domain.model.aluno.Aluno;
import com.edubase.academico.domain.repository.AlunoRepository;
import com.edubase.shared.domain.exception.DomainException;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class AtualizarAlunoUseCase {

	private final AlunoRepository alunoRepository;

	public AtualizarAlunoUseCase(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public AlunoResponse executar(Long id, AlunoRequest request) {
		Aluno aluno = alunoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Aluno", id));
		if (alunoRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
			throw new DomainException("Já existe aluno com o email: " + request.getEmail());
		}
		aluno.atualizarDados(request.getNome(), request.getEmail());
		return AlunoMapper.toResponse(alunoRepository.save(aluno));
	}
}
