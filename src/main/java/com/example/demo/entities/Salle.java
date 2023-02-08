package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Salle {

	@Id@GeneratedValue
	private Long idSalle;
	private String numero;
	private int nbPlace;
	
	public Salle() {
		
	}
	
	public Salle(String numero, int nbPlace) {
		this.numero = numero;
		this.nbPlace = nbPlace;
	}

	public Long getIdSalle() {
		return idSalle;
	}

	public void setIdSalle(Long idSalle) {
		this.idSalle = idSalle;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public int getNbPlace() {
		return nbPlace;
	}

	public void setNbPlace(int nbPlace) {
		this.nbPlace = nbPlace;
	}
}
