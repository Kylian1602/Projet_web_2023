package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SpecialiteExamen {
	@Id @GeneratedValue
	private Long id;
	private Long idSpecialite;
	private Long idExamen;
	private boolean boolExclu;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isBoolExclu() {
		return boolExclu;
	}

	public void setBoolExclu(boolean boolExclu) {
		this.boolExclu = boolExclu;
	}

	public SpecialiteExamen() {
		
	}

	public Long getIdSpecialite() {
		return idSpecialite;
	}

	public void setIdSpecialite(Long idSpecialite) {
		this.idSpecialite = idSpecialite;
	}

	public Long getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}
}
