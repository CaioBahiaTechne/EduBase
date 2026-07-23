package com.edubase.academico.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.academico.domain.model.curso.Disciplina;
import com.edubase.academico.domain.repository.DisciplinaRepository;

public interface JpaDisciplinaRepository extends JpaRepository<Disciplina, Long>, DisciplinaRepository {
}
