package com.example.demo.webController;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.dao.ExamenRepository;
import com.example.demo.dao.ListeDateExcluRepository;
import com.example.demo.dao.MatiereExamenMutualiseRepository;
import com.example.demo.dao.MatiereExamenRepository;
import com.example.demo.dao.MatiereRepository;
import com.example.demo.dao.PersonneRepository;
import com.example.demo.dao.PlanningExamenRepository;
import com.example.demo.dao.SalleHoraireRepository;
import com.example.demo.dao.SalleRepository;
import com.example.demo.dao.SpecialiteExamenRepository;
import com.example.demo.dao.SpecialiteRepository;
import com.example.demo.entities.Eleve;
import com.example.demo.entities.Examen;
import com.example.demo.entities.ListeDateExclu;
import com.example.demo.entities.Matiere;
import com.example.demo.entities.MatiereExamen;
import com.example.demo.entities.MatiereExamenMutualise;
import com.example.demo.entities.Personne;
import com.example.demo.entities.PlanningExamen;
import com.example.demo.entities.Salle;
import com.example.demo.entities.SalleHoraire;
import com.example.demo.entities.Specialite;
import com.example.demo.entities.SpecialiteExamen;

@Controller
@RequestMapping("/Examen")
public class ExamenController {

	@Autowired
	ExamenRepository examenRepository;
	
	@Autowired
	SpecialiteRepository specialiteRepository;
	
	@Autowired
	MatiereRepository matiereRepository;
	
	@Autowired
	MatiereExamenRepository matiereExamenRepository;
	
	@Autowired
	SpecialiteExamenRepository specialiteExamenRepository;
	
	@Autowired
	SalleRepository salleRepository;
	
	@Autowired
	SalleHoraireRepository salleHoraireRepository;
	
	@Autowired
	MatiereExamenMutualiseRepository matiereExamenMutualiseRepository;
	
	@Autowired
	ListeDateExcluRepository listeDateExcluRepository;
	
	@Autowired
	PlanningExamenRepository planningExamenRepository;
	
	@Autowired
	PersonneRepository personneRepository;
	
	private List<Long> liste_idSpecialite;
	
	
	@RequestMapping("/indexExamen")
	public String indexExamen() {
		return "Examen/indexExamen";
	}
	
	@RequestMapping("/creationExamenDate")
	public String creationExamen() {
		return "Examen/creationExamenDate";
	}

	@RequestMapping("/modificationExamen")
	public String modificationExamen(Model model) {
		List<Examen> List_examen = examenRepository.findAll();
		ArrayList<Examen> List_toReturn = new ArrayList<>();
		for(Examen l : List_examen) {
			if(l.getEtatModification() != 9) {
				List_toReturn.add(l);
			}
		}
		model.addAttribute("examens",List_toReturn);
		return "Examen/modificationExamen";
	}
	
	@RequestMapping("/creationExamenSpecialite")
	public String creationExamenSpecialite(Model model, HttpSession session) {
		List<Specialite> List_specialite = specialiteRepository.findAll();
		model.addAttribute("specialites",List_specialite);
		return "Examen/creationExamenSpecialite";
	}
	
	@RequestMapping("/creationExamenMatiere")
	public String creationExamenMatiere(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		List<SpecialiteExamen> list_specialite = specialiteExamenRepository.findAllSpecialiteForExamen(examen.getIdExamen(),false);
		ArrayList<Matiere> List_matiere = new ArrayList<>();
		String specialiteInExamen = "";
		for(SpecialiteExamen specialite : list_specialite) {
			specialiteInExamen += "" + specialite.getIdSpecialite() + "" + ";";
			List<Matiere> list_matiere = matiereRepository.findAllMatiereOfSpecialite(specialite.getIdSpecialite());
			for(Matiere matiere : list_matiere) {
				List_matiere.add(matiere);
			}
		}
		model.addAttribute("specialiteInExamen",specialiteInExamen);
		model.addAttribute("matieres",List_matiere);
		return "Examen/creationExamenMatiere";
	}
	
	@RequestMapping("/creationExamenSalle")
	public String creationExamenSalle(Model model) {
		List<Salle> List_salle = salleRepository.findAll();
		model.addAttribute("salles",List_salle);
		return "Examen/creationExamenSalle";
	}
	
	@ResponseBody
	@RequestMapping("/changeForSalle")
	public String changeForSalle(HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		if(examen.getEtatModification() == 4) {
			return "false";
		}
		else {
			return "";
		}
	}
	
	@RequestMapping("/creationExamenMatiereMutualise")
	public String creationExamenMatiereMutualise(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		List<String> List_nomMatiere = matiereExamenRepository.findSharedMatiere(examen.getIdExamen());
		if(List_nomMatiere == null || List_nomMatiere.size() == 0) {
			examen.setEtatModification(4);
			Examen examenToSave = examenRepository.getReferenceById(examen.getIdExamen());
			examenToSave.setEtatModification(4);
			examenRepository.save(examenToSave);
			session.setAttribute("Examen", examen);
		}
		model.addAttribute("noms",List_nomMatiere);
		return "Examen/creationExamenMatiereMutualise";
	}
	
	@RequestMapping("/changeSessionForMutualise")
	public String changeSessionForMutualise(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		Examen examenBDD = examenRepository.getReferenceById(examen.getIdExamen());
		examen.setEtatModification(4);
		examenBDD.setEtatModification(4);
		examenRepository.save(examenBDD);
		session.setAttribute("Examen", examen);
		return "Examen/creationExamenSalle";
	}

	//retourne la page avec la date du Lundi et du dimanche de la semaine et les specialite
	@RequestMapping("/creationExamenHoraire")
	public String creationExameneHoraire(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		List<SpecialiteExamen> List_specialiteExamen = specialiteExamenRepository.findAllSpecialiteForExamen(examen.getIdExamen(),false);
		ArrayList<Specialite> List_specialite = new ArrayList<>();
		for(SpecialiteExamen p : List_specialiteExamen) {
			Specialite specialite = specialiteRepository.findSpecialite(p.getIdSpecialite());
			List_specialite.add(specialite);
		}
		model.addAttribute("specialites",List_specialite);
		
		LocalDate dateDebut = examen.getDateDebut();
		DayOfWeek dateDebutJour = dateDebut.getDayOfWeek();
		Long numberForAffichage = (long) 0;
		String dateDebutSemaineAffichage = "";
		String dateFinSemaineAffichage = "";
		switch(dateDebutJour) {
		case MONDAY:
			numberForAffichage = (long) 0;
			break;
		case TUESDAY :
			numberForAffichage = (long) 1;
			break;
		case WEDNESDAY :
			numberForAffichage = (long) 2;
			break;
		case THURSDAY:
			numberForAffichage = (long) 3;
			break;
		case FRIDAY :
			numberForAffichage = (long) 4;
			break;
		case SATURDAY :
			numberForAffichage = (long) 5;
			break;
		case SUNDAY :
			numberForAffichage = (long) 6;
			break;
		}
		dateDebutSemaineAffichage = "" + dateDebut.minusDays(numberForAffichage).getDayOfMonth() + "/" + "" +dateDebut.minusDays(numberForAffichage).getMonthValue() + "";
		dateFinSemaineAffichage = "" + dateDebut.minusDays(numberForAffichage).plusDays(6).getDayOfMonth() + "/" + "" +dateDebut.minusDays(numberForAffichage).plusDays(6).getMonthValue() + "";
		
		model.addAttribute("dateDebutSemaineAffichage",dateDebutSemaineAffichage);
		model.addAttribute("dateFinSemaineAffichage",dateFinSemaineAffichage);
		return "Examen/creationExamenHoraire";
	}
	
	@ResponseBody
	@RequestMapping("/validationExamenHoraireMatiere")
	public String validationExamenHoraireMatiere(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		List<SpecialiteExamen> List_specialiteExamen = specialiteExamenRepository.findAllSpecialiteForExamen(examen.getIdExamen(),false);
		for(SpecialiteExamen p : List_specialiteExamen) {
			List<MatiereExamen> matieres = matiereExamenRepository.findAllMatiereForExamen(p.getIdSpecialite(),examen.getIdExamen());
			for(MatiereExamen m : matieres) {
				if(m.getDateDebut() == null || m.getDateDebut().equals("") || m.getHeureDebut().equals("") || m.getHeureDebut() == null || m.getHeureFin().equals("") || m.getHeureFin() == null) {
					Matiere matiere = matiereRepository.getReferenceById(m.getIdMatiere());
					Specialite specialite = specialiteRepository.getReferenceById(p.getIdSpecialite());
					return "La matiere " + matiere.getNom() + " pour la spécialite " + specialite.getNom() + " n'a pas été placer sur le planning";
				}
			}
		}
		examen.setEtatModification(7);
		examenRepository.save(examen);
		return "";
	}
	
