package com.example.demo.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class PlanningExamen {

	@Id @GeneratedValue
	private Long id;
	private Long idExamen;
	private Long idSpecialite;
	private Long idMatiere;
	private Long idSalle;
	public Long getIdSalle() {
		return idSalle;
	}
	public void setIdSalle(Long idSalle) {
		this.idSalle = idSalle;
	}
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
	public Long getIdSpecialite() {
		return idSpecialite;
	}
	public void setIdSpecialite(Long idSpecialite) {
		this.idSpecialite = idSpecialite;
	}
	public Long getIdMatiere() {
		return idMatiere;
	}
	public void setIdMatiere(Long idMatiere) {
		this.idMatiere = idMatiere;
	}
	public Long getIdSurveillant() {
		return idSurveillant;
	}
	public void setIdSurveillant(Long idSurveillant) {
		this.idSurveillant = idSurveillant;
	}
	public LocalDate getDateJour() {
		return dateJour;
	}
	public void setDateJour(LocalDate dateJour) {
		this.dateJour = dateJour;
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
	private Long idSurveillant;
	private LocalDate dateJour;
	private String horaireDebut;
	private String horaireFin;
}
