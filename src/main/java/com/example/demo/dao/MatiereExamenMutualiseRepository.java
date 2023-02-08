package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.MatiereExamenMutualise;

public interface MatiereExamenMutualiseRepository extends JpaRepository<MatiereExamenMutualise,Long>{

	@Query("FROM MatiereExamenMutualise WHERE nom = ?1 AND idExamen = ?2")
	public MatiereExamenMutualise findMatiereWithIdAndName(String nom, Long idExamen);
}
