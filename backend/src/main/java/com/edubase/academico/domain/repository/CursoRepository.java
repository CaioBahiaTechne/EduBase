package com.edubase.academico.domain.repository;

import java.util.List;
import java.util.Optional;

import com.edubase.academico.domain.model.curso.Curso;

public interface CursoRepository {

	Curso save(Curso curso);

	Optional<Curso> findById(Long id);

	boolean existsById(Long id);

	void deleteById(Long id);

	List<Curso> findAll();
}
