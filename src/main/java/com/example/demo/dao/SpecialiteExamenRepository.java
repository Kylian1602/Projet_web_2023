package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entities.SpecialiteExamen;

public interface SpecialiteExamenRepository extends JpaRepository<SpecialiteExamen,Long>{

	@Query("FROM SpecialiteExamen WHERE idSpecialite = ?1 AND idExamen = ?2")
	public SpecialiteExamen findSpecialiteForExamen(Long idSpecialite, Long idExamen);
	
	@Query("FROM SpecialiteExamen WHERE idExamen = ?1 AND boolExclu = ?2")
	public List<SpecialiteExamen> findAllSpecialiteForExamen(Long idExamen,boolean boolExclu);
	
	@Query("FROM SpecialiteExamen WHERE idSpecialite = ?1 AND boolExclu = ?2")
	public List<SpecialiteExamen> findAllExamenForSpecialite(Long idSpecialite, boolean boolExclu);
}
