package com.edubase.academico.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.academico.domain.model.aluno.Aluno;
import com.edubase.academico.domain.repository.AlunoRepository;

public interface JpaAlunoRepository extends JpaRepository<Aluno, Long>, AlunoRepository {
}
