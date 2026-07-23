package com.edubase.academico.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.academico.domain.model.curso.Curso;
import com.edubase.academico.domain.repository.CursoRepository;

public interface JpaCursoRepository extends JpaRepository<Curso, Long>, CursoRepository {
}
