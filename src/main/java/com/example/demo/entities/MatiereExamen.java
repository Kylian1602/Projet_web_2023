package com.example.demo.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MatiereExamen {
	@Id @GeneratedValue
	private Long id;
	private Long idMatiere;
	private Long idExamen;
	private String nom;
	private Long idSurveillant;
	private Long idSalle;
	public Long getIdSalle() {
		return idSalle;
	}

	public void setIdSalle(Long idSalle) {
		this.idSalle = idSalle;
	}

	public Long getIdSurveillant() {
		return idSurveillant;
	}

	public void setIdSurveillant(Long idSurveillant) {
		this.idSurveillant = idSurveillant;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Long idSpecialite;
	private boolean boolMutualise;
	private boolean boolExclu;

	public boolean isBoolExclu() {
		return boolExclu;
	}

	public void setBoolExclu(boolean boolExclu) {
		this.boolExclu = boolExclu;
	}

	private LocalDate dateDebut;
	private String heureDebut;
	private String heureFin;
	
	public MatiereExamen() {
		
	}

	public Long getIdMatiere() {
		return idMatiere;
	}

	public void setIdMatiere(Long idMatiere) {
		this.idMatiere = idMatiere;
	}

	public Long getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}

	public Long getIdSpecialite() {
		return idSpecialite;
	}

	public void setIdSpecialite(Long idSpecialite) {
		this.idSpecialite = idSpecialite;
	}

	public boolean isBoolMutualise() {
		return boolMutualise;
	}

	public void setBoolMutualise(boolean boolMutualise) {
		this.boolMutualise = boolMutualise;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public String getHeureDebut() {
		return heureDebut;
	}

	public void setHeureDebut(String heureDebut) {
		this.heureDebut = heureDebut;
	}

	public String getHeureFin() {
		return heureFin;
	}

	public void setHeureFin(String heureFin) {
		this.heureFin = heureFin;
	}


}
