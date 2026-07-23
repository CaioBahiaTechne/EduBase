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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.edubase.dto.CursoRequest;
import com.edubase.dto.CursoResponse;
import com.edubase.service.CursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

	private final CursoService cursoService;

	public CursoController(CursoService cursoService) {
		this.cursoService = cursoService;
	}

	@GetMapping
	public List<CursoResponse> listar() {
		return cursoService.listar();
	}

	@GetMapping("/{id}")
	public CursoResponse buscar(@PathVariable Long id) {
		return cursoService.buscarPorId(id);
	}

	@PostMapping
	public ResponseEntity<CursoResponse> criar(@Valid @RequestBody CursoRequest request) {
		CursoResponse criado = cursoService.criar(request);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(criado.getId())
				.toUri();
		return ResponseEntity.created(location).body(criado);
	}

	@PutMapping("/{id}")
	public CursoResponse atualizar(@PathVariable Long id, @Valid @RequestBody CursoRequest request) {
		return cursoService.atualizar(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		cursoService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
