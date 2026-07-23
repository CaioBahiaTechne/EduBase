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

import com.edubase.academico.application.dto.TurmaRequest;
import com.edubase.academico.application.dto.TurmaResponse;
import com.edubase.academico.application.usecase.turma.AtualizarTurmaUseCase;
import com.edubase.academico.application.usecase.turma.CriarTurmaUseCase;
import com.edubase.academico.application.usecase.turma.ExcluirTurmaUseCase;
import com.edubase.academico.application.usecase.turma.ListarTurmasUseCase;
import com.edubase.academico.domain.model.turma.StatusTurma;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

	private final ListarTurmasUseCase listarTurmasUseCase;
	private final CriarTurmaUseCase criarTurmaUseCase;
	private final AtualizarTurmaUseCase atualizarTurmaUseCase;
	private final ExcluirTurmaUseCase excluirTurmaUseCase;

	public TurmaController(
			ListarTurmasUseCase listarTurmasUseCase,
			CriarTurmaUseCase criarTurmaUseCase,
			AtualizarTurmaUseCase atualizarTurmaUseCase,
			ExcluirTurmaUseCase excluirTurmaUseCase) {
		this.listarTurmasUseCase = listarTurmasUseCase;
		this.criarTurmaUseCase = criarTurmaUseCase;
		this.atualizarTurmaUseCase = atualizarTurmaUseCase;
		this.excluirTurmaUseCase = excluirTurmaUseCase;
	}

	@GetMapping
	public List<TurmaResponse> listar(
			@RequestParam(required = false) Long disciplinaId,
			@RequestParam(required = false) StatusTurma status) {
		return listarTurmasUseCase.executar(disciplinaId, status);
	}

	@GetMapping("/{id}")
	public TurmaResponse buscar(@PathVariable Long id) {
		return listarTurmasUseCase.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<TurmaResponse> criar(@Valid @RequestBody TurmaRequest request) {
		TurmaResponse criado = criarTurmaUseCase.executar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public TurmaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TurmaRequest request) {
		return atualizarTurmaUseCase.executar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		excluirTurmaUseCase.executar(id);
		return ResponseEntity.noContent().build();
	}
}
