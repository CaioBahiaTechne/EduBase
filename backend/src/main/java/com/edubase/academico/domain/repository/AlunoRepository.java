package com.edubase.academico.domain.repository;

import java.util.List;
import java.util.Optional;

import com.edubase.academico.domain.model.aluno.Aluno;

public interface AlunoRepository {

	Aluno save(Aluno aluno);

	Optional<Aluno> findById(Long id);

	boolean existsById(Long id);

	void deleteById(Long id);

	List<Aluno> findAll();

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

	Optional<Aluno> findByEmailIgnoreCase(String email);

	List<Aluno> findByNomeContainingIgnoreCase(String nome);
}
