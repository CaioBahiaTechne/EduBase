package com.edubase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.entity.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

	List<Disciplina> findByCursoId(Long cursoId);
}
