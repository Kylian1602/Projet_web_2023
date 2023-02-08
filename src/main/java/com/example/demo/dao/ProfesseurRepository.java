package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Personne;
import com.example.demo.entities.Professeur;

public interface ProfesseurRepository extends JpaRepository<Professeur,Long>{

	
	@Query("FROM Professeur WHERE idPersonne = ?1")
	public Professeur findUser(Long id);
}