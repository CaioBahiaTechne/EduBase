package com.edubase.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.entity.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

	Optional<Aluno> findByEmailIgnoreCase(String email);

	List<Aluno> findByNomeContainingIgnoreCase(String nome);
}
