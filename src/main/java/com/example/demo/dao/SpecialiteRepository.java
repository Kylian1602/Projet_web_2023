package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entities.Personne;
import com.example.demo.entities.Specialite;

public interface SpecialiteRepository extends JpaRepository<Specialite,Long>{

	@Query("FROM Specialite WHERE nom = ?1 AND nbEtudiant = ?2")
	public Specialite findSpecialite(String nom, int nbEtudiant);
	
	@Query("FROM Specialite WHERE idSpecialite = ?1")
	public Specialite findSpecialite(Long idSpecialite);
}