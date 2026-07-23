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

import com.edubase.dto.AlunoRequest;
import com.edubase.dto.AlunoResponse;
import com.edubase.service.AlunoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

	private final AlunoService alunoService;

	public AlunoController(AlunoService alunoService) {
		this.alunoService = alunoService;
	}

	@GetMapping
	public List<AlunoResponse> listar(
			@RequestParam(required = false) String nome,
			@RequestParam(required = false) String email) {
		return alunoService.listar(nome, email);
	}

	@GetMapping("/{id}")
	public AlunoResponse buscar(@PathVariable Long id) {
		return alunoService.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<AlunoResponse> criar(@Valid @RequestBody AlunoRequest request) {
		AlunoResponse criado = alunoService.criar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public AlunoResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlunoRequest request) {
		return alunoService.atualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		alunoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
