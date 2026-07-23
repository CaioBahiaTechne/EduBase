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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edubase.academico.application.dto.CursoRequest;
import com.edubase.academico.application.dto.CursoResponse;
import com.edubase.academico.application.usecase.curso.AtualizarCursoUseCase;
import com.edubase.academico.application.usecase.curso.CriarCursoUseCase;
import com.edubase.academico.application.usecase.curso.ExcluirCursoUseCase;
import com.edubase.academico.application.usecase.curso.ListarCursosUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

	private final ListarCursosUseCase listarCursosUseCase;
	private final CriarCursoUseCase criarCursoUseCase;
	private final AtualizarCursoUseCase atualizarCursoUseCase;
	private final ExcluirCursoUseCase excluirCursoUseCase;

	public CursoController(
			ListarCursosUseCase listarCursosUseCase,
			CriarCursoUseCase criarCursoUseCase,
			AtualizarCursoUseCase atualizarCursoUseCase,
			ExcluirCursoUseCase excluirCursoUseCase) {
		this.listarCursosUseCase = listarCursosUseCase;
		this.criarCursoUseCase = criarCursoUseCase;
		this.atualizarCursoUseCase = atualizarCursoUseCase;
		this.excluirCursoUseCase = excluirCursoUseCase;
	}

	@GetMapping
	public List<CursoResponse> listar() {
		return listarCursosUseCase.executar();
	}

	@GetMapping("/{id}")
	public CursoResponse buscar(@PathVariable Long id) {
		return listarCursosUseCase.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<CursoResponse> criar(@Valid @RequestBody CursoRequest request) {
		CursoResponse criado = criarCursoUseCase.executar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public CursoResponse atualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
		return atualizarCursoUseCase.executar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		excluirCursoUseCase.executar(id);
		return ResponseEntity.noContent().build();
	}
}
