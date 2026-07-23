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

import com.edubase.dto.DisciplinaRequest;
import com.edubase.dto.DisciplinaResponse;
import com.edubase.service.DisciplinaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

	private final DisciplinaService disciplinaService;

	public DisciplinaController(DisciplinaService disciplinaService) {
		this.disciplinaService = disciplinaService;
	}

	@GetMapping
	public List<DisciplinaResponse> listar(@RequestParam(required = false) Long cursoId) {
		return disciplinaService.listar(cursoId);
	}

	@GetMapping("/{id}")
	public DisciplinaResponse buscar(@PathVariable Long id) {
		return disciplinaService.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<DisciplinaResponse> criar(@Valid @RequestBody DisciplinaRequest request) {
		DisciplinaResponse criado = disciplinaService.criar(request);
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
		return disciplinaService.atualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		disciplinaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
