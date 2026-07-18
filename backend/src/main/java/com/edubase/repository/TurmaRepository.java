package com.edubase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.entity.StatusTurma;
import com.edubase.entity.Turma;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

	List<Turma> findByDisciplinaId(Long disciplinaId);

	List<Turma> findByStatus(StatusTurma status);
}
