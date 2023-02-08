package com.example.demo.webController;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.dao.PersonneRepository;
import com.example.demo.entities.Personne;

@Controller
@CrossOrigin("*")
@RequestMapping("/Login")
public class LoginController {

	@Autowired
	PersonneRepository personneRepository;
	
	@RequestMapping("/")
	public String indexAccueil() {
		return "indexAccueil";
	}
	
	@RequestMapping("/connexion")
	public String Connexion() {
		return "Login/Connexion";
	}
	
	@RequestMapping("/deconnexion")
	public String deconnexion(HttpSession session) {
		session.setAttribute("compte", "");
		return "Login/deconnexion";
	}
	
	@RequestMapping("/enregistrer")
	public String Enregistrer() {
		return "Login/Enregistrer";
	}
	
	@ResponseBody
	@RequestMapping("/addUser")
	public String addUser(
			@RequestParam(name="name") String name,
			@RequestParam(name="surname") String surname,
			@RequestParam(name="username") String username,
			@RequestParam(name="password") String password,
			@RequestParam(name="type") String type
			) {
		String msg_err = "";
		if(name == "" || username == "" || password== "" || type == "") {
			msg_err = "Un des champs est vide";
		}
		else {
			if(personneRepository.findUser(username) != null) {
				return "username deja pris";
			}
			Personne personne = new Personne(name,surname,username,password,type);
			personneRepository.save(personne);
		}
		
		return msg_err;
	}
	
	@ResponseBody
	@RequestMapping("/findUser")
	public String findUser(
			@RequestParam(name="name") String name,
			@RequestParam(name="password") String password,
			HttpSession session
			) {
		Personne user = personneRepository.findUser(name,password);
		if(user == null) {
			return "Le nom ou le mot de passe est incorrect";
		}
		else {
			session.setAttribute("compte", user);
		}
		return "";
	}
}
