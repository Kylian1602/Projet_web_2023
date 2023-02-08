package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Eleve {

	@Id
	private Long idPersonne;
	private Long idSpecialite;
	
	public Eleve() {
		super();
	}
	
	public Eleve(Long idPersonne, Long idSpecialite) {
		this.idPersonne = idPersonne;
		this.idSpecialite = idSpecialite;
	}
	
	public Long getIdPersonne() {
		return idPersonne;
	}
	public void setIdPersonne(Long idPersonne) {
		this.idPersonne = idPersonne;
	}
	public Long getIdSpecialite() {
		return idSpecialite;
	}
	public void setIdSpecialite(Long idSpecialite) {
		this.idSpecialite = idSpecialite;
	}
}
