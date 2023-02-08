package com.example.demo.entities;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Matiere {

	@Id@GeneratedValue
	private Long idMatiere;
	private Long idSpecialite;
	private String nom;
	private LocalTime tempsExamen;
	
	public Matiere() {
		
	}
	
	public Matiere(Long idSpecialite, String nom, LocalTime tempsExamen) {
		this.idSpecialite = idSpecialite;
		this.nom = nom;
		this.tempsExamen = tempsExamen;
	}

	public Long getIdMatiere() {
		return idMatiere;
	}

	public void setIdMatiere(Long idMatiere) {
		this.idMatiere = idMatiere;
	}

	public Long getIdSpecialite() {
		return idSpecialite;
	}

	public void setIdSpecialite(Long idSpecialite) {
		this.idSpecialite = idSpecialite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public LocalTime getTempsExamen() {
		return tempsExamen;
	}

	public void setTempsExamen(LocalTime tempsExamen) {
		this.tempsExamen = tempsExamen;
	}
}
