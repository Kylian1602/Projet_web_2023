package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Salle;

public interface SalleRepository extends JpaRepository<Salle,Long>{

	@Query("FROM Salle WHERE numero = ?1 AND nbPlace = ?2")
	public Salle findSalle(String numero, int nbPlace);
	
	@Query("FROM Salle WHERE idSalle = ?1")
	public Salle findSalle(Long idSalle);
}
