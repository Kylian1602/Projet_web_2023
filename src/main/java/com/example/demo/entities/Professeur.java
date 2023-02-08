package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Professeur {
	@Id @GeneratedValue
	private Long id;
	private Long idPersonne;
	private Long idMatiere;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Professeur() {
		
	}
	
	public Professeur(Long idPersonne,Long idMatiere) {
		this.idPersonne = idPersonne;
		this.idMatiere = idMatiere; 
	}

	public Long getIdPersonne() {
		return idPersonne;
	}

	public void setIdPersonne(Long idPersonne) {
		this.idPersonne = idPersonne;
	}

	public Long getIdMatiere() {
		return idMatiere;
	}

	public void setIdMatiere(Long idMatiere) {
		this.idMatiere = idMatiere;
	}
	
}
