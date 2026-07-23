package com.edubase.academico.application.usecase.matricula;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.academico.application.dto.MatriculaResponse;
import com.edubase.academico.application.mapper.MatriculaMapper;
import com.edubase.academico.domain.model.matricula.Matricula;
import com.edubase.academico.domain.repository.AlunoRepository;
import com.edubase.academico.domain.repository.MatriculaRepository;
import com.edubase.academico.domain.repository.TurmaRepository;
import com.edubase.shared.domain.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class ListarMatriculasUseCase {

	private final MatriculaRepository matriculaRepository;
	private final AlunoRepository alunoRepository;
	private final TurmaRepository turmaRepository;

	public ListarMatriculasUseCase(
			MatriculaRepository matriculaRepository,
			AlunoRepository alunoRepository,
			TurmaRepository turmaRepository) {
		this.matriculaRepository = matriculaRepository;
		this.alunoRepository = alunoRepository;
		this.turmaRepository = turmaRepository;
	}

	public List<MatriculaResponse> executar(Long alunoId, Long turmaId) {
		List<Matricula> matriculas;
		if (alunoId != null) {
			matriculas = matriculaRepository.findByAlunoId(alunoId);
		} else if (turmaId != null) {
			matriculas = matriculaRepository.findByTurmaId(turmaId);
		} else {
			matriculas = matriculaRepository.findAll();
		}
		return matriculas.stream().map(MatriculaMapper::toResponse).toList();
	}

	/** RN007 */
	public List<MatriculaResponse> porAluno(Long alunoId) {
		if (!alunoRepository.existsById(alunoId)) {
			throw new NotFoundException("Aluno", alunoId);
		}
		return matriculaRepository.findByAlunoId(alunoId).stream()
				.map(MatriculaMapper::toResponse)
				.toList();
	}

	/** RN008 */
	public List<MatriculaResponse> porTurma(Long turmaId) {
		if (!turmaRepository.existsById(turmaId)) {
			throw new NotFoundException("Turma", turmaId);
		}
		return matriculaRepository.findByTurmaId(turmaId).stream()
				.map(MatriculaMapper::toResponse)
				.toList();
	}

	public MatriculaResponse buscarPorId(Long id) {
		return matriculaRepository.findById(id)
				.map(MatriculaMapper::toResponse)
				.orElseThrow(() -> new NotFoundException("Matrícula", id));
	}
}
