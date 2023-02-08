package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.MatiereExamen;
import com.example.demo.entities.Personne;

public interface MatiereExamenRepository extends JpaRepository<MatiereExamen,Long>{

	@Query("FROM MatiereExamen WHERE idMatiere = ?1 AND idSpecialite = ?2 AND idExamen = ?3")
	public MatiereExamen findMatiereForExamen(Long idMatiere, Long idSpecialite, Long idExamen);
	
	@Query("FROM MatiereExamen WHERE nom = ?1 AND idSpecialite = ?2 AND idExamen = ?3")
	public MatiereExamen findMatiereForExamen(String nom, Long idSpecialite, Long idExamen);
	
	@Query("FROM MatiereExamen WHERE idSpecialite = ?1 AND idExamen = ?2")
	public List<MatiereExamen> findAllMatiereForExamen(Long idSpecialite, Long idExamen);
	
	@Query("SELECT nom FROM MatiereExamen GROUP BY nom,idExamen HAVING COUNT(*) > 1 AND idExamen = ?1")
	public List<String> findSharedMatiere(Long idExamen);
	
	@Query("SELECT DISTINCT idSpecialite FROM MatiereExamen WHERE nom = ?1")
	public List<Long> findSharedSpecialite(String nom);
	
	@Query("FROM MatiereExamen WHERE idExamen = ?1 AND nom = ?2 AND boolMutualise = ?3")
	public List<MatiereExamen> findListSharedMatiere(Long idExamen,String nom,boolean boolMutualise);
	
	@Query("FROM MatiereExamen WHERE idExamen = ?1")
	public List<MatiereExamen> findAllMatiereOfExamen(Long idExamen);
}
