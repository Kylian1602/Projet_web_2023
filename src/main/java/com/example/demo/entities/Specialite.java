package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Specialite {

	@Id@GeneratedValue
	private Long idSpecialite;
	private String nom;
	private int nbEtudiant;
	
	public Specialite() {
		
	}
	
	public Specialite(String nom, int nbEtudiant) {
		this.nom = nom;
		this.nbEtudiant = nbEtudiant;
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

	public int getNbEtudiant() {
		return nbEtudiant;
	}

	public void setNbEtudiant(int nbEtudiant) {
		this.nbEtudiant = nbEtudiant;
	}
}
