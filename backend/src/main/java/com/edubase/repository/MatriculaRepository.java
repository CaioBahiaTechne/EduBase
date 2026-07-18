package com.edubase.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.entity.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

	List<Matricula> findByAlunoId(Long alunoId);

	List<Matricula> findByTurmaId(Long turmaId);

	boolean existsByAlunoIdAndTurmaId(Long alunoId, Long turmaId);

	Optional<Matricula> findByAlunoIdAndTurmaId(Long alunoId, Long turmaId);
}
