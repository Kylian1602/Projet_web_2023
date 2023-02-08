package com.example.demo.webController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunHttpServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dao.EleveRepository;
import com.example.demo.dao.ExamenRepository;
import com.example.demo.dao.MatiereExamenRepository;
import com.example.demo.dao.PersonneRepository;
import com.example.demo.dao.PlanningExamenRepository;
import com.example.demo.dao.SalleRepository;
import com.example.demo.dao.SpecialiteExamenRepository;
import com.example.demo.dao.SpecialiteRepository;
import com.example.demo.entities.Eleve;
import com.example.demo.entities.Examen;
import com.example.demo.entities.ListeDateExclu;
import com.example.demo.entities.MatiereExamen;
import com.example.demo.entities.Personne;
import com.example.demo.entities.PlanningExamen;
import com.example.demo.entities.Salle;
import com.example.demo.entities.Specialite;
import com.example.demo.entities.SpecialiteExamen;

@Controller
@RequestMapping("/Planning")
public class PlanningController {
	
	@Autowired
	EleveRepository eleveRepository;
	
	@Autowired
	SpecialiteExamenRepository specialiteExamenRepository;
	
	@Autowired
	PlanningExamenRepository planningExamenRepository;
	
	@Autowired
	ExamenRepository examenRepository;
	
	@Autowired
	MatiereExamenRepository matiereExamenRepository;
	
	@Autowired
	PersonneRepository personneRepository;
	
	@Autowired
	SpecialiteRepository specialiteRepository;
	
	@Autowired
	SalleRepository salleRepository;
	
	@RequestMapping("/indexPlanning")
	public String index(Model model, HttpSession session) {
		Personne personne = (Personne)session.getAttribute("compte");
		if(personne == null) {
			return "recharger la session";
		}
		List<SpecialiteExamen> specialiteExamen = null;
		ArrayList<Examen> liste_examen = new ArrayList<>();
		ArrayList<Specialite> liste_specialite = new ArrayList<>();
		if(personne.getType().equals("STU")) {
			Eleve eleve = eleveRepository.getReferenceById(personne.getIdPersonne());
			Long idSpecialite = eleve.getIdSpecialite();
			specialiteExamen = specialiteExamenRepository.findAllExamenForSpecialite(idSpecialite,false);
			if(specialiteExamen != null) {
				for(SpecialiteExamen p : specialiteExamen) {
					Examen e = examenRepository.getReferenceById(p.getIdExamen());
					if(e != null && e.getEtatModification() > 7) {
						liste_examen.add(e);
					}
				}
			}
		}
		else if(personne.getType().equals("ADM")) {
			specialiteExamen = specialiteExamenRepository.findAll();
			if(specialiteExamen != null) {
				specialiteExamen = specialiteExamenRepository.findAllExamenForSpecialite(specialiteExamen.get(0).getIdSpecialite(),false);
				if(specialiteExamen != null) {
					for(SpecialiteExamen p : specialiteExamen) {
						Examen e = examenRepository.getReferenceById(p.getIdExamen());
						if(e != null && e.getEtatModification() > 7) {
							liste_examen.add(e);
						}
					}
				}
			}
			List<Specialite> liste_specialit = specialiteRepository.findAll();
			for(Specialite p : liste_specialit) {
				liste_specialite.add(p);
			}
		}
		
		
		
		Long numberForAffichage = (long)0;
		LocalDate dateDebutExamen = liste_examen.get(0).getDateDebut();
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
		LocalDate dateDebutSemaine = dateDebutExamen.minusDays(numberForAffichage);
		LocalDate dateFinSemaine = dateDebutSemaine.plusDays(6);
		model.addAttribute("dateDebutSemaine", dateDebutSemaine);
		model.addAttribute("dateFinSemaine", dateFinSemaine);
		model.addAttribute("idExamen", liste_examen.get(0).getIdExamen());
		model.addAttribute("specialites", liste_specialite);
		model.addAttribute("examens", liste_examen);
		model.addAttribute("type", personne.getType());
		return "Planning/indexPlanning";
	}
	
