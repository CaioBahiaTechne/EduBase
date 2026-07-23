package com.edubase.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubase.dto.MatriculaRequest;
import com.edubase.dto.MatriculaResponse;
import com.edubase.entity.Aluno;
import com.edubase.entity.Matricula;
import com.edubase.entity.StatusMatricula;
import com.edubase.entity.Turma;
import com.edubase.exception.BusinessException;
import com.edubase.exception.ResourceNotFoundException;
import com.edubase.mapper.MatriculaMapper;
import com.edubase.repository.MatriculaRepository;

@Service
@Transactional
public class MatriculaService {

	private final MatriculaRepository matriculaRepository;
	private final AlunoService alunoService;
	private final TurmaService turmaService;

	public MatriculaService(
			MatriculaRepository matriculaRepository,
			AlunoService alunoService,
			TurmaService turmaService) {
		this.matriculaRepository = matriculaRepository;
		this.alunoService = alunoService;
		this.turmaService = turmaService;
	}

	@Transactional(readOnly = true)
	public List<MatriculaResponse> listar(Long alunoId, Long turmaId) {
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
	@Transactional(readOnly = true)
	public List<MatriculaResponse> listarPorAluno(Long alunoId) {
		alunoService.buscarEntidade(alunoId);
		return matriculaRepository.findByAlunoId(alunoId).stream()
				.map(MatriculaMapper::toResponse)
				.toList();
	}

	/** RN008 */
	@Transactional(readOnly = true)
	public List<MatriculaResponse> listarPorTurma(Long turmaId) {
		turmaService.buscarEntidade(turmaId);
		return matriculaRepository.findByTurmaId(turmaId).stream()
				.map(MatriculaMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public MatriculaResponse buscarPorId(Long id) {
		return MatriculaMapper.toResponse(buscarEntidade(id));
	}

	/**
	 * Matricular aluno em turma (status inicial sempre {@link StatusMatricula#PENDENTE}).
	 * Aplica RN001, RN002 e RN003.
	 */
	public MatriculaResponse criar(MatriculaRequest request) {
		Aluno aluno = alunoService.buscarEntidade(request.getAlunoId());
		Turma turma = turmaService.buscarEntidade(request.getTurmaId());

		validarTurmaParaMatricula(turma);
		validarMatriculaUnica(aluno.getId(), turma.getId(), null);

		Matricula matricula = new Matricula(aluno, turma);
		matricula.setStatus(StatusMatricula.PENDENTE);
		return MatriculaMapper.toResponse(matriculaRepository.save(matricula));
	}

	/**
	 * Atualiza vínculo aluno/turma apenas se a matrícula ainda estiver PENDENTE.
	 * Mudanças de status usam {@link #confirmar(Long)} / {@link #cancelar(Long)}.
	 */
	public MatriculaResponse atualizar(Long id, MatriculaRequest request) {
		Matricula matricula = buscarEntidade(id);
		if (matricula.getStatus() != StatusMatricula.PENDENTE) {
			throw new BusinessException(
					"Somente matrículas PENDENTE podem ser editadas; use confirmar ou cancelar para alterar o status");
		}

		Aluno aluno = alunoService.buscarEntidade(request.getAlunoId());
		Turma turma = turmaService.buscarEntidade(request.getTurmaId());

		validarTurmaParaMatricula(turma);
		validarMatriculaUnica(aluno.getId(), turma.getId(), id);

		matricula.setAluno(aluno);
		matricula.setTurma(turma);
		return MatriculaMapper.toResponse(matriculaRepository.save(matricula));
	}

	/** RN005 */
	public MatriculaResponse confirmar(Long id) {
		Matricula matricula = buscarEntidade(id);
		if (matricula.getStatus() != StatusMatricula.PENDENTE) {
			throw new BusinessException("Somente matrículas PENDENTE podem ser confirmadas");
		}

		Turma turma = matricula.getTurma();
		validarTurmaParaMatricula(turma);

		try {
			turma.consumirVaga();
		} catch (IllegalStateException ex) {
			throw new BusinessException(ex.getMessage());
		}

		matricula.setStatus(StatusMatricula.CONFIRMADA);
		return MatriculaMapper.toResponse(matriculaRepository.save(matricula));
	}

	/**
	 * RN006 — cancelar confirmada devolve vaga.
	 * Cancelar PENDENTE apenas altera o status (vaga ainda não havia sido consumida).
	 */
	public MatriculaResponse cancelar(Long id) {
		Matricula matricula = buscarEntidade(id);
		StatusMatricula statusAtual = matricula.getStatus();

		if (statusAtual == StatusMatricula.CANCELADA) {
			throw new BusinessException("Matrícula já está CANCELADA");
		}

		if (statusAtual == StatusMatricula.CONFIRMADA) {
			matricula.getTurma().devolverVaga();
		}

		matricula.setStatus(StatusMatricula.CANCELADA);
		return MatriculaMapper.toResponse(matriculaRepository.save(matricula));
	}

	public void excluir(Long id) {
		Matricula matricula = buscarEntidade(id);
		if (matricula.getStatus() == StatusMatricula.CONFIRMADA) {
			matricula.getTurma().devolverVaga();
		}
		matriculaRepository.delete(matricula);
	}

	@Transactional(readOnly = true)
	public Matricula buscarEntidade(Long id) {
		return matriculaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Matrícula", id));
	}

	/** RN001 + RN002 */
	private void validarTurmaParaMatricula(Turma turma) {
		if (!turma.isAberta()) {
			throw new BusinessException("Não é possível matricular em turma FECHADA");
		}
		if (!turma.possuiVagas()) {
			throw new BusinessException("Turma sem vagas disponíveis");
		}
	}

	/** RN003 */
	private void validarMatriculaUnica(Long alunoId, Long turmaId, Long idAtual) {
		matriculaRepository.findByAlunoIdAndTurmaId(alunoId, turmaId)
				.filter(existente -> idAtual == null || !existente.getId().equals(idAtual))
				.ifPresent(existente -> {
					throw new BusinessException("Já existe matrícula deste aluno nesta turma");
				});
	}
}
