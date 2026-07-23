package com.edubase.academico.domain.repository;

import java.util.List;
import java.util.Optional;

import com.edubase.academico.domain.model.turma.StatusTurma;
import com.edubase.academico.domain.model.turma.Turma;

public interface TurmaRepository {

	Turma save(Turma turma);

	Optional<Turma> findById(Long id);

	boolean existsById(Long id);

	void deleteById(Long id);

	List<Turma> findAll();

	List<Turma> findByDisciplinaId(Long disciplinaId);

	List<Turma> findByStatus(StatusTurma status);
}
