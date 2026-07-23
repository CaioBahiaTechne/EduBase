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

import com.edubase.academico.application.dto.AlunoRequest;
import com.edubase.academico.application.dto.AlunoResponse;
import com.edubase.academico.application.usecase.aluno.AtualizarAlunoUseCase;
import com.edubase.academico.application.usecase.aluno.CriarAlunoUseCase;
import com.edubase.academico.application.usecase.aluno.ExcluirAlunoUseCase;
import com.edubase.academico.application.usecase.aluno.ListarAlunosUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

	private final ListarAlunosUseCase listarAlunosUseCase;
	private final CriarAlunoUseCase criarAlunoUseCase;
	private final AtualizarAlunoUseCase atualizarAlunoUseCase;
	private final ExcluirAlunoUseCase excluirAlunoUseCase;

	public AlunoController(
			ListarAlunosUseCase listarAlunosUseCase,
			CriarAlunoUseCase criarAlunoUseCase,
			AtualizarAlunoUseCase atualizarAlunoUseCase,
			ExcluirAlunoUseCase excluirAlunoUseCase) {
		this.listarAlunosUseCase = listarAlunosUseCase;
		this.criarAlunoUseCase = criarAlunoUseCase;
		this.atualizarAlunoUseCase = atualizarAlunoUseCase;
		this.excluirAlunoUseCase = excluirAlunoUseCase;
	}

	@GetMapping
	public List<AlunoResponse> listar(
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String email) {
		return listarAlunosUseCase.executar(nome, email);
	}

	@GetMapping("/{id}")
	public AlunoResponse buscar(@PathVariable Long id) {
		return listarAlunosUseCase.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<AlunoResponse> criar(@Valid @RequestBody AlunoRequest request) {
		AlunoResponse criado = criarAlunoUseCase.executar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public AlunoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
		return atualizarAlunoUseCase.executar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		excluirAlunoUseCase.executar(id);
		return ResponseEntity.noContent().build();
	}
}