	@RequestMapping("/creationExamenHoraireMatiere")
	public String creationExamenHoraireMatiere(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		examen.setEtatModification(6);
		examenRepository.save(examen);
		List<SpecialiteExamen> List_specialiteExamen = specialiteExamenRepository.findAllSpecialiteForExamen(examen.getIdExamen(),false);
		ArrayList<Specialite> List_specialite = new ArrayList<>();
		for(SpecialiteExamen p : List_specialiteExamen) {
			Specialite specialite = specialiteRepository.findSpecialite(p.getIdSpecialite());
			List_specialite.add(specialite);
		}
		model.addAttribute("specialites",List_specialite);
		
		LocalDate dateDebut = examen.getDateDebut();
		DayOfWeek dateDebutJour = dateDebut.getDayOfWeek();
		Long numberForAffichage = (long) 0;
		String dateDebutSemaineAffichage = "";
		String dateFinSemaineAffichage = "";
		switch(dateDebutJour) {
		case MONDAY:
			numberForAffichage = (long) 0;
			break;
		case TUESDAY :
			numberForAffichage = (long) 1;
			break;
		case WEDNESDAY :
			numberForAffichage = (long) 2;
			break;
		case THURSDAY:
			numberForAffichage = (long) 3;
			break;
		case FRIDAY :
			numberForAffichage = (long) 4;
			break;
		case SATURDAY :
			numberForAffichage = (long) 5;
			break;
		case SUNDAY :
			numberForAffichage = (long) 6;
			break;
		}
		dateDebutSemaineAffichage = "" + dateDebut.minusDays(numberForAffichage).getDayOfMonth() + "/" + "" +dateDebut.minusDays(numberForAffichage).getMonthValue() + "";
		dateFinSemaineAffichage = "" + dateDebut.minusDays(numberForAffichage).plusDays(6).getDayOfMonth() + "/" + "" +dateDebut.minusDays(numberForAffichage).plusDays(6).getMonthValue() + "";
		
		model.addAttribute("dateDebutSemaineAffichage",dateDebutSemaineAffichage);
		model.addAttribute("dateFinSemaineAffichage",dateFinSemaineAffichage);
		return "Examen/planningMatiere/creationExamenHoraireMatiere";
	}
	
