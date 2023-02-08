package com.example.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Personne;

@Repository
public interface PersonneRepository extends JpaRepository<Personne,Long>{
	
	@Query("FROM Personne WHERE idPersonne = ?1")
	public Personne findUser(Long id);
	
	@Query("FROM Personne WHERE username = ?1")
	public Personne findUser(String username);
	
	@Query("FROM Personne WHERE username = ?1 AND password = ?2")
	public Personne findUser(String username, String password);
	
	@Query("FROM Personne WHERE name LIKE %?1% AND username LIKE %?2% AND type = ?3")
	public List<Personne> findUsersWithType(String name, String username, String type);
	
	@Query("FROM Personne WHERE name LIKE %?1% AND username LIKE %?2% ")
	public List<Personne> findUsersWithoutType(String name, String username);
	
	@Query("Select p FROM Personne p "
			+ "LEFT JOIN Professeur f ON "
			+ "p.idPersonne = f.idPersonne "
			+ "AND f.idMatiere = '12' "
			+ "WHERE p.type = 'PROF' OR p.type = 'SURV'")
	public List<Personne> findSurveillant(Long idMatiere);
}