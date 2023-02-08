package com.example.demo.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SalleHoraire {
	@Id @GeneratedValue
	private Long id;
	private Long idSalle;
	private Long idExamen;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}

	private boolean boolDisponible;
	private Date dateDebut;
	private Date dateFin;
	
	public SalleHoraire() {
		
	}

	public Long getIdSalle() {
		return idSalle;
	}

	public void setIdSalle(Long idSalle) {
		this.idSalle = idSalle;
	}

	public boolean isBoolDisponible() {
		return boolDisponible;
	}

	public void setBoolDisponible(boolean boolDisponible) {
		this.boolDisponible = boolDisponible;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}
}
