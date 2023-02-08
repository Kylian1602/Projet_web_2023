package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MatiereExamenMutualise {

	@Id @GeneratedValue
	private Long id;
	private String nom;
	private Long idExamen;
	private String listIdSpecialite;
	
	public String getListIdSpecialite() {
		return listIdSpecialite;
	}
	public void setListIdSpecialite(String listIdSpecialite) {
		this.listIdSpecialite = listIdSpecialite;
	}
	public MatiereExamenMutualise() {
		
	}
	public MatiereExamenMutualise(String nom, Long idExamen, String listIdSpecialite) {
		this.nom = nom;
		this.idExamen = idExamen;
		this.listIdSpecialite = listIdSpecialite;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Long getIdExamen() {
		return idExamen;
	}
	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}
	public String getIdSpecialite() {
		return listIdSpecialite;
	}
	public void setIdSpecialite(String listIdSpecialite) {
		this.listIdSpecialite = listIdSpecialite;
	}
	
	
}
