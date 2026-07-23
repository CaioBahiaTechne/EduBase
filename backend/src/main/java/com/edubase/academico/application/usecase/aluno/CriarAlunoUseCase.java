package com.edubase.academico.application.usecase.aluno;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.AlunoRequest;
import com.edubase.academico.application.dto.AlunoResponse;
import com.edubase.academico.application.mapper.AlunoMapper;
import com.edubase.academico.domain.model.aluno.Aluno;
import com.edubase.academico.domain.repository.AlunoRepository;
import com.edubase.shared.domain.exception.DomainException;

@Service
@Transactional
public class CriarAlunoUseCase {

	private final AlunoRepository alunoRepository;

	public CriarAlunoUseCase(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public AlunoResponse executar(AlunoRequest request) {
		if (alunoRepository.existsByEmailIgnoreCase(request.getEmail())) {
			throw new DomainException("Já existe aluno com o email: " + request.getEmail());
		}
		Aluno aluno = AlunoMapper.toEntity(request);
		return AlunoMapper.toResponse(alunoRepository.save(aluno));
	}
}
