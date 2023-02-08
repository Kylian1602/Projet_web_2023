package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Eleve;

public interface EleveRepository extends JpaRepository<Eleve,Long>{

	@Query("FROM Eleve WHERE idPersonne = ?1")
	public Eleve findUser(Long id);
}
