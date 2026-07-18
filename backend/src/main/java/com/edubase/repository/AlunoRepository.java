package com.edubase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.entity.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	boolean existsByEmail(String email);

	boolean existsByEmailAndIdNot(String email, Long id);
}
