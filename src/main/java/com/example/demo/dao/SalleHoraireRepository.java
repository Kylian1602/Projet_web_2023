package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.Salle;
import com.example.demo.entities.SalleHoraire;

public interface SalleHoraireRepository extends JpaRepository<SalleHoraire,Long>{

	@Query("FROM SalleHoraire WHERE idSalle = ?1 AND idExamen = ?2")
	public SalleHoraire findSalleHoraireForExamen(Long idSalle, Long idExamen);
	
	@Query("FROM SalleHoraire WHERE idExamen = ?1")
	public List<SalleHoraire> findForExamen(Long idExamen);
}