package com.edubase.academico.application.usecase.matricula;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.MatriculaRequest;
import com.edubase.academico.application.dto.MatriculaResponse;
import com.edubase.academico.application.mapper.MatriculaMapper;
import com.edubase.academico.domain.model.aluno.Aluno;
import com.edubase.academico.domain.model.matricula.Matricula;
import com.edubase.academico.domain.model.turma.Turma;
import com.edubase.academico.domain.repository.AlunoRepository;
import com.edubase.academico.domain.repository.MatriculaRepository;
import com.edubase.academico.domain.repository.TurmaRepository;
import com.edubase.academico.domain.service.PoliticaMatricula;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional
public class AtualizarMatriculaUseCase {

	private final MatriculaRepository matriculaRepository;
	private final AlunoRepository alunoRepository;
	private final TurmaRepository turmaRepository;

	public AtualizarMatriculaUseCase(
			MatriculaRepository matriculaRepository,
			AlunoRepository alunoRepository,
			TurmaRepository turmaRepository) {
		this.matriculaRepository = matriculaRepository;
		this.alunoRepository = alunoRepository;
		this.turmaRepository = turmaRepository;
	}

	public MatriculaResponse executar(Long id, MatriculaRequest request) {
		Matricula matricula = matriculaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Matrícula", id));
		Aluno aluno = alunoRepository.findById(request.getAlunoId())
				.orElseThrow(() -> new NotFoundException("Aluno", request.getAlunoId()));
		Turma turma = turmaRepository.findById(request.getTurmaId())
				.orElseThrow(() -> new NotFoundException("Turma", request.getTurmaId()));

		PoliticaMatricula.garantirMatriculaUnica(
				matriculaRepository, aluno.getId(), turma.getId(), id);

		matricula.alterarVinculo(aluno, turma);
		return MatriculaMapper.toResponse(matriculaRepository.save(matricula));
	}
}
