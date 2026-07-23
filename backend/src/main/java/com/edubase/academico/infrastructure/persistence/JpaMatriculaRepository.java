package com.edubase.academico.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.academico.domain.model.matricula.Matricula;
import com.edubase.academico.domain.repository.MatriculaRepository;

public interface JpaMatriculaRepository extends JpaRepository<Matricula, Long>, MatriculaRepository {
}
