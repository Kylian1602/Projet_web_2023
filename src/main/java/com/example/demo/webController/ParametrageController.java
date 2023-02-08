package com.example.demo.webController;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dao.EleveRepository;
import com.example.demo.dao.MatiereRepository;
import com.example.demo.dao.PersonneRepository;
import com.example.demo.dao.ProfesseurRepository;
import com.example.demo.dao.SalleRepository;
import com.example.demo.dao.SpecialiteRepository;
import com.example.demo.entities.Eleve;
import com.example.demo.entities.Matiere;
import com.example.demo.entities.Personne;
import com.example.demo.entities.Professeur;
import com.example.demo.entities.Salle;
import com.example.demo.entities.Specialite;

@Controller
@RequestMapping("/Parametrage")
public class ParametrageController {
	
	@Autowired
	SpecialiteRepository specialiteRepository;
	
	@Autowired
	SalleRepository salleRepository;
	
	@Autowired
	MatiereRepository matiereRepository;
	
	@Autowired
	PersonneRepository personneRepository;
	
	@Autowired
	EleveRepository eleveRepository;
	
	@Autowired
	ProfesseurRepository professeurRepository;
	
	private List<Personne> liste_personne;
	
	@RequestMapping("/index")
	public String indexParametrage() {
		return "Parametrage/indexParametrage";
	}
	
	@RequestMapping("/tableauCompte")
	public String tableauCompte(Model model) {
		model.addAttribute("personnes", liste_personne);
		return "Parametrage/tableauCompte";
	}
	
	@RequestMapping("/indexMatiere")
	public String indexMatiere(Model model) {
		List<Matiere> List_matiere = matiereRepository.findAll();
		model.addAttribute("matieres",List_matiere);
		List<Specialite> List_specialite = specialiteRepository.findAll();
		model.addAttribute("specialites",List_specialite);
		return "Parametrage/indexMatiere";
	}
	
	@RequestMapping("/indexSpecialite")
	public String indexSpecialite(Model model) {
		List<Specialite> List_specialite = specialiteRepository.findAll();
		model.addAttribute("specialites",List_specialite);
		return "Parametrage/indexSpecialite";
	}
	
	@RequestMapping("/indexSalle")
	public String indexSalle(Model model) {
		List<Salle> List_salle = salleRepository.findAll();
		model.addAttribute("salles",List_salle);
		return "Parametrage/indexSalle";
	}
	
	@RequestMapping("/indexCompte")
	public String indexCompte(Model model) {
		List<Specialite> List_specialite = specialiteRepository.findAll();
		List<Matiere> List_matiere = matiereRepository.findAll();
		model.addAttribute("specialites",List_specialite);
		model.addAttribute("matieres",List_matiere);
		model.addAttribute("personnes", liste_personne);
		return "Parametrage/indexCompte";
	}
	
