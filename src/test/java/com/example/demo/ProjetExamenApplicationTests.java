package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entities.Examen;
import com.example.demo.webController.ExamenController;

@SpringBootTest
class ProjetExamenApplicationTests {

	@Test
	void contextLoads() {
		ExamenController controller = new ExamenController();
		assertThat(controller).isNotNull();
	}
	
	public class ExamenTest {

	    @Test
	    public void testGettersAndSetters() {
	        String name = "Math Exam";
	        LocalDate dateDebut = LocalDate.of(2022, 12, 1);
	        LocalDate dateFin = LocalDate.of(2022, 12, 15);
	        int etatModification = 0;

	        Examen examen = new Examen();
	        examen.setName(name);
	        examen.setDateDebut(dateDebut);
	        examen.setDateFin(dateFin);
	        examen.setEtatModification(etatModification);

	        assertEquals(name, examen.getName());
	        assertEquals(dateDebut, examen.getDateDebut());
	        assertEquals(dateFin, examen.getDateFin());
	        assertEquals(etatModification, examen.getEtatModification());
	    }

	    @Test
	    public void testConstructor() {
	        String name = "Math Exam";
	        LocalDate dateDebut = LocalDate.of(2022, 12, 1);
	        LocalDate dateFin = LocalDate.of(2022, 12, 15);
	        int etatModification = 0;

	        Examen examen = new Examen(name, dateDebut, dateFin, etatModification);

	        assertEquals(name, examen.getName());
	        assertEquals(dateDebut, examen.getDateDebut());
	        assertEquals(dateFin, examen.getDateFin());
	        assertEquals(etatModification, examen.getEtatModification());
	    }
	}

}
