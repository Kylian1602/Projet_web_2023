package com.example.demo.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Personne;
import com.example.demo.entities.PlanningExamen;

@Repository
public interface PlanningExamenRepository extends JpaRepository<PlanningExamen,Long>{
	
	@Query("FROM PlanningExamen WHERE idExamen = ?1 AND idSpecialite = ?2 AND idMatiere = ?3 AND dateJour = ?4 AND horaireDebut = ?5 AND horaireFin = ?6")
	public PlanningExamen findDate(Long idExamen, Long idSpecialite, Long idMatiere,LocalDate dateJour, String horaireDebut, String horaireFin);
	
	@Query("FROM PlanningExamen WHERE idExamen = ?1 AND idSpecialite = ?2 AND idMatiere = ?3")
	public List<PlanningExamen> findDateOfMatiere(Long idExamen, Long idSpecialite, Long idMatiere);
	
	@Query("FROM PlanningExamen WHERE idExamen = ?1 AND idSpecialite = ?2 AND (dateJour BETWEEN ?3 AND ?4)")
	public List<PlanningExamen> findPlanningOfSpecialite(Long idExamen, Long idSpecialite, LocalDate dateDebutSemaine, LocalDate dateFinSemaine);
	
	@Query("FROM PlanningExamen WHERE idExamen = ?1")
	public List<PlanningExamen> findPlanningOfExamen(Long idExamen);
	
	@Query("FROM PlanningExamen WHERE dateJour = ?1 AND horaireDebut = ?2 AND horaireFin = ?3 AND (idExamen <> ?4 AND idMatiere <> ?5 AND idSpecialite <> ?6)")
	public List<PlanningExamen> findPlanningWithDate(LocalDate dateJour, String horaireDebut, String horaireFin,Long idExamen, Long idMatiere, Long idSpecialite);

	@Query("FROM PlanningExamen WHERE idExamen = ?1 AND idSpecialite = ?2 AND idMatiere <> ?3 AND dateJour = ?4 AND horaireDebut = ?5 AND horaireFin = ?6")
	public PlanningExamen findIfDateFree(Long idExamen, Long idSpecialite, Long idMatiere,LocalDate dateJour, String horaireDebut, String horaireFin);
}
