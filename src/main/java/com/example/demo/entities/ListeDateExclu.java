package com.example.demo.entities;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ListeDateExclu {
	@Id @GeneratedValue
	private Long id;
	private Long idSpecialite;
	private Long idExamen;
	private LocalDate dateJour;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDateJour() {
		return dateJour;
	}

	public void setDateJour(LocalDate dateJour) {
		this.dateJour = dateJour;
	}

	private String horaireDebut;
	private String horaireFin;
	
	public ListeDateExclu() {
		
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

	public String getHoraireDebut() {
		return horaireDebut;
	}

	public void setHoraireDebut(String horaireDebut) {
		this.horaireDebut = horaireDebut;
	}

	public String getHoraireFin() {
		return horaireFin;
	}

	public void setHoraireFin(String horaireFin) {
		this.horaireFin = horaireFin;
	}

}