	@ResponseBody
	@RequestMapping("/addSpecialite")
	public String addSpecialite(
			@RequestParam(name="name") String name,
			@RequestParam(name="nbEtudiant") int nbEtudiant
			) {
		String msg_err = "";
		if(name == "" || nbEtudiant == 0) {
			msg_err = "Un des champs est vide";
		}
		else {
			Specialite verif = specialiteRepository.findSpecialite(name, nbEtudiant);
			if(verif == null) {
				Specialite specialite = new Specialite(name,nbEtudiant);
				specialiteRepository.save(specialite);
			}
			else {
				msg_err = "La spécialité existe déjà";
			}
			
		}
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/addSalle")
	public String addSalle(
			@RequestParam(name="numero") String numero,
			@RequestParam(name="nbPlace") int nbPlace
			) {
		String msg_err = "";
		if(numero == "" || nbPlace == 0) {
			msg_err = "Un des champs est vide";
		}
		else {
			Salle verif = salleRepository.findSalle(numero, nbPlace);
			if(verif == null) {
				Salle salle = new Salle(numero,nbPlace);
				salleRepository.save(salle);
			}
			else {
				msg_err = "La salle existe déjà";
			}
		}
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/addMatiere")
	public String addMatiere(
			@RequestParam(name="idSpecialite") Long idSpecialite,
			@RequestParam(name="nom") String nom,
			@RequestParam(name="tempsExamen") String tempsExamen
			) {
		String msg_err = "";
		if(idSpecialite == 0 || nom == "" || tempsExamen == null) {
			msg_err = "Un des champs est vide";
		}
		else {
			try {
				LocalTime tempsExamenConvert = LocalTime.parse(tempsExamen);
				Matiere verif = matiereRepository.findMatiere(idSpecialite, nom);
				if(verif == null) {
					Matiere matiere = new Matiere(idSpecialite,nom,tempsExamenConvert);
					matiereRepository.save(matiere);
				}
				else {
					msg_err = "La salle existe déjà";
				}
			}catch (Exception e) {
				msg_err = "erreur sur le format de la date";
			}
		}
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/addCompte")
	public String addCompte(
			@RequestParam(name="name") String name,
			@RequestParam(name="surname") String surname,
			@RequestParam(name="username") String username,
			@RequestParam(name="password") String password,
			@RequestParam(name="type") String type,
			@RequestParam(name="idMatiere") Long idMatiere,
			@RequestParam(name="idSpecialite") Long idSpecialite
			) {
		if(name == "" || username == "" || password == "" || type == "0") {
			return "Un des champs est vide";
		}
		if(type == "STU" && idSpecialite == 0) {
			return "Il faut choisir une specialite";
		}
		if(type == "PROF" && idMatiere == 0) {
			return "Il faut choisir une matiere";
		}
		else {
			Personne personne = new Personne(name,surname,username,password,type);
			personneRepository.save(personne);
			Personne personneForId;
			switch(type) {
			case "STU" :
				personneForId = personneRepository.findUser(username, password);
				if(personneForId == null) {
					return "probleme enregistrement de l'eleve";
				}
				else {
					Eleve eleve = new Eleve(personneForId.getIdPersonne(), idSpecialite);
					eleveRepository.save(eleve);
				}
				break;
			case "PROF" :
				personneForId = personneRepository.findUser(username, password);
				if(personneForId == null) {
					return "probleme enregistrement du professeur";
				}
				else {
					Professeur professeur = new Professeur(personneForId.getIdPersonne(), idMatiere);
					professeurRepository.save(professeur);
				}
				break;
			}
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/modifierSpecialite")
	public String modifierSpecialite(
			@RequestParam(name="idSpecialiteModif") Long idSpecialite,
			@RequestParam(name="name") String name,
			@RequestParam(name="nbEtudiant") String nbEtudiant
			) {
		Specialite specialite = specialiteRepository.getReferenceById(idSpecialite);
		if(name == "") {
			return "Le nom n'est pas remplit";
		}
		specialite.setNom(name);
		try {
			int nbEtudiantConvert = Integer.parseInt(nbEtudiant);
			specialite.setNbEtudiant(nbEtudiantConvert);
		}catch (Exception e) {
			return "Le nombre d'etudiant est invalide";
		}
		
		specialiteRepository.save(specialite);
		return "La spécialité a été modifier";
	}
	
	@ResponseBody
	@RequestMapping("/modifierSalle")
	public String modifierSalle(
			@RequestParam(name="idSalleModif") Long idSalle,
			@RequestParam(name="numero") String numero,
			@RequestParam(name="nbPlace") String nbPlace
			) {
		Salle salle = salleRepository.getReferenceById(idSalle);
		if(!numero.equals("")) {
			salle.setNumero(numero);
		}
		if(!nbPlace.equals("")) {
			try {
				int nbPlaceConvert = Integer.parseInt(nbPlace);
				salle.setNbPlace(nbPlaceConvert);
			}catch (Exception e) {
				return "Le nombre de place est invalide";
			}
		}
		salleRepository.save(salle);
		return "La salle a été modifier";
	}
	
	@ResponseBody
	@RequestMapping("/modifierMatiere")
	public String modifierMatiere(
			@RequestParam(name="idMatiereModif") Long idMatiere,
			@RequestParam(name="nom") String nom,
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="tempsExamen") String tempsExamen
			) {
		Matiere matiere = matiereRepository.getReferenceById(idMatiere);
		if(!nom.equals("")) {
			matiere.setNom(nom);
		}
		if(!idSpecialite.equals("")) {
			Long idSpecialiteConvert = Long.parseLong(idSpecialite);
			matiere.setIdSpecialite(idSpecialiteConvert);
		}
		if(!tempsExamen.equals("")) {
			try {
				LocalTime tempsExamenConvert = LocalTime.parse(tempsExamen);
				matiere.setTempsExamen(tempsExamenConvert);
			}catch (Exception e) {
				return "temps invalide";
			}
		}
		matiereRepository.save(matiere);
		return "La matiere a été modifier";
	}
	
	@ResponseBody
	@RequestMapping("/modifierCompte")
	public String modifierCompte(
			@RequestParam(name="id") Long id,
			@RequestParam(name="name") String name,
			@RequestParam(name="surname") String surname,
			@RequestParam(name="username") String username,
			@RequestParam(name="password") String password,
			@RequestParam(name="typeModif") String type,
			@RequestParam(name="idMatiereModif") Long idMatiere,
			@RequestParam(name="idSpecialiteModif") Long idSpecialite
			) {
		Personne pers = personneRepository.findUser(id);
		if(personneRepository.findUser(username) != null && !username.equals(pers.getUsername())) {
			return "username deja pris";
		}
		Personne personne = personneRepository.getReferenceById(id);
		if(!name.equals("")) {
			personne.setName(name);
		}
		if(!surname.equals("")) {
			personne.setSurname(surname);
		}
		if(!username.equals("")) {
			personne.setUsername(username);
		}
		if(!password.equals("")) {
			personne.setPassword(password);
		}
		if(!type.equals("0")) {
			personne.setType(type);
		}
		
		personneRepository.save(personne);
		if(type.equals("STU")) {
			Eleve eleve = eleveRepository.getReferenceById(id);
			eleve.setIdSpecialite(idSpecialite);
			eleveRepository.save(eleve);
		}
		if(type.equals("PROF")) {
			Professeur p = professeurRepository.findUser(id);
			Professeur professeur = professeurRepository.getReferenceById(p.getId());
			if(professeur != null) {
				professeur.setIdMatiere(idMatiere);
				professeurRepository.save(professeur);
			}
			else {
				return "exception detecter";
			}	
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/findCompte")
	public String findCompte(
			Model model,
			@RequestParam(name="name") String name,
			@RequestParam(name="username") String username,
			@RequestParam(name="typeFind") String typeFind
			/*@RequestParam(name="idMatiereFind") Long idMatiereFind,
			@RequestParam(name="idSpecialiteFind") Long idSpecialiteFind*/
			) {
		List<Personne> list_personne;
		if(!typeFind.equals("0")) {
			list_personne = personneRepository.findUsersWithType(name,username,typeFind);
		}
		else {
			list_personne = personneRepository.findUsersWithoutType(name,username);
		}
		
		this.liste_personne = list_personne;
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/deleteSpecialite")
	public String deleteSpecialite(
			@RequestParam(name="idSpecialiteModif") Long idSpecialite
			) {
		String msg_err = "";
		if(idSpecialite == 0) {
			msg_err = "Selectionner une spécialité valide";
		}
		else {
			specialiteRepository.deleteById(idSpecialite);
			msg_err = "La specialité à été supprimer";
		}
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/deleteSalle")
	public String deleteSalle(
			@RequestParam(name="idSalleModif") Long idSalle
			) {
		String msg_err = "";
		if(idSalle == 0) {
			msg_err = "Selectionner une salle valide";
		}
		else {
			salleRepository.deleteById(idSalle);
			msg_err = "La salle à été supprimer";
		}
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/deleteMatiere")
	public String deleteMatiere(
			@RequestParam(name="idMatiereModif") Long idMatiere
			) {
		String msg_err = "";
		if(idMatiere == 0) {
			msg_err = "Selectionner une matiere valide";
		}
		else {
			matiereRepository.deleteById(idMatiere);
			msg_err = "La matiere à été supprimer";
		}
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/getInformationCompte")
	public String getInformationCompte(
			Model model,
			@RequestParam(name="id") Long id
			) {
		Personne personne = personneRepository.findUser(id);
		String matiere = "";
		String specialite = "";
		if(personne.getType().equals("STU")) {
			Eleve eleve = eleveRepository.findUser(id);
			specialite = "" + eleve.getIdSpecialite() + "";
		}
		if(personne.getType().equals("PROF")) {
			Professeur professeur = professeurRepository.findUser(id);
			matiere = "" + professeur.getIdMatiere() + "";
		}
		return personne.getName() +  ";" +personne.getSurname() +  ";" + personne.getUsername() +  ";" + personne.getPassword() + ";" + personne.getType() + ";" + specialite + ";" + matiere + ";" + personne.getIdPersonne();
	}
	
}
