package com.edubase.academico.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.academico.domain.model.turma.Turma;
import com.edubase.academico.domain.repository.TurmaRepository;

public interface JpaTurmaRepository extends JpaRepository<Turma, Long>, TurmaRepository {
}
