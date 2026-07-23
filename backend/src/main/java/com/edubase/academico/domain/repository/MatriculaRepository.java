package com.edubase.academico.domain.repository;

import java.util.List;
import java.util.Optional;

import com.edubase.academico.domain.model.matricula.Matricula;

public interface MatriculaRepository {

	Matricula save(Matricula matricula);

	Optional<Matricula> findById(Long id);

	void delete(Matricula matricula);

	List<Matricula> findAll();

	List<Matricula> findByAlunoId(Long alunoId);

	List<Matricula> findByTurmaId(Long turmaId);

	Optional<Matricula> findByAlunoIdAndTurmaId(Long alunoId, Long turmaId);
}