	@ResponseBody
	@RequestMapping("/getHoraire")
	public String getHoraire(Model model,
			HttpSession session,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaineForm,
			@RequestParam(name="dateFinSemaine") String dateFinSemaineForm,
			@RequestParam(name="buttonClick") String buttonClick,
			@RequestParam(name="idExamen") String idExamen
			) {
		Long numberForAffichage = (long) 0;
		LocalDate dateDebutSemaine = null;
		LocalDate dateFinSemaine = null;
		if(buttonClick.equals("")) {
			Long idExamenConvert = Long.parseLong(idExamen);
			Examen exam = examenRepository.findByIdExamen(idExamenConvert);
			switch(exam.getDateDebut().getDayOfWeek()) {
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
			dateDebutSemaine = exam.getDateDebut().minusDays(numberForAffichage);
			dateFinSemaine = dateDebutSemaine.plusDays(6);
		}
		//si passe a la semaine suivante
		if(buttonClick.equals("Up")) {
			dateDebutSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(1);
			dateFinSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(7);
		}
		//si on passe a la semaine précédente
		if(buttonClick.equals("Down")) {
			dateDebutSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(7);
			dateFinSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(1);
			model.addAttribute("dateDebutSemaine", dateDebutSemaine);
			model.addAttribute("dateFinSemaine", dateFinSemaine);
		}
		model.addAttribute("dateDebutSemaine", dateDebutSemaine);
		model.addAttribute("dateFinSemaine", dateFinSemaine);
		return dateDebutSemaine + "¤" + dateFinSemaine;
	}
	
	@ResponseBody
	@RequestMapping("/getMatiere")
	public String getMatiere(
			HttpSession session,
			@RequestParam(name="idSpecialite") String idSpecialiteForm
			) {
		Long idSpecialite = Long.parseLong(idSpecialiteForm);
		ArrayList<Examen> liste_examen = new ArrayList<>();
		List<SpecialiteExamen> specialiteExamen = specialiteExamenRepository.findAllExamenForSpecialite(idSpecialite,false);
		if(specialiteExamen != null) {
			for(SpecialiteExamen p : specialiteExamen) {
				Examen e = examenRepository.getReferenceById(p.getIdExamen());
				if(e != null && e.getEtatModification() > 7) {
					liste_examen.add(e);
				}
			}
		}
		String option = "";
		for(Examen e : liste_examen) {
			option += "<option value=" + "" + e.getIdExamen() + "" + ">"+e.getName()+"</option>";
		}
		return option;
	}

	@ResponseBody
	@RequestMapping("/getPlanning")
	public String getPlanning(
			Model model,
			HttpSession session,
			@RequestParam(name="dateDebutSemaine") String dateDebutSemaineForm,
			@RequestParam(name="dateFinSemaine") String dateFinSemaineForm,
			@RequestParam(name="idExamen") String idExamenForm,
			@RequestParam(name="idSpecialite") String idSpecialiteForm,
			@RequestParam(name="buttonClick") String buttonClick
			) {
		Personne personne = (Personne)session.getAttribute("compte");
		if(personne == null) {
			return "recharger la session";
		}
		else {
			//mettre les horaires inchangé
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
				model.addAttribute("horaires",List_horaire);
			}
			switch(personne.getType()) {
			case "STU":
				Eleve eleve = eleveRepository.getReferenceById(personne.getIdPersonne());
				Long idSpecialite = eleve.getIdSpecialite();
				List<SpecialiteExamen> specialiteExamen = specialiteExamenRepository.findAllExamenForSpecialite(idSpecialite,false);
				if(specialiteExamen == null || specialiteExamen.size() == 0 ) {
					return "vous n'avez pas d'examen de prevue";
				}
				Long idExamen = Long.parseLong(idExamenForm); 
				Examen examen = examenRepository.getReferenceById(idExamen);
				Long numberForAffichage = (long) 0;
				//List<PlanningExamen> liste_planning = planningExamenRepository.findPlanningOfSpecialite(idExamen,idSpecialite);
				List<MatiereExamen> liste_matiere = matiereExamenRepository.findAllMatiereForExamen(idSpecialite, idExamen);
				LocalDate dateDebut;
				LocalDate dateFin;
				LocalDate dateDebutSemaine = null;
				LocalDate dateFinSemaine = null;
				if(buttonClick.equals("")) {
					dateDebut = examen.getDateDebut();
					switch(dateDebut.getDayOfWeek()) {
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
					dateDebutSemaine = dateDebut.minusDays(numberForAffichage);
					dateFinSemaine = dateDebutSemaine.plusDays(6);
				}
				//si passe a la semaine suivante
				if(buttonClick.equals("Up")) {
					dateDebutSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(1);
					dateFinSemaine = LocalDate.parse(dateFinSemaineForm).plusDays(7);
					model.addAttribute("dateDebutSemaine", dateDebutSemaine);
					model.addAttribute("dateFinSemaine", dateFinSemaine);
					//verifier si la semaine appartien a l'examen
					if(dateDebutSemaine.isAfter(examen.getDateFin())) {
						return "La semaine n'appartient pas a l'examen";
					}
				}
				//si on passe a la semaine précédente
				if(buttonClick.equals("Down")) {
					dateDebutSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(7);
					dateFinSemaine = LocalDate.parse(dateDebutSemaineForm).minusDays(1);
					model.addAttribute("dateDebutSemaine", dateDebutSemaine);
					model.addAttribute("dateFinSemaine", dateFinSemaine);
					//verifier si la semaine appartien a l'examen
					if(dateFinSemaine.isBefore(examen.getDateDebut())) {
						return "La semaine n'appartient pas a l'examen";
					}
				}
				String tableau = "";
				tableau += "<tr>";
 					tableau += "<th></th>";
					tableau += "<th>Lundi</th>";
					tableau += "<th>Mardi</th>";
					tableau += "<th>Mercredi</th>";
					tableau += "<th>Jeudi</th>";
					tableau += "<th>Vendredi</th>";
					tableau += "<th>Samedi</th>";
					tableau += "<th>Dimanche</th>";
				tableau += "</tr>";
				LocalDate dateDuJour = dateDebutSemaine;
				//pour chaque horaire
				for(ListeDateExclu l : List_horaire) {
					tableau += "<tr>";
					tableau += "<td>" + l.getHoraireDebut()  + "-" + l.getHoraireFin() + "</td>";
					//pour chaque jour
					for(int i = 0; i<7; i++) {
						boolean tdIsEmpty = true;
						for(MatiereExamen e : liste_matiere) {
							if((l.getHoraireDebut()).equals(e.getHeureDebut()) && dateDuJour.isEqual(e.getDateDebut())) {
								tdIsEmpty = false;
								int countRowspawn = 1;
								boolean verif = false;
								String[] tabHoraireDebut = e.getHeureDebut().split("-");
								while(!verif) {
									String horaireToTest = "";
									if(tabHoraireDebut[1].equals("00")) {
										horaireToTest = tabHoraireDebut[0] + "-30";
									}
									else {
										int intermediaire = Integer.parseInt(tabHoraireDebut[0]) + 1;
										horaireToTest = "" + intermediaire + "-00";
									}
									if(horaireToTest.equals(e.getHeureFin())) {
										verif = true;
									}
									else {
										countRowspawn += 1;
										tabHoraireDebut[0] = horaireToTest.split("-")[0];
										tabHoraireDebut[1] = horaireToTest.split("-")[1];
									}
								}
								MatiereExamen matiere = matiereExamenRepository.getReferenceById(e.getId());
								String nomMatiere = "";
								if(matiere != null && !matiere.getNom().equals("")) {
									nomMatiere = matiere.getNom();
								}
								Personne pers = personneRepository.findUser(e.getIdSurveillant());
								String nomSurveillant = "";
								if(pers != null && !pers.getUsername().equals("")) {
									nomSurveillant = pers.getUsername();
								}
								Salle salle = salleRepository.findSalle(e.getIdSalle());
								String nomSalle = "";
								if(salle != null && !salle.getNumero().equals("")) {
									nomSalle = salle.getNumero();
								}
								tableau += "<td style='border:solid;width:150px;' rowspan=" + countRowspawn + "> matiere : " + nomMatiere + " surveillant : " + nomSurveillant + " salle : " + nomSalle + "</td>";
							}
						}
						if(tdIsEmpty) {
							tableau += "<td></td>";
						}
						if(dateDuJour.equals(dateFinSemaine)) {
							dateDuJour = dateDebutSemaine;
						}
						else {
							dateDuJour = dateDuJour.plusDays(1);
						}
					}
					tableau += "</tr>";
				}
				
					return tableau;
			case "ADM":
				Long idSpecialiteConvert2 = Long.parseLong(idSpecialiteForm);
				Long idExamen2 = Long.parseLong(idExamenForm); 
				Examen examen2 = examenRepository.getReferenceById(idExamen2);
				Long numberForAffichage2 = (long) 0;
				List<MatiereExamen> liste_matiere2 = matiereExamenRepository.findAllMatiereForExamen(idSpecialiteConvert2, idExamen2);
				LocalDate dateDebut2;
				LocalDate dateFin2;
				LocalDate dateDebutSemaine2 = null;
				LocalDate dateFinSemaine2 = null;
				if(buttonClick.equals("")) {
					dateDebut2 = examen2.getDateDebut();
					switch(dateDebut2.getDayOfWeek()) {
					case TUESDAY :
						numberForAffichage2 = (long) 1;
						break;
					case WEDNESDAY :
						numberForAffichage2 = (long) 2;
						break;
					case THURSDAY :
						numberForAffichage2 = (long) 3;
						break;
					case FRIDAY :
						numberForAffichage2 = (long) 4;
						break;
					case SATURDAY :
						numberForAffichage2 = (long) 5;
						break;
					case SUNDAY :
						numberForAffichage2 = (long) 6;
						break;
					}
					dateDebutSemaine2 = dateDebut2.minusDays(numberForAffichage2);
					dateFinSemaine2 = dateDebutSemaine2.plusDays(6);
				}
				//si passe a la semaine suivante
				if(buttonClick.equals("Up")) {
					dateDebutSemaine2 = LocalDate.parse(dateFinSemaineForm).plusDays(1);
					dateFinSemaine2 = LocalDate.parse(dateFinSemaineForm).plusDays(7);
					model.addAttribute("dateDebutSemaine", dateDebutSemaine2);
					model.addAttribute("dateFinSemaine", dateFinSemaine2);
					//verifier si la semaine appartien a l'examen
					if(dateDebutSemaine2.isAfter(examen2.getDateFin())) {
						return "La semaine n'appartient pas a l'examen";
					}
				}
				//si on passe a la semaine précédente
				if(buttonClick.equals("Down")) {
					dateDebutSemaine2 = LocalDate.parse(dateDebutSemaineForm).minusDays(7);
					dateFinSemaine2 = LocalDate.parse(dateDebutSemaineForm).minusDays(1);
					model.addAttribute("dateDebutSemaine", dateDebutSemaine2);
					model.addAttribute("dateFinSemaine", dateFinSemaine2);
					//verifier si la semaine appartien a l'examen
					if(dateFinSemaine2.isBefore(examen2.getDateDebut())) {
						return "La semaine n'appartient pas a l'examen";
					}
				}
				String tableau2 = "";
				tableau2 += "<tr>";
 					tableau2 += "<th></th>";
					tableau2 += "<th>Lundi</th>";
					tableau2 += "<th>Mardi</th>";
					tableau2 += "<th>Mercredi</th>";
					tableau2 += "<th>Jeudi</th>";
					tableau2 += "<th>Vendredi</th>";
					tableau2 += "<th>Samedi</th>";
					tableau2 += "<th>Dimanche</th>";
				tableau2 += "</tr>";
				LocalDate dateDuJour2 = dateDebutSemaine2;
				//pour chaque horaire
				for(ListeDateExclu l : List_horaire) {
					tableau2 += "<tr>";
					tableau2 += "<td>" + l.getHoraireDebut()  + "-" + l.getHoraireFin() + "</td>";
					//pour chaque jour
					for(int i = 0; i<7; i++) {
						boolean tdIsEmpty = true;
						for(MatiereExamen e : liste_matiere2) {
							if((l.getHoraireDebut()).equals(e.getHeureDebut()) && dateDuJour2.isEqual(e.getDateDebut())) {
								tdIsEmpty = false;
								int countRowspawn = 1;
								boolean verif = false;
								String[] tabHoraireDebut = e.getHeureDebut().split("-");
								while(!verif) {
									String horaireToTest = "";
									if(tabHoraireDebut[1].equals("00")) {
										horaireToTest = tabHoraireDebut[0] + "-30";
									}
									else {
										int intermediaire = Integer.parseInt(tabHoraireDebut[0]) + 1;
										horaireToTest = "" + intermediaire + "-00";
									}
									if(horaireToTest.equals(e.getHeureFin())) {
										verif = true;
									}
									else {
										countRowspawn += 1;
										tabHoraireDebut[0] = horaireToTest.split("-")[0];
										tabHoraireDebut[1] = horaireToTest.split("-")[1];
									}
								}
								MatiereExamen matiere = matiereExamenRepository.getReferenceById(e.getId());
								String nomMatiere = "";
								if(matiere != null && !matiere.getNom().equals("")) {
									nomMatiere = matiere.getNom();
								}
								Personne pers = personneRepository.findUser(e.getIdSurveillant());
								String nomSurveillant = "";
								if(pers != null && !pers.getUsername().equals("")) {
									nomSurveillant = pers.getUsername();
								}
								Salle salle = salleRepository.findSalle(e.getIdSalle());
								String nomSalle = "";
								if(salle != null && !salle.getNumero().equals("")) {
									nomSalle = salle.getNumero();
								}
								tableau2 += "<td style='border:solid;width:150px;' rowspan=" + countRowspawn + "> matiere : " + nomMatiere + " surveillant : " + nomSurveillant + " salle : " + nomSalle + "</td>";
							}
						}
						if(tdIsEmpty) {
							tableau2 += "<td></td>";
						}
						if(dateDuJour2.equals(dateFinSemaine2)) {
							dateDuJour2 = dateDebutSemaine2;
						}
						else {
							dateDuJour2 = dateDuJour2.plusDays(1);
						}
					}
					tableau2 += "</tr>";
				}
				return tableau2;
			}
		}
		return "";
	}
}