	@RequestMapping("/creationExamenSalleForMatiere")
	public String creationExamenSalleForMatiere(HttpSession session, Model model) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "recharger la session";
		}
		List<SpecialiteExamen> specialiteExamen = specialiteExamenRepository.findAllSpecialiteForExamen(examen.getIdExamen(),false);
		ArrayList<Specialite> liste_specialite = new ArrayList<>();
		for(SpecialiteExamen p : specialiteExamen) {
			Specialite spe  = specialiteRepository.getReferenceById(p.getIdSpecialite());
			if(spe != null) {
				liste_specialite.add(spe);
			}
		}
		model.addAttribute("idExamen", examen.getIdExamen());
		model.addAttribute("specialites", liste_specialite);
		return "Examen/creationExamenSalleForMatiere";
	}
	
	@ResponseBody
	@RequestMapping("/tableauSalle")
	public String tableauSalle(
			Model model,
			HttpSession session,
			@RequestParam(name="idSpecialite") String idSpecialite
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "";
		}
		else {
			Long idSpecialiteConvert = Long.parseLong(idSpecialite);
			List<MatiereExamen> liste_matiere = matiereExamenRepository.findAllMatiereForExamen(idSpecialiteConvert, examen.getIdExamen());
			String tableau = "";
			tableau += "<tr>";
				tableau += "<th>nom</th>";
				tableau += "<th>date d'examen</th>";
				tableau += "<th>heure de debut</th>";
				tableau += "<th>heure de fin</th>";
				tableau += "<th>Surveillant</th>";
			tableau += "</tr>";
			for(MatiereExamen m : liste_matiere) {
				tableau += "<tr>";
					tableau += "<td>" + m.getNom() + "</td>";
					tableau += "<td>" + m.getDateDebut() + "</td>";
					tableau += "<td>" + m.getHeureDebut() + "</td>";
					tableau += "<td>" + m.getHeureFin() + "</td>";
					tableau += "<td>";
						
						MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(m.getIdMatiere(), idSpecialiteConvert, examen.getIdExamen());
						if(matiereExamen != null && matiereExamen.getIdSalle() != null) {
							tableau += "<select id="+m.getIdMatiere()+" value=" + "" + matiereExamen.getIdSalle() +"" + ">";
						}
						else {
							tableau += "<select id="+m.getIdMatiere()+" value='0'>";
						}
						tableau += "<option value=0>Selectionner une salle</option>";
						List<SalleHoraire> list_salle = salleHoraireRepository.findForExamen(examen.getIdExamen());
						for(SalleHoraire s : list_salle) {
							Salle salle = salleRepository.findSalle(s.getIdSalle());
							tableau += "<option value="+salle.getIdSalle()+">"+salle.getNumero()+"</option>";
						}
						tableau += "</select>";
					tableau += "</td>";
				tableau += "</tr>";
			}
			return tableau;
		}
	}
	
	@ResponseBody
	@RequestMapping("/verifSalle")
	public String verifSalle(
			HttpSession session,
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="tabId") String tabId
	) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "";
		}
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		String[] tabIdSplit = tabId.split("¤");
		boolean verifAllSurveillantFree = true;
		String idSalleNotFree = "";
		for(String chaine : tabIdSplit) {
			String[] tab = chaine.split("-");
			if(tab[1].equals("0")) {
				return "Il faut selectionner une salle pour chaque matiere";
			}
			List<PlanningExamen> planningForMatiere = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(),idSpecialiteConvert,Long.parseLong(tab[0]));
			for(PlanningExamen p : planningForMatiere) {
				List<PlanningExamen> isFree = planningExamenRepository.findPlanningWithDate(p.getDateJour(),p.getHoraireDebut(),p.getHoraireFin(),p.getIdExamen(),p.getIdMatiere(),p.getIdSpecialite());
				if(isFree != null && isFree.size() > 0) {
					for(PlanningExamen pe : isFree) {
						if(pe.getIdSalle() != null) {
							verifAllSurveillantFree = false;
							idSalleNotFree = tab[1];
						}
					}
				}
			}
		}
		if(!verifAllSurveillantFree) {
			Salle indisponible = salleRepository.findSalle(Long.parseLong(idSalleNotFree));
			return "Surveillant "+indisponible.getNumero()+" est indisponible";
		}
		else {
			for(String chaine : tabIdSplit) {
				String[] tab = chaine.split("-");
				MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(Long.parseLong(tab[0]),idSpecialiteConvert,examen.getIdExamen());
				matiereExamen.setIdSalle(Long.parseLong(tab[1]));
				List<PlanningExamen> planningForMatiere = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(),idSpecialiteConvert,Long.parseLong(tab[0]));
				for(PlanningExamen p : planningForMatiere) {
					PlanningExamen planningToSave = planningExamenRepository.getReferenceById(p.getId());
					planningToSave.setIdSalle(Long.parseLong(tab[1]));
					planningExamenRepository.save(planningToSave);
				}
			}
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/verifPlanningSalle")
	public String verifPlanningSalle(HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session null";
		}
		/*List<PlanningExamen> list_planning = planningExamenRepository.findPlanningOfExamen(examen.getIdExamen());
		for(PlanningExamen p : list_planning) {
			if(p != null) {
				if(p.getIdSalle() == null) {
					return "Une matiere n'a pas de salle";
				}
			}
		}*/
		List<MatiereExamen> liste_matiereExamen = matiereExamenRepository.findAllMatiereOfExamen(examen.getIdExamen());
		for(MatiereExamen m : liste_matiereExamen) {
			if(m.getIdSalle() == null) {
				return "une matiere n'a pas de salle";
			}
		}
		examen.setEtatModification(9);
		examenRepository.save(examen);
		session.setAttribute("Examen", examen);
		return "";
	}
	
	@RequestMapping("/creationExamenSurveillant")
	public String creationExamenSurveillant(Model model, HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "recharger la session";
		}
		List<SpecialiteExamen> specialiteExamen = specialiteExamenRepository.findAllSpecialiteForExamen(examen.getIdExamen(),false);
		ArrayList<Specialite> liste_specialite = new ArrayList<>();
		for(SpecialiteExamen p : specialiteExamen) {
			Specialite spe  = specialiteRepository.getReferenceById(p.getIdSpecialite());
			if(spe != null) {
				liste_specialite.add(spe);
			}
		}
		model.addAttribute("idExamen", examen.getIdExamen());
		model.addAttribute("specialites", liste_specialite);
		return "Examen/planningSurveillant/creationExamenSurveillant";
	}
	
	@ResponseBody
	@RequestMapping("/tableauSurveillant")
	public String tableauSurveillant(
			Model model,
			HttpSession session,
			@RequestParam(name="idSpecialite") String idSpecialite
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "";
		}
		else {
			Long idSpecialiteConvert = Long.parseLong(idSpecialite);
			List<MatiereExamen> liste_matiere = matiereExamenRepository.findAllMatiereForExamen(idSpecialiteConvert, examen.getIdExamen());
			String tableau = "";
			tableau += "<tr>";
				tableau += "<th>nom</th>";
				tableau += "<th>date d'examen</th>";
				tableau += "<th>heure de debut</th>";
				tableau += "<th>heure de fin</th>";
				tableau += "<th>Surveillant</th>";
			tableau += "</tr>";
			for(MatiereExamen m : liste_matiere) {
				tableau += "<tr>";
					tableau += "<td>" + m.getNom() + "</td>";
					tableau += "<td>" + m.getDateDebut() + "</td>";
					tableau += "<td>" + m.getHeureDebut() + "</td>";
					tableau += "<td>" + m.getHeureFin() + "</td>";
					tableau += "<td>";
					if(m.getIdSurveillant() != null) {
						tableau += "<select id="+m.getIdMatiere()+" value =" + "" + m.getIdSurveillant() + "" +  ">";
					}
					else {
						tableau += "<select id="+m.getIdMatiere()+" value = '0' >";
					}
						
						tableau += "<option value=0>Selectionner un surveillant</option>";
						List<Personne> list_personne = personneRepository.findSurveillant(m.getIdMatiere());
						for(Personne p : list_personne) {
							tableau += "<option value="+p.getIdPersonne()+">"+p.getName()+"</option>";
						}
						tableau += "</select>";
					tableau += "</td>";
				tableau += "</tr>";
			}
			return tableau;
		}
	}
	
	@ResponseBody
	@RequestMapping("/verifSurveillant")
	public String verifSurveillant(
			HttpSession session,
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="tabId") String tabId
	) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session null";
		}
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		String[] tabIdSplit = tabId.split("¤");
		List<MatiereExamen> liste_matiere = matiereExamenRepository.findAllMatiereForExamen(idSpecialiteConvert, examen.getIdExamen());
		boolean verifAllSurveillantFree = true;
		String idSurveillantNotFree = "";
		for(String chaine : tabIdSplit) {
			String[] tab = chaine.split("-");
			if(tab[1].equals("0")) {
				return "Il faut selectionner un surveillant pour chaque matiere";
			}
			//trouver l'id surveillant lier a la matiere
			//chercher pour chaque horaire de 30min si le surveillant est libre avec dateJour/heureDebut/heureFin
			List<PlanningExamen> planningForMatiere = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(),idSpecialiteConvert,Long.parseLong(tab[0]));
			for(PlanningExamen p : planningForMatiere) {
				List<PlanningExamen> isFree = planningExamenRepository.findPlanningWithDate(p.getDateJour(),p.getHoraireDebut(),p.getHoraireFin(),p.getIdExamen(),p.getIdMatiere(),p.getIdSpecialite());
				if(isFree != null && isFree.size() > 0) {
					for(PlanningExamen pe : isFree) {
						if(pe.getIdSurveillant() != null) {
							verifAllSurveillantFree = false;
							idSurveillantNotFree = tab[1];
						}
					}
				}
			}
		}
		if(!verifAllSurveillantFree) {
			Personne indisponible = personneRepository.findUser(Long.parseLong(idSurveillantNotFree));
			return "Surveillant "+indisponible.getUsername()+" est indisponible";
		}
		else {
			for(String chaine : tabIdSplit) {
				String[] tab = chaine.split("-");
				MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(Long.parseLong(tab[0]),idSpecialiteConvert,examen.getIdExamen());
				matiereExamen.setIdSurveillant(Long.parseLong(tab[1]));
				List<PlanningExamen> planningForMatiere = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(),idSpecialiteConvert,Long.parseLong(tab[0]));
				for(PlanningExamen p : planningForMatiere) {
					PlanningExamen planningToSave = planningExamenRepository.getReferenceById(p.getId());
					planningToSave.setIdSurveillant(Long.parseLong(tab[1]));
					planningExamenRepository.save(planningToSave);
				}
			}
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/verifPlanningSurveillant")
	public String verifPlanningSurveillant(HttpSession session) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session null";
		}
		List<PlanningExamen> list_planning = planningExamenRepository.findPlanningOfExamen(examen.getIdExamen());
		for(PlanningExamen p : list_planning) {
			if(p != null) {
				if(p.getIdSurveillant() == null) {
					return "Une matiere n'a pas de surveillant";
				}
			}
		}
		examen.setEtatModification(8);
		examenRepository.save(examen);
		session.setAttribute("Examen", examen);
		return "";
	}
	
	private String translateDay(DayOfWeek day) {
		String reponse = "";
		switch(day) {
		case MONDAY :
			reponse = "Lundi;";
			break;
		case TUESDAY :
			reponse = "Mardi;";
			break;
		case WEDNESDAY :
			reponse = "Mercredi;";
			break;
		case THURSDAY :
			reponse = "Jeudi;";
			break;
		case FRIDAY :
			reponse = "Vendredi;";
			break;
		case SATURDAY :
			reponse = "Samedi;";
			break;
		case SUNDAY :
			reponse = "Dimanche;";
			break;
		}
		return reponse;
	}
	
	@RequestMapping("/tableHoraireMatiere")
	public String tableHoraireMatiere(
			Model model, 
			HttpSession session,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaineForm,
			@RequestParam(name="dateFinSemaine") String dateFinSemaineForm,
			@RequestParam(name="buttonClick") String buttonClick
			) {
		//charger la liste des horaires
		ArrayList<ListeDateExclu> List_horaire = new ArrayList<ListeDateExclu>();
		for(int i = 8; i <= 18; i++) {
			ListeDateExclu horaire = new ListeDateExclu();
			int intermediare = i+1;
			horaire.setHoraireDebut("" + i + "" + "-00");
			horaire.setHoraireFin("" + i + "" + "-30");
			List_horaire.add(horaire);
			
			ListeDateExclu horaire2 = new ListeDateExclu();
			horaire2.setHoraireDebut("" + i + "" + "-30");
			horaire2.setHoraireFin("" + intermediare + "" + "-00");
			List_horaire.add(horaire2);
		}
		//verifier qu'on connais l'examen a modifier
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		//date de semaine
		LocalDate dateDebutSemaine = null;
		LocalDate dateFinSemaine = null;
		LocalDate dateDebutExamen = examen.getDateDebut();
		LocalDate dateFinExamen = examen.getDateFin();
		//nom du premier et dernier jour de la semaine inclu dans l'examen
		DayOfWeek firstDayInclu;
		DayOfWeek lastDayInclu;
		//jours a bloquer dans le planning
		String daysLock = "";
		Long numberForAffichage = (long) 0;
		//savoir si on est dans une semaine d'examen
		boolean alert = true;
		
		
		//determiner la date de debut et de fin de semaine
		
		//Si on charge pour la premiere fois
		if(buttonClick.equals("")) {
			switch(dateDebutExamen.getDayOfWeek()) {
			case TUESDAY :
				numberForAffichage = (long) 1;
				break;
			case WEDNESDAY :
				numberForAffichage = (long) 2;
				break;
			case THURSDAY :
				numberForAffichage = (long) 3;
				break;
			case FRIDAY :
				numberForAffichage = (long) 4;
				break;
			case SATURDAY :
				numberForAffichage = (long) 5;
				break;
			case SUNDAY :
				numberForAffichage = (long) 6;
				break;
			}
			dateDebutSemaine = dateDebutExamen.minusDays(numberForAffichage);
			dateFinSemaine = dateDebutSemaine.plusDays(6);
		}
		//si passe a la semaine suivante
		if(buttonClick.equals("Up")) {
			dateDebutSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(1);
			dateFinSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(7);
			//verifier si la semaine appartien a l'examen
			if(dateDebutSemaine.isAfter(examen.getDateFin())) {
				alert = false;
			}
		}
		//si on passe a la semaine précédente
		if(buttonClick.equals("Down")) {
			dateDebutSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(7);
			dateFinSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(1);
			//verifier si la semaine appartien a l'examen
			if(dateFinSemaine.isBefore(examen.getDateDebut())) {
				alert = false;
			}
		}
		
		//bloquer les jours qui ne sont pas dans l'examen
		for(int i = 0; i<7; i++) {
			if(dateDebutSemaine != null && (dateDebutSemaine.plusDays(i).isBefore(dateDebutExamen) || dateDebutSemaine.plusDays(i).isAfter(dateFinExamen))) {
				daysLock += translateDay(dateDebutSemaine.plusDays(i).getDayOfWeek());
			}
		}
		
		
		//affichage header du tableau
		
		
			//dateDebutSemaineAffichage = "" + dateDebutSemaine.minusDays(numberForAffichage).getDayOfMonth() + "/" + "" +dateDebutSemaine.minusDays(numberForAffichage).getMonthValue() + "";
			//dateFinSemaineAffichage = "" + dateDebutSemaine.minusDays(numberForAffichage).plusDays(6).getDayOfMonth() + "/" + "" +dateDebutSemaine.minusDays(numberForAffichage).plusDays(6).getMonthValue() + "";
		String dateDebutSemaineAffichage = "" + dateDebutSemaine.getDayOfMonth() + "/" + "" +dateDebutSemaine.getMonthValue() + "";
		String dateFinSemaineAffichage = "" + dateDebutSemaine.plusDays(6).getDayOfMonth() + "/" + "" +dateDebutSemaine.plusDays(6).getMonthValue() + "";
		
		model.addAttribute("dateDebutSemaineAffichage", dateDebutSemaineAffichage);
		model.addAttribute("dateFinSemaineAffichage", dateFinSemaineAffichage);
		model.addAttribute("horaires",List_horaire);
		model.addAttribute("dateDebutSemaine",dateDebutSemaine);
		model.addAttribute("dateFinSemaine",dateFinSemaine);
		model.addAttribute("daysLock",daysLock);
		model.addAttribute("alert",alert);
		return "Examen/planningMatiere/tableHoraireMatiere";
	}
	
	
	
	@RequestMapping("/tableHoraire")
	public String tableHoraire(
			Model model, 
			HttpSession session,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaineForm,
			@RequestParam(name="dateFinSemaine") String dateFinSemaineForm,
			@RequestParam(name="buttonClick") String buttonClick
			) {
		//charger la liste des horaires
		ArrayList<ListeDateExclu> List_horaire = new ArrayList<ListeDateExclu>();
		for(int i = 8; i <= 19; i++) {
			ListeDateExclu horaire = new ListeDateExclu();
			int intermediare = i+1;
			horaire.setHoraireDebut("" + i + "" + "-00");
			horaire.setHoraireFin("" + i + "" + "-30");
			List_horaire.add(horaire);
			
			ListeDateExclu horaire2 = new ListeDateExclu();
			horaire2.setHoraireDebut("" + i + "" + "-30");
			horaire2.setHoraireFin("" + intermediare + "" + "-00");
			List_horaire.add(horaire2);
		}
		//verifier qu'on connais l'examen a modifier
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		//date de semaine
		LocalDate dateDebutSemaine = null;
		LocalDate dateFinSemaine = null;
		LocalDate dateDebutExamen = examen.getDateDebut();
		LocalDate dateFinExamen = examen.getDateFin();
		//nom du premier et dernier jour de la semaine inclu dans l'examen
		DayOfWeek firstDayInclu;
		DayOfWeek lastDayInclu;
		//jours a bloquer dans le planning
		String daysLock = "";
		Long numberForAffichage = (long) 0;
		//savoir si on est dans une semaine d'examen
		boolean alert = true;
		
		
		//determiner la date de debut et de fin de semaine
		
		//Si on charge pour la premiere fois
		if(buttonClick.equals("")) {
			switch(dateDebutExamen.getDayOfWeek()) {
			case TUESDAY :
				numberForAffichage = (long) 1;
				break;
			case WEDNESDAY :
				numberForAffichage = (long) 2;
				break;
			case THURSDAY :
				numberForAffichage = (long) 3;
				break;
			case FRIDAY :
				numberForAffichage = (long) 4;
				break;
			case SATURDAY :
				numberForAffichage = (long) 5;
				break;
			case SUNDAY :
				numberForAffichage = (long) 6;
				break;
			}
			dateDebutSemaine = dateDebutExamen.minusDays(numberForAffichage);
			dateFinSemaine = dateDebutSemaine.plusDays(6);
		}
		//si passe a la semaine suivante
		if(buttonClick.equals("Up")) {
			dateDebutSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(1);
			dateFinSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(7);
			//verifier si la semaine appartien a l'examen
			if(dateDebutSemaine.isAfter(examen.getDateFin())) {
				alert = false;
			}
		}
		//si on passe a la semaine précédente
		if(buttonClick.equals("Down")) {
			dateDebutSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(7);
			dateFinSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(1);
			//verifier si la semaine appartien a l'examen
			if(dateFinSemaine.isBefore(examen.getDateDebut())) {
				alert = false;
			}
		}
		
		//bloquer les jours qui ne sont pas dans l'examen
		for(int i = 0; i<7; i++) {
			if(dateDebutSemaine != null && (dateDebutSemaine.plusDays(i).isBefore(dateDebutExamen) || dateDebutSemaine.plusDays(i).isAfter(dateFinExamen))) {
				daysLock += translateDay(dateDebutSemaine.plusDays(i).getDayOfWeek());
			}
		}
		
		//affichage header du tableau
		
		
			//dateDebutSemaineAffichage = "" + dateDebutSemaine.minusDays(numberForAffichage).getDayOfMonth() + "/" + "" +dateDebutSemaine.minusDays(numberForAffichage).getMonthValue() + "";
			//dateFinSemaineAffichage = "" + dateDebutSemaine.minusDays(numberForAffichage).plusDays(6).getDayOfMonth() + "/" + "" +dateDebutSemaine.minusDays(numberForAffichage).plusDays(6).getMonthValue() + "";
		String dateDebutSemaineAffichage = "" + dateDebutSemaine.getDayOfMonth() + "/" + "" +dateDebutSemaine.getMonthValue() + "";
		String dateFinSemaineAffichage = "" + dateDebutSemaine.plusDays(6).getDayOfMonth() + "/" + "" +dateDebutSemaine.plusDays(6).getMonthValue() + "";
		
		model.addAttribute("dateDebutSemaineAffichage", dateDebutSemaineAffichage);
		model.addAttribute("dateFinSemaineAffichage", dateFinSemaineAffichage);
		model.addAttribute("horaires",List_horaire);
		model.addAttribute("dateDebutSemaine",dateDebutSemaine);
		model.addAttribute("dateFinSemaine",dateFinSemaine);
		model.addAttribute("daysLock",daysLock);
		model.addAttribute("alert",alert);
		return "Examen/tableHoraire";
	}
	
	@RequestMapping("/dropdownMatiere")
	public String dropdownMatiere(
			@RequestParam(name="idSpecialite") String idSpecialite,
			Model model,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		Specialite specialite = specialiteRepository.findSpecialite(idSpecialiteConvert);
		List<MatiereExamen> liste_matiereExamen = matiereExamenRepository.findAllMatiereForExamen(idSpecialiteConvert,examen.getIdExamen());
		ArrayList<Matiere> liste_matiere = new ArrayList<>();
		for(MatiereExamen m : liste_matiereExamen) {
			Matiere matiere = matiereRepository.getReferenceById(m.getIdMatiere());
			if(matiere != null) {
				liste_matiere.add(matiere);
			}
		}
		model.addAttribute("matieres",liste_matiere);
		return "Examen/planningMatiere/dropdownMatiere";
	}
	
	
	@ResponseBody
	@RequestMapping("/choseMatiereMutualise")
	public String choseMatiereMutualise(
			@RequestParam(name="nomMatiere") String nomMatiere,
			Model model
			) {
		this.liste_idSpecialite = matiereExamenRepository.findSharedSpecialite(nomMatiere);
		return "";
	}
	
	@RequestMapping("/tableauMutualise")
	public String tableauMutualise(Model model) {
		ArrayList<Specialite> liste_specialite = new ArrayList<Specialite>();
		for(Long id : liste_idSpecialite) {
			liste_specialite.add(specialiteRepository.findSpecialite(id));
		}
		 
		model.addAttribute("specialites", liste_specialite);
		return "Examen/tableauMutualise";
	}
	
	@ResponseBody
	@RequestMapping("/findExamen")
	public String findExamen(
			@RequestParam(name="idExamen") Long idExamen,
			HttpSession session
			) {
		Examen examen = examenRepository.findByIdExamen(idExamen);
		session.setAttribute("Examen", examen);
		String url = "";
		switch(examen.getEtatModification()) {
			case 1 : 
				url = "/Examen/creationExamenSpecialite";
				break;
			case 2 :
				url = "/Examen/creationExamenMatiere";
				break;
			case 3 :
				url = "/Examen/creationExamenMatiereMutualise";
				break;
			case 4 :
				url = "/Examen/creationExamenSalle";
				break;
			case 5 :
				url = "/Examen/creationExamenHoraire";
				break;
			case 6 :
				url = "/Examen/creationExamenHoraireMatiere";
				break;
			case 7 :
				url = "/Examen/creationExamenSurveillant";
				break;
			case 8 :
				url = "/Examen/creationExamenSalleForMatiere";
				break;
		}
		return url;
	}
	
	@ResponseBody
	@RequestMapping("/creationDate")
	public String creationDate(
			@RequestParam(name="name") String name,
			@RequestParam(name="dateDebut") String dateDebut,
			@RequestParam(name="dateFin") String dateFin,
			HttpSession session
			) {
		String msg_err = "";
		//Date todaysDate = new Date();
		LocalDate todaysDate = LocalDate.now();
		if(name == "") {
			return "Le nom est vide";
		}
		if(examenRepository.findByName(name) != null) {
			return "un examen porte déjà ce nom";
		}
		try {
			/*Date dateDebutConvert = new SimpleDateFormat("dd/MM/yyyy").parse(dateDebut);
			Date dateFinConvert = new SimpleDateFormat("dd/MM/yyyy").parse(dateFin);*/
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate dateDebutConvert = LocalDate.parse(dateDebut);
			LocalDate dateFinConvert = LocalDate.parse(dateFin);
			if(dateDebutConvert.isBefore(todaysDate)) {
				msg_err = "La date de debut doit etre superieur a la date d'aujourd'hui";
			}
			else if(dateFinConvert.isBefore(dateDebutConvert) || dateFinConvert.isEqual(dateDebutConvert)) {
				msg_err = "La date de fin doit etre superieur a la date de debut";
			}
			else {
				Examen examen = new Examen(name,dateDebutConvert,dateFinConvert,1);
				examenRepository.save(examen);
				examen.setEtatModification(1);
				session.setAttribute("Examen", examen);
			}
		}catch (Exception e) {
			msg_err = "ERREUR dans la conversion des dates";
		}
		return msg_err;
	}
	

	@ResponseBody
	@RequestMapping("/exclureSpecialite")
	public String exclureSpecialite(
			@RequestParam(name="idSpecialite") String List_idSpecialite,
			@RequestParam(name="idSpecialiteInclure") String List_idSpecialite_Inclure,
			HttpSession session
			) {
		String[] tabIdSpecialite = List_idSpecialite.split(";");
		String[] tabIdSpecialiteInclure = List_idSpecialite_Inclure.split(";");
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		Long idConvert;
		for(String id : tabIdSpecialite) {
			if(id != "") {
				idConvert = Long.parseLong(id);
				SpecialiteExamen specialiteExamen = specialiteExamenRepository.findSpecialiteForExamen(idConvert, examen.getIdExamen());
				if(specialiteExamen != null) {
					SpecialiteExamen specialiteToDelete = specialiteExamenRepository.getReferenceById(specialiteExamen.getId());
					specialiteExamenRepository.delete(specialiteToDelete);
				}
			}
		}
		//verifier si les spécialité n'ont pas un examen dont les dates chevauche cette examen
		for(String id : tabIdSpecialiteInclure) {
			if(id != "") {
				idConvert = Long.parseLong(id);
				SpecialiteExamen specialiteExamen = specialiteExamenRepository.findSpecialiteForExamen(idConvert, examen.getIdExamen());
			}
		}
		//ajouter les spécialite
		for(String id : tabIdSpecialiteInclure) {
			if(id != "") {
				idConvert = Long.parseLong(id);
				SpecialiteExamen specialiteExamen = specialiteExamenRepository.findSpecialiteForExamen(idConvert, examen.getIdExamen());
				if(specialiteExamen == null) {
					specialiteExamen = new SpecialiteExamen();
					specialiteExamen.setIdSpecialite(idConvert);
					specialiteExamen.setIdExamen(examen.getIdExamen());
					specialiteExamen.setBoolExclu(false);
					specialiteExamenRepository.save(specialiteExamen);
				}
				/*else {
					SpecialiteExamen modifSpecialite = specialiteExamenRepository.getReferenceById(specialiteExamen.getId());
					modifSpecialite.setBoolExclu(true);
					specialiteExamenRepository.save(modifSpecialite);
				}*/
			}
		}
		examen.setEtatModification(2);
		Examen examenToSave = examenRepository.getReferenceById(examen.getIdExamen());
		examenToSave.setEtatModification(2);
		examenRepository.save(examenToSave);
		session.setAttribute("Examen", examen);
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/exclureMatiere")
	public String exclureMatiere(
			@RequestParam(name="idMatiere") String List_idMatiere,
			@RequestParam(name="idMatiereExclure") String List_idMatiere_Exclure,
			HttpSession session
			) {
		String[] tabIdMatiere = List_idMatiere.split(";");
		String[] tabIdMatiereExclure = List_idMatiere_Exclure.split(";");
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		Long idMatiereConvert;
		Long idSpecialiteConvert;
		for(String id : tabIdMatiere) {
			if(id != "") {
				String[] tab = id.split(",");
				String idMatiere = tab[0];
				String idSpecialite = tab[1];
				idMatiereConvert = Long.parseLong(idMatiere);
				idSpecialiteConvert = Long.parseLong(idSpecialite);
				MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(idMatiereConvert, idSpecialiteConvert, examen.getIdExamen());
				Matiere matiere = matiereRepository.getReferenceById(idMatiereConvert);
				if(matiereExamen == null) {
					matiereExamen = new MatiereExamen();
					matiereExamen.setIdExamen(examen.getIdExamen());
					matiereExamen.setIdMatiere(idMatiereConvert);
					matiereExamen.setIdSpecialite(idSpecialiteConvert);
					matiereExamen.setBoolMutualise(false);
					matiereExamen.setNom(matiere.getNom());
					matiereExamenRepository.save(matiereExamen);
				}
			}
		}
		for(String id : tabIdMatiereExclure) {
			if(id != "") {
				String[] tab = id.split(",");
				String idMatiere = tab[0];
				String idSpecialite = tab[1];
				idMatiereConvert = Long.parseLong(idMatiere);
				idSpecialiteConvert = Long.parseLong(idSpecialite);
				MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(idMatiereConvert, idSpecialiteConvert, examen.getIdExamen());
				if(matiereExamen != null) {
					MatiereExamen matiere = matiereExamenRepository.getReferenceById(matiereExamen.getId());
					matiereExamenRepository.delete(matiere);
				}
			}
		}
		examen.setEtatModification(3);
		Examen examenToSave = examenRepository.getReferenceById(examen.getIdExamen());
		examenToSave.setEtatModification(3);
		examenRepository.save(examenToSave);
		session.setAttribute("Examen", examen);
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/exclureSalle")
	public String exclureSalle(
			@RequestParam(name="idSalle") String List_idSalle,
			@RequestParam(name="idSalleExclure") String List_idSalle_Exclure,
			HttpSession session
			) {
		String[] tabIdSalle = List_idSalle.split(";");
		String[] tabIdSalleExclure = List_idSalle_Exclure.split(";");
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		Long idConvert;
		for(String id : tabIdSalle) {
			if(id != "") {
				idConvert = Long.parseLong(id);
				SalleHoraire salleHoraire = salleHoraireRepository.findSalleHoraireForExamen(idConvert, examen.getIdExamen());
				/*if(salleHoraire == null) {
					salleHoraire = new SalleHoraire();
					salleHoraire.setIdSalle(idConvert);
					salleHoraire.setIdExamen(examen.getIdExamen());
					salleHoraire.setBoolDisponible(true);
					salleHoraireRepository.save(salleHoraire);
				}*/
				if(salleHoraire != null) {
					SalleHoraire salleToDelete = salleHoraireRepository.getReferenceById(salleHoraire.getId());
					salleHoraireRepository.delete(salleToDelete);
				}
			}
		}
		for(String id : tabIdSalleExclure) {
			if(id != "") {
				idConvert = Long.parseLong(id);
				SalleHoraire salleHoraire = salleHoraireRepository.findSalleHoraireForExamen(idConvert, examen.getIdExamen());
				if(salleHoraire == null) {
					salleHoraire = new SalleHoraire();
					salleHoraire.setIdSalle(idConvert);
					salleHoraire.setIdExamen(examen.getIdExamen());
					salleHoraire.setBoolDisponible(true);
					salleHoraireRepository.save(salleHoraire);
				}
				/*else {
					SalleHoraire modifSalleHoraire = salleHoraireRepository.getReferenceById(salleHoraire.getId());
					modifSalleHoraire.setBoolDisponible(false);
					salleHoraireRepository.save(modifSalleHoraire);
				}*/
			}
		}
		examen.setEtatModification(5);
		Examen examenToSave = examenRepository.getReferenceById(examen.getIdExamen());
		examenToSave.setEtatModification(5);
		examenRepository.save(examenToSave);
		session.setAttribute("Examen", examen);
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/addMatiereMutualise")
	public String addMatiereMutualise(
			@RequestParam(name="idSpecialite") String List_idSpecialite,
			@RequestParam(name="nom") String nom,
			HttpSession session
			) {
		String[] tabIdSpecialite = List_idSpecialite.split(";");
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "session examen null recharger la page correctement";
		}
		Long idConvert;
		for(String id : tabIdSpecialite) {
			if(id != "") {
				idConvert = Long.parseLong(id);
				MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(nom,idConvert,examen.getIdExamen());
				if(matiereExamen != null) {
					matiereExamen.setBoolMutualise(true);
					matiereExamenRepository.save(matiereExamen);
				}
			}
		}
		MatiereExamenMutualise matiereExamenMutualise = matiereExamenMutualiseRepository.findMatiereWithIdAndName(nom,examen.getIdExamen());
		if(matiereExamenMutualise != null) {
			matiereExamenMutualise.setIdSpecialite(List_idSpecialite);
			matiereExamenMutualiseRepository.save(matiereExamenMutualise);
		}
		else {
			matiereExamenMutualise = new MatiereExamenMutualise(nom,examen.getIdExamen(),List_idSpecialite);
			matiereExamenMutualiseRepository.save(matiereExamenMutualise);
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/addHoraireExclure")
	public String addHoraireExclure(
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="ListExclure") String ListHoraireExclure,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaine,
			@RequestParam(name="listeIdAtStart") String listeIdAtStart,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "";
		}
		String[] tabHoraireExclure = ListHoraireExclure.split("¤");
		String[] tabHoraire = listeIdAtStart.split("¤");
		LocalDate dateDebutSemaineConvert = LocalDate.parse(dateDebutSemaine);
		Long addDay =  (long) 0;
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		//supprimer les horaires qui ont été changer
		for(String horaire : tabHoraire) {
			if(!horaire.equals("")) {
				String[] parameter = horaire.split("-");
				switch(parameter[0]) {
				case "Mardi" :
					addDay = (long) 1;
					break;
				case "Mercredi" :
					addDay = (long) 2;
					break;
				case "Jeudi" :
					addDay = (long) 3;
					break;
				case "Vendredi" :
					addDay = (long) 4;
					break;
				case "Samedi" :
					addDay = (long) 5;
					break;
				case "Dimanche" :
					addDay = (long) 6;
					break;
				}
				LocalDate dateOfDay = dateDebutSemaineConvert.plusDays(addDay);
				ListeDateExclu dateExist = listeDateExcluRepository.AlreadyExist(idSpecialiteConvert, examen.getIdExamen(), dateOfDay, parameter[1] + "-" + parameter[2], parameter[3] + "-" + parameter[4]);
				if(dateExist != null) {
					ListeDateExclu horaireToDelete = listeDateExcluRepository.getReferenceById(dateExist.getId());
					listeDateExcluRepository.delete(horaireToDelete);
				}
			}
		}
		//ajouter les horaires
		for(String horaire : tabHoraireExclure) {
			String[] parameter = horaire.split("-");
			if(!horaire.equals("")) {
				switch(parameter[0]) {
				case "Lundi" :
					addDay = (long) 0;
					break;
				case "Mardi" :
					addDay = (long) 1;
					break;
				case "Mercredi" :
					addDay = (long) 2;
					break;
				case "Jeudi" :
					addDay = (long) 3;
					break;
				case "Vendredi" :
					addDay = (long) 4;
					break;
				case "Samedi" :
					addDay = (long) 5;
					break;
				case "Dimanche" :
					addDay = (long) 6;
					break;
				}
				LocalDate dateOfDay = dateDebutSemaineConvert.plusDays(addDay);
				ListeDateExclu dateExist = listeDateExcluRepository.AlreadyExist(idSpecialiteConvert, examen.getIdExamen(), dateOfDay, parameter[1] + "-" + parameter[2], parameter[3] + "-" + parameter[4]);
				if(dateExist == null) {
					ListeDateExclu l = new ListeDateExclu();
					l.setIdExamen(examen.getIdExamen());
					l.setIdSpecialite(idSpecialiteConvert);
					l.setDateJour(dateOfDay);
					l.setHoraireDebut(parameter[1] + "-" + parameter[2]);
					l.setHoraireFin(parameter[3] + "-" + parameter[4]);
					listeDateExcluRepository.save(l);
				}
			}
		}
		return "";
	}
	
	@ResponseBody
	@RequestMapping("/getPlanningMatiereForCheckbox")
	public String getPlanningMatiereForCheckbox(
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="idMatiere") String idMatiere,
			Model model,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "";
		}
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		Long idMatiereConvert = Long.parseLong(idMatiere);
		List<PlanningExamen> liste_planning = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(),idSpecialiteConvert,idMatiereConvert);
		String id = "";
		for(PlanningExamen p : liste_planning) {
			id += translateDay(p.getDateJour().getDayOfWeek());
			id = id.replace(";", "-");
			id += p.getHoraireDebut() + "-" + p.getHoraireFin();
			id += "¤";
		}
		return id;
	}
	
	@ResponseBody
	@RequestMapping("/getAllPlanningMatiereForCheckbox")
	public String getAllPlanningMatiereForCheckbox(
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaine,
			@RequestParam(name="dateFinSemaine") String dateFinSemaine,
			Model model,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "";
		}
		LocalDate dateDebutSemaineConvert = LocalDate.parse(dateDebutSemaine);
		LocalDate dateFinSemaineConvert = LocalDate.parse(dateFinSemaine);
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		List<PlanningExamen> liste_planning = planningExamenRepository.findPlanningOfSpecialite(examen.getIdExamen(),idSpecialiteConvert,dateDebutSemaineConvert,dateFinSemaineConvert);
		String id = "";
		for(PlanningExamen p : liste_planning) {
			id += translateDay(p.getDateJour().getDayOfWeek());
			id = id.replace(";", "-");
			id += p.getHoraireDebut() + "-" + p.getHoraireFin();
			id += "¤";
		}
		return id;
	}
	
	@ResponseBody
	@RequestMapping("/getDateExlue")
	public String getDateExlue(
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="dateFinSemaine") String dateFinSemaine,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaine,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "examen null recharger session";
		}
		List<ListeDateExclu> liste_date = listeDateExcluRepository.findAllDateExclueForDate(Long.parseLong(idSpecialite), examen.getIdExamen(), LocalDate.parse(dateDebutSemaine), LocalDate.parse(dateFinSemaine));
		String id = "";
		for(ListeDateExclu l : liste_date) {
			id += translateDay(l.getDateJour().getDayOfWeek());
			id = id.replace(";", "-");
			id += l.getHoraireDebut() + "-" + l.getHoraireFin();
			id += "¤";
		}
		return id;
	}
	
	@ResponseBody
	@RequestMapping("/verifOnMatiere")
	public String verifOnMatiere(
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="idMatiere") String idMatiere,
			@RequestParam(name="listIdForOneMatiere") String listIdForOneMatiere,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaine,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "examen null recharger session";
		}
		String[] tabHoraire = listIdForOneMatiere.split("¤");
		LocalDate dateDebutSemaineConvert = LocalDate.parse(dateDebutSemaine);
		Long addDay =  (long) 0;
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		Long idMatiereConvert = Long.parseLong(idMatiere);
		//retrouver la matiere
		Matiere matiere = matiereRepository.getReferenceById(idMatiereConvert);
		MatiereExamen matiereExamen = matiereExamenRepository.findMatiereForExamen(idMatiereConvert,idSpecialiteConvert,examen.getIdExamen());
		//si tableau vide supprimer les horraires de la matiere
		if(listIdForOneMatiere.equals("")) {
			matiereExamen.setDateDebut(null);
			matiereExamen.setHeureDebut(null);
			matiereExamen.setHeureFin(null);
			matiereExamenRepository.save(matiereExamen);
			List<PlanningExamen> planning = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(), idSpecialiteConvert, matiereExamen.getIdMatiere());
			if(planning.size()>0) {
				for(PlanningExamen p : planning) {
					planningExamenRepository.delete(p);
				}
				return "La matiere n'est plus enregistrer";
			}
			else {
				return "";
			}
		}
		//verifier que le temps en bdd correspond au temps avec les checkbox
		String timeMatiere = "" + matiere.getTempsExamen() + "";
		String[] tabTimeMatiere = timeMatiere.split(":");
		int countCheckbox = tabHoraire.length;
		int countTimeMatiere = Integer.parseInt(tabTimeMatiere[0]) * 2;
		if(tabTimeMatiere[1].equals("30")) {
			countTimeMatiere += 1;
		}
		if(countCheckbox > countTimeMatiere) {
			return "Horaire definit superieur au temps d'examen de la matiere";
		}
		else if(countCheckbox < countTimeMatiere) {
			return "Horaire definit inferieur au temps d'examen de la matiere";
		}
		//verifier que les horaires sont bien coller
		if(matiere.getTempsExamen().getHour() > 0) {
			String[] tabSplit;
			String valueSplitBefore;
			String valueSplitAfter;
			int intermediaire;
			boolean verifHoraire = false;
			for(String horaire : tabHoraire) {
				if(!horaire.equals("")) {
					tabSplit = horaire.split("-");
						if(tabSplit[2].equals("00")) {
							intermediaire = Integer.parseInt(tabSplit[1]) - 1;
							valueSplitBefore = "" + intermediaire + "-30";
							valueSplitAfter = tabSplit[1] + "-30";
						}
						else {
							valueSplitBefore = tabSplit[1] + "-00";
							intermediaire = Integer.parseInt(tabSplit[1]) + 1;
							valueSplitAfter = "" + intermediaire + "-00";
						}
						for(String horaireToTest : tabHoraire) {
							if(!horaire.equals("") && !horaireToTest.equals(horaire)) {
								String valueHoraireDebut = horaireToTest.split("-")[1] + "-" + horaireToTest.split("-")[2];
								if(valueHoraireDebut.equals(valueSplitBefore) || valueHoraireDebut.equals(valueSplitAfter)) {
									verifHoraire = true;
									//return "L'horaire de " + horaireToTest.split("-")[0] + " de : " + horaireToTest.split("-")[1] + "-" + horaireToTest.split("-")[2] + " a " + horaireToTest.split("-")[3] + "-" + horaireToTest.split("-")[4] + " n'est pas disponible";
								}
							}
						}
						if(!verifHoraire) {
							return "Il ne doit pas y avoir de pause entre les horaires";
						}
					}
				
			}
		}
		
		
		//si pas mutualisé enregistrer
		if(!matiereExamen.isBoolMutualise()) {
			//recuperer la date de la premiere checkbox et la date du jour
			String[] parameter = tabHoraire[0].split("-");
			String dayOfFirst = "";
			switch(parameter[0]) {
			case "Lundi" :
				dayOfFirst = "Lundi";
				break;
			case "Mardi" :
				addDay = (long) 1;
				dayOfFirst = "Mardi";
				break;
			case "Mercredi" :
				addDay = (long) 2;
				dayOfFirst = "Mercredi";
				break;
			case "Jeudi" :
				addDay = (long) 3;
				dayOfFirst = "Jeudi";
				break;
			case "Vendredi" :
				addDay = (long) 4;
				dayOfFirst = "Vendredi";
				break;
			case "Samedi" :
				addDay = (long) 5;
				dayOfFirst = "Samedi";
				break;
			case "Dimanche" :
				addDay = (long) 6;
				dayOfFirst = "Dimanche";
				break;
			}
			//verifier que les horaires sont bien coller
			LocalDate dateOfDay = dateDebutSemaineConvert.plusDays(addDay);
			for(String horaire : tabHoraire) {
				parameter = horaire.split("-");
				if(!horaire.equals("")) {
					if(!parameter[0].equals(dayOfFirst)) {
						return "Les horaires doivent etre coller";
					}
					//gerer le cas ou les horaires ne sont pas coller par rapport a l'heure
				}
			}	
			//verifier que les horaires sont libre
			for(String horaire : tabHoraire) {
				parameter = horaire.split("-");
				if(!horaire.equals("")) {
					PlanningExamen p = planningExamenRepository.findIfDateFree(examen.getIdExamen(), idSpecialiteConvert, matiereExamen.getIdMatiere(), dateOfDay, parameter[1] + "-" + parameter[2],parameter[3] + "-" + parameter[4]);
					if(p != null && !p.getHoraireDebut().equals("")) {
						return "L'horaire " + p.getHoraireDebut() + "du " + p.getDateJour() + " pour la spécialite " + p.getIdSpecialite() +" est deja pris";
					}
				}
			}
			//chercher l'heure de debut et de fin
			String heureDebutToSave = "";
			String heureFinToSave = "";
			for(String horaire : tabHoraire) {
				parameter = horaire.split("-");
				if(heureDebutToSave.equals("")) {
					heureDebutToSave = parameter[1] + "-" + parameter[2];
					heureFinToSave = parameter[3] + "-" + parameter[4];
				}
				if(Integer.parseInt(parameter[1]) < Integer.parseInt(heureDebutToSave.split("-")[0])) {
					heureDebutToSave = parameter[1] + "-" + parameter[2];
				}
				else if(Integer.parseInt(parameter[1]) == Integer.parseInt(heureDebutToSave.split("-")[0])) {
					if(Integer.parseInt(parameter[2]) < Integer.parseInt(heureDebutToSave.split("-")[1])) {
						heureDebutToSave = parameter[1] + "-" + parameter[2];
					}
				}
				
				if(Integer.parseInt(parameter[3]) > Integer.parseInt(heureFinToSave.split("-")[0])) {
					heureFinToSave = parameter[3] + "-" + parameter[4];
				}
				else if(Integer.parseInt(parameter[3]) == Integer.parseInt(heureFinToSave.split("-")[0])) {
					if(Integer.parseInt(parameter[4]) > Integer.parseInt(heureFinToSave.split("-")[1])) {
						heureFinToSave = parameter[3] + "-" + parameter[4];
					}
				}
			} 
			//ajouter les horaires dans matiereExamen
			MatiereExamen matiereExamenToSaveExamen = matiereExamenRepository.getReferenceById(matiereExamen.getId());
			matiereExamenToSaveExamen.setDateDebut(dateOfDay);
			matiereExamenToSaveExamen.setHeureDebut(heureDebutToSave);
			matiereExamenToSaveExamen.setHeureFin(heureFinToSave);
			matiereExamenRepository.save(matiereExamenToSaveExamen);
			//supprimer tous les anciens horaires de l'examen
			List<PlanningExamen> liste_p = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(), idSpecialiteConvert, matiereExamen.getIdMatiere());
			if(liste_p != null && liste_p.size() > 0) {
				for(PlanningExamen pe : liste_p) {
					if(pe != null) {
						PlanningExamen pToDelete = planningExamenRepository.getReferenceById(pe.getId());
						planningExamenRepository.delete(pToDelete);
					}
				}
			}
			
			//ajouter les horaires dans le planningExamen
			for(String horaire : tabHoraire) {
				parameter = horaire.split("-");
				PlanningExamen p = planningExamenRepository.findDate(examen.getIdExamen(), idSpecialiteConvert, matiereExamen.getIdMatiere(),dateOfDay, parameter[1] + "-" + parameter[2],parameter[3] + "-" + parameter[4]);
				if(p == null) {
					PlanningExamen planning = new PlanningExamen();
					planning.setDateJour(dateOfDay);
					planning.setHoraireDebut(parameter[1] + "-" + parameter[2]);
					planning.setHoraireFin(parameter[3] + "-" + parameter[4]);
					planning.setIdExamen(examen.getIdExamen());
					planning.setIdMatiere(matiereExamen.getIdMatiere());
					planning.setIdSpecialite(idSpecialiteConvert);
					planningExamenRepository.save(planning);
				}
				else {
					PlanningExamen pToSave = planningExamenRepository.getReferenceById(p.getId());
					pToSave.setDateJour(dateOfDay);
					pToSave.setHoraireDebut(parameter[1] + "-" + parameter[2]);
					pToSave.setHoraireFin(parameter[3] + "-" + parameter[4]);
					pToSave.setIdExamen(examen.getIdExamen());
					pToSave.setIdMatiere(matiereExamen.getIdMatiere());
					pToSave.setIdSpecialite(idSpecialiteConvert);
					planningExamenRepository.save(pToSave);
				}
			}
			
		}
		else {
			//recuperer la date de la premiere checkbox et la date du jour
			String[] parameter = tabHoraire[0].split("-");
			String dayOfFirst = "";
			switch(parameter[0]) {
			case "Lundi" :
				dayOfFirst = "Lundi";
				break;
			case "Mardi" :
				addDay = (long) 1;
				dayOfFirst = "Mardi";
				break;
			case "Mercredi" :
				addDay = (long) 2;
				dayOfFirst = "Mercredi";
				break;
			case "Jeudi" :
				addDay = (long) 3;
				dayOfFirst = "Jeudi";
				break;
			case "Vendredi" :
				addDay = (long) 4;
				dayOfFirst = "Vendredi";
				break;
			case "Samedi" :
				addDay = (long) 5;
				dayOfFirst = "Samedi";
				break;
			case "Dimanche" :
				addDay = (long) 6;
				dayOfFirst = "Dimanche";
				break;
			}
			LocalDate dateOfDay = dateDebutSemaineConvert.plusDays(addDay);
			//verifier que les horaires sont bien coller
			for(String horaire : tabHoraire) {
				parameter = horaire.split("-");
				if(!horaire.equals("")) {
					if(!parameter[0].equals(dayOfFirst)) {
						return "Les horaires doivent etre le meme jours";
					}
					//gerer le cas ou les horaires ne sont pas coller par rapport a l'heure
				}
			}	
					
			//recuperer la liste de la meme matiere partager par les spécialité
			List<MatiereExamen> liste_matiereExamen = matiereExamenRepository.findListSharedMatiere(examen.getIdExamen(), matiereExamen.getNom(),true);
			//verifier pour chaque spécialite si l'horaire est libre
			for(MatiereExamen m : liste_matiereExamen) {
				for(String horaire : tabHoraire) {
					parameter = horaire.split("-");
					if(!horaire.equals("")) {
						PlanningExamen p = planningExamenRepository.findDate(examen.getIdExamen(), m.getIdSpecialite(), m.getIdMatiere(),dateOfDay, parameter[1] + "-" + parameter[2],parameter[3] + "-" + parameter[4]);
						if(p != null && !p.getHoraireDebut().equals("")) {
							return "L'horaire " + p.getHoraireDebut() + "du " + p.getDateJour() + " pour la spécialite " + p.getIdSpecialite() +" est deja pris";
						}
					}
				}
			}
			//chercher l'heure de debut et de fin
			String heureDebutToSave = "";
			String heureFinToSave = "";
			for(String horaire : tabHoraire) {
				parameter = horaire.split("-");
				if(heureDebutToSave.equals("")) {
					heureDebutToSave = parameter[1] + "-" + parameter[2];
					heureFinToSave = parameter[3] + "-" + parameter[4];
				}
				if(Integer.parseInt(parameter[1]) < Integer.parseInt(heureDebutToSave.split("-")[0])) {
					heureDebutToSave = parameter[1] + "-" + parameter[2];
				}
				else if(Integer.parseInt(parameter[1]) == Integer.parseInt(heureDebutToSave.split("-")[0])) {
					if(Integer.parseInt(parameter[2]) < Integer.parseInt(heureDebutToSave.split("-")[1])) {
						heureDebutToSave = parameter[1] + "-" + parameter[2];
					}
				}
				
				if(Integer.parseInt(parameter[3]) > Integer.parseInt(heureFinToSave.split("-")[0])) {
					heureFinToSave = parameter[3] + "-" + parameter[4];
				}
				else if(Integer.parseInt(parameter[3]) == Integer.parseInt(heureFinToSave.split("-")[0])) {
					if(Integer.parseInt(parameter[4]) > Integer.parseInt(heureFinToSave.split("-")[1])) {
						heureFinToSave = parameter[3] + "-" + parameter[4];
					}
				}
			}
			
			//enregistre dans MatiereExamen pour toute les matieres + planningExamen pour tout les spécialite
			for(MatiereExamen m: liste_matiereExamen) {
				MatiereExamen matiereToSave = matiereExamenRepository.getReferenceById(m.getId());
				matiereToSave.setDateDebut(dateOfDay);
				matiereToSave.setHeureDebut(heureDebutToSave);
				matiereToSave.setHeureFin(heureFinToSave);
				matiereExamenRepository.save(matiereToSave);
				
				//supprimer ancien horaires
				List<PlanningExamen> liste_p = planningExamenRepository.findDateOfMatiere(examen.getIdExamen(), idSpecialiteConvert, m.getIdMatiere());
				if(liste_p != null && liste_p.size() > 0) {
					for(PlanningExamen pe : liste_p) {
						if(pe != null) {
							PlanningExamen pToDelete = planningExamenRepository.getReferenceById(pe.getId());
							planningExamenRepository.delete(pToDelete);
						}
					}
				}
				//enregistrer nouveau horaire
				for(String horaire : tabHoraire) {
					parameter = horaire.split("-");
					if(!horaire.equals("")) {
						PlanningExamen p = planningExamenRepository.findDate(examen.getIdExamen(), m.getIdSpecialite(), m.getIdMatiere(),dateOfDay, parameter[1] + "-" + parameter[2],parameter[2] + "-" + parameter[3]);
						if(p == null) {
							PlanningExamen pToSave = new PlanningExamen();
							pToSave.setDateJour(dateOfDay);
							pToSave.setHoraireDebut(parameter[1] + "-" + parameter[2]);
							pToSave.setHoraireFin(parameter[3] + "-" + parameter[4]);
							pToSave.setIdExamen(examen.getIdExamen());
							pToSave.setIdMatiere(m.getIdMatiere());
							pToSave.setIdSpecialite(m.getIdSpecialite());
							planningExamenRepository.save(pToSave);
						}
						else {
							PlanningExamen pToSave = planningExamenRepository.getReferenceById(p.getId());
							pToSave.setDateJour(dateOfDay);
							pToSave.setHoraireDebut(parameter[1] + "-" + parameter[2]);
							pToSave.setHoraireFin(parameter[3] + "-" + parameter[4]);
							pToSave.setIdExamen(examen.getIdExamen());
							pToSave.setIdMatiere(m.getIdMatiere());
							pToSave.setIdSpecialite(m.getIdSpecialite());
							planningExamenRepository.save(pToSave);
						}
					}
				}
			}
		}
		//!!! gerer le cas en html ou on click sur une checbox deja cocher pour une autre matiere (peut etre lock sur le onchange)
		return "";
	}
	
	private String convertHoraire(String horaire) {
		String[] tabDateDebut = horaire.split("-");
		String dateFin = "";
		if(tabDateDebut[1] == "00") {
			dateFin = tabDateDebut[0] + ":30";
		}
		else {
			int transition = Integer.parseInt(tabDateDebut[0]) + 1;
			dateFin = "" + transition + ":00";
		}
		return dateFin;
	}
	
	@ResponseBody
	@RequestMapping("/getHoraireExcluOfSpecialite")
	public String getHoraireExcluOfSpecialite(
			Model model,
			@RequestParam(name="idSpecialite") String idSpecialite,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaine,
			@RequestParam(name="dateFinSemaine") String dateFinSemaine,
			HttpSession session
			) {
		Examen examen = (Examen)session.getAttribute("Examen");
		if(examen == null) {
			return "examen null";
		}
		Long idSpecialiteConvert = Long.parseLong(idSpecialite);
		List<ListeDateExclu> listHoraireExclu = listeDateExcluRepository.findAllDateExclue(idSpecialiteConvert,examen.getIdExamen());
		String listeHoraire = "";
		LocalDate dateDebutSemaineConvert = LocalDate.parse(dateDebutSemaine);	
		LocalDate dateFinSemaineConvert = LocalDate.parse(dateFinSemaine);
		for(ListeDateExclu l : listHoraireExclu) {
			if((l.getDateJour().isEqual(dateDebutSemaineConvert) || l.getDateJour().isAfter(dateDebutSemaineConvert)) && (l.getDateJour().isEqual(dateFinSemaineConvert) || l.getDateJour().isBefore(dateFinSemaineConvert)))
			listeHoraire += translateDay(l.getDateJour().getDayOfWeek()) + "-" + l.getHoraireDebut() + "-" + l.getHoraireFin() + "¤";
		}
		listeHoraire = listeHoraire.replaceAll("[;]", "");
		model.addAttribute("horaireExclu", listeHoraire);
		return listeHoraire;
	}
	
	
}
