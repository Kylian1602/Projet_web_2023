package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Matiere;

public interface MatiereRepository extends JpaRepository<Matiere,Long>{

	@Query("FROM Matiere WHERE idSpecialite = ?1 AND nom = ?2")
	public Matiere findMatiere(Long idSpecialite, String nom);
	
	@Query("FROM Matiere WHERE idSpecialite = ?1")
	public List<Matiere> findAllMatiereOfSpecialite(Long idSpecialite);
	
}