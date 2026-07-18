package com.edubase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubase.entity.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
