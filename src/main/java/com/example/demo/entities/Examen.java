package com.example.demo.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Examen {

	@Id@GeneratedValue
	private Long idExamen;
	private String name;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	private int etatModification;
	
	public Examen() {
		super();
	}
	
	public int getEtatModification() {
		return etatModification;
	}

	public void setEtatModification(int etatModification) {
		this.etatModification = etatModification;
	}

	public Examen(String name, LocalDate dateDebut, LocalDate dateFin, int etatModification) {
		super();
		this.name = name;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
		this.etatModification = etatModification;
	}

	public Long getIdExamen() {
		return idExamen;
	}

	public void setIdExamen(Long idExamen) {
		this.idExamen = idExamen;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
	}

	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}
}
