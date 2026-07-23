package com.edubase.academico.infrastructure.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edubase.academico.application.dto.MatriculaRequest;
import com.edubase.academico.application.dto.MatriculaResponse;
import com.edubase.academico.application.usecase.matricula.AtualizarMatriculaUseCase;
import com.edubase.academico.application.usecase.matricula.CancelarMatriculaUseCase;
import com.edubase.academico.application.usecase.matricula.ConfirmarMatriculaUseCase;
import com.edubase.academico.application.usecase.matricula.ExcluirMatriculaUseCase;
import com.edubase.academico.application.usecase.matricula.ListarMatriculasUseCase;
import com.edubase.academico.application.usecase.matricula.MatricularAlunoUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

	private final ListarMatriculasUseCase listarMatriculasUseCase;
	private final MatricularAlunoUseCase matricularAlunoUseCase;
	private final ConfirmarMatriculaUseCase confirmarMatriculaUseCase;
	private final CancelarMatriculaUseCase cancelarMatriculaUseCase;
	private final AtualizarMatriculaUseCase atualizarMatriculaUseCase;
	private final ExcluirMatriculaUseCase excluirMatriculaUseCase;

	public MatriculaController(
			ListarMatriculasUseCase listarMatriculasUseCase,
			MatricularAlunoUseCase matricularAlunoUseCase,
			ConfirmarMatriculaUseCase confirmarMatriculaUseCase,
			CancelarMatriculaUseCase cancelarMatriculaUseCase,
			AtualizarMatriculaUseCase atualizarMatriculaUseCase,
			ExcluirMatriculaUseCase excluirMatriculaUseCase) {
		this.listarMatriculasUseCase = listarMatriculasUseCase;
		this.matricularAlunoUseCase = matricularAlunoUseCase;
		this.confirmarMatriculaUseCase = confirmarMatriculaUseCase;
		this.cancelarMatriculaUseCase = cancelarMatriculaUseCase;
		this.atualizarMatriculaUseCase = atualizarMatriculaUseCase;
		this.excluirMatriculaUseCase = excluirMatriculaUseCase;
	}

	@GetMapping
	public List<MatriculaResponse> listar(
			@RequestParam(required = false) Long alunoId,
			@RequestParam(required = false) Long turmaId) {
		return listarMatriculasUseCase.executar(alunoId, turmaId);
	}

	/** RN007 */
	@GetMapping("/aluno/{alunoId}")
	public List<MatriculaResponse> listarPorAluno(@PathVariable Long alunoId) {
		return listarMatriculasUseCase.porAluno(alunoId);
	}

	/** RN008 */
	@GetMapping("/turma/{turmaId}")
	public List<MatriculaResponse> listarPorTurma(@PathVariable Long turmaId) {
		return listarMatriculasUseCase.porTurma(turmaId);
	}

	@GetMapping("/{id}")
	public MatriculaResponse buscar(@PathVariable Long id) {
		return listarMatriculasUseCase.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<MatriculaResponse> criar(@Valid @RequestBody MatriculaRequest request) {
		MatriculaResponse criado = matricularAlunoUseCase.executar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	/** RN005 */
	@PostMapping("/{id}/confirmar")
	public MatriculaResponse confirmar(@PathVariable Long id) {
		return confirmarMatriculaUseCase.executar(id);
	}

	/** RN006 */
	@PostMapping("/{id}/cancelar")
	public MatriculaResponse cancelar(@PathVariable Long id) {
		return cancelarMatriculaUseCase.executar(id);
	}

	@PutMapping("/{id}")
	public MatriculaResponse atualizar(
			@PathVariable Long id,
			@Valid @RequestBody MatriculaRequest request) {
		return atualizarMatriculaUseCase.executar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		excluirMatriculaUseCase.executar(id);
		return ResponseEntity.noContent().build();
	}
}
