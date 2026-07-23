package com.edubase.academico.domain.repository;

import java.util.List;
import java.util.Optional;

import com.edubase.academico.domain.model.curso.Disciplina;

public interface DisciplinaRepository {

	Disciplina save(Disciplina disciplina);

	Optional<Disciplina> findById(Long id);

	boolean existsById(Long id);

	void deleteById(Long id);

	List<Disciplina> findAll();

	List<Disciplina> findByCursoId(Long cursoId);
}
