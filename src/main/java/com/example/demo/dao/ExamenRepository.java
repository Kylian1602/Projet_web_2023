package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Examen;
import com.example.demo.entities.Salle;

public interface ExamenRepository extends JpaRepository<Examen,Long>{

	@Query("FROM Examen WHERE name = ?1")
	public Examen findByName(String name);
	
	@Query("FROM Examen WHERE idExamen = ?1")
	public Examen findByIdExamen(Long idExamen);
}