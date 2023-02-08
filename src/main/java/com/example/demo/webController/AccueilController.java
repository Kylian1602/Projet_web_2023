package com.example.demo.webController;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Personne;

@Controller
public class AccueilController {

	@RequestMapping("/")
	public String indexAccueil() {
		return "indexAccueil";
	}
	
	@ResponseBody
	@RequestMapping("/verifConnexion")
	public String verifConnexion(HttpSession session) {
		Personne p = null;
		if(session.getAttribute("compte") != null && session.getAttribute("compte") != "" ) {
			p = (Personne)session.getAttribute("compte");
		}
		if(p == null) {
			return "";
		}
		else {
			return p.getType() + ";" + p.getUsername();
		}
	}
	
	@RequestMapping("/connexion")
	public String Connexion() {
		return "Login/Connexion";
	}
}
