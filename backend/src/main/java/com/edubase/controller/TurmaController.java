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

import com.edubase.dto.TurmaRequest;
import com.edubase.dto.TurmaResponse;
import com.edubase.entity.StatusTurma;
import com.edubase.service.TurmaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

	private final TurmaService turmaService;

	public TurmaController(TurmaService turmaService) {
		this.turmaService = turmaService;
	}

	@GetMapping
	public List<TurmaResponse> listar(
			@RequestParam(required = false) Long disciplinaId,
			@RequestParam(required = false) StatusTurma status) {
		return turmaService.listar(disciplinaId, status);
	}

	@GetMapping("/{id}")
	public TurmaResponse buscar(@PathVariable Long id) {
		return turmaService.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<TurmaResponse> criar(@Valid @RequestBody TurmaRequest request) {
		TurmaResponse criado = turmaService.criar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public TurmaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TurmaRequest request) {
		return turmaService.atualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		turmaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
