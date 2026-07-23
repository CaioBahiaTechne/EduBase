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

import com.edubase.academico.application.dto.DisciplinaRequest;
import com.edubase.academico.application.dto.DisciplinaResponse;
import com.edubase.academico.application.usecase.disciplina.AtualizarDisciplinaUseCase;
import com.edubase.academico.application.usecase.disciplina.CriarDisciplinaUseCase;
import com.edubase.academico.application.usecase.disciplina.ExcluirDisciplinaUseCase;
import com.edubase.academico.application.usecase.disciplina.ListarDisciplinasUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

	private final ListarDisciplinasUseCase listarDisciplinasUseCase;
	private final CriarDisciplinaUseCase criarDisciplinaUseCase;
	private final AtualizarDisciplinaUseCase atualizarDisciplinaUseCase;
	private final ExcluirDisciplinaUseCase excluirDisciplinaUseCase;

	public DisciplinaController(
			ListarDisciplinasUseCase listarDisciplinasUseCase,
			CriarDisciplinaUseCase criarDisciplinaUseCase,
			AtualizarDisciplinaUseCase atualizarDisciplinaUseCase,
			ExcluirDisciplinaUseCase excluirDisciplinaUseCase) {
		this.listarDisciplinasUseCase = listarDisciplinasUseCase;
		this.criarDisciplinaUseCase = criarDisciplinaUseCase;
		this.atualizarDisciplinaUseCase = atualizarDisciplinaUseCase;
		this.excluirDisciplinaUseCase = excluirDisciplinaUseCase;
	}

	@GetMapping
	public List<DisciplinaResponse> listar(@RequestParam(required = false) Long cursoId) {
		return listarDisciplinasUseCase.executar(cursoId);
	}

	@GetMapping("/{id}")
	public DisciplinaResponse buscar(@PathVariable Long id) {
		return listarDisciplinasUseCase.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<DisciplinaResponse> criar(@Valid @RequestBody DisciplinaRequest request) {
		DisciplinaResponse criado = criarDisciplinaUseCase.executar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public DisciplinaResponse atualizar(
			@PathVariable Long id,
			@Valid @RequestBody DisciplinaRequest request) {
		return atualizarDisciplinaUseCase.executar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		excluirDisciplinaUseCase.executar(id);
		return ResponseEntity.noContent().build();
	}
}
