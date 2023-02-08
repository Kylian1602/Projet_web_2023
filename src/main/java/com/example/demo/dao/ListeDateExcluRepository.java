package com.example.demo.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.ListeDateExclu;

public interface ListeDateExcluRepository extends JpaRepository<ListeDateExclu,Long>{

	@Query("FROM ListeDateExclu WHERE idSpecialite = ?1 AND idExamen = ?2")
	public List<ListeDateExclu> findAllDateExclue(Long idSpecialite, long idExamen);
	
	@Query("FROM ListeDateExclu WHERE idSpecialite = ?1 AND idExamen = ?2 AND (dateJour BETWEEN ?3 AND ?4)")
	public List<ListeDateExclu> findAllDateExclueForDate(Long idSpecialite, long idExamen, LocalDate dateDebutSemaine, LocalDate dateFinSemaine);
	
	@Query("FROM ListeDateExclu WHERE idSpecialite = ?1 AND idExamen = ?2 AND dateJour = ?3 AND horaireDebut = ?4 AND horaireFin = ?5")
	public ListeDateExclu AlreadyExist(Long idSpecialite, Long idExamen, LocalDate dateJour,String horaireDebut, String horaireFin);
}
