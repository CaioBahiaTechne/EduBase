package com.edubase.controller;

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

import com.edubase.dto.MatriculaRequest;
import com.edubase.dto.MatriculaResponse;
import com.edubase.service.MatriculaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

	private final MatriculaService matriculaService;

	public MatriculaController(MatriculaService matriculaService) {
		this.matriculaService = matriculaService;
	}

	@GetMapping
	public List<MatriculaResponse> listar(
			@RequestParam(required = false) Long alunoId,
			@RequestParam(required = false) Long turmaId) {
		return matriculaService.listar(alunoId, turmaId);
	}

	/** RN007 */
	@GetMapping("/aluno/{alunoId}")
	public List<MatriculaResponse> listarPorAluno(@PathVariable Long alunoId) {
		return matriculaService.listarPorAluno(alunoId);
	}

	/** RN008 */
	@GetMapping("/turma/{turmaId}")
	public List<MatriculaResponse> listarPorTurma(@PathVariable Long turmaId) {
		return matriculaService.listarPorTurma(turmaId);
	}

	@GetMapping("/{id}")
	public MatriculaResponse buscar(@PathVariable Long id) {
		return matriculaService.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<MatriculaResponse> criar(@Valid @RequestBody MatriculaRequest request) {
		MatriculaResponse criado = matriculaService.criar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	/** RN005 */
	@PostMapping("/{id}/confirmar")
	public MatriculaResponse confirmar(@PathVariable Long id) {
		return matriculaService.confirmar(id);
	}

	/** RN006 */
	@PostMapping("/{id}/cancelar")
	public MatriculaResponse cancelar(@PathVariable Long id) {
		return matriculaService.cancelar(id);
	}

	@PutMapping("/{id}")
	public MatriculaResponse atualizar(
			@PathVariable Long id,
			@Valid @RequestBody MatriculaRequest request) {
		return matriculaService.atualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		matriculaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
