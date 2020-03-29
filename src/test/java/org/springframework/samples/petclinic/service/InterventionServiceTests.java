package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Intervention;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class InterventionServiceTests {

	@Autowired
	protected InterventionService interventionService;
	
	@Autowired
	protected PetService petService;
	
	@Test
	void shouldNotFindInterventionWithIncorrectId() {
		Optional<Intervention> noIntervention = this.interventionService.findInterventionById(-1);
		
		assertThat(noIntervention.isPresent()).isEqualTo(false);
	}
	
	@Test
	void shouldFindInterventionWithCorrectId() {
		Optional<Intervention> intervention = this.interventionService.findInterventionById(1);
		
		assertThat(intervention.isPresent()).isEqualTo(true);
		assertThat(intervention.get().getInterventionDescription()).startsWith("Surgery");
	}
	
	@Test
	void shouldInsertIntervention() {
		Intervention intervention = new Intervention();
		intervention.setInterventionTime(2);
		intervention.setInterventionDate(LocalDate.of(2020, Month.DECEMBER, 24));
		intervention.setInterventionDescription("Test");
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		intervention.setPet(pet);
		
		this.interventionService.saveIntervention(intervention);
		
		assertThat(intervention.getId()).isNotEqualTo(0);
	}
	
	@Test
	void shouldNotInsertInterventionWithDescriptionEmpty() {
		ConstraintViolationException exception;
		Intervention intervention = new Intervention();
		
		intervention.setInterventionDescription("");
		intervention.setInterventionDate(LocalDate.of(2020, Month.DECEMBER, 24));
		intervention.setInterventionTime(2);
		Pet pet = new Pet();
		pet.setName("petTestName");
		pet.setBirthDate(LocalDate.of(2018, Month.AUGUST, 17));
		intervention.setPet(pet);

		exception = assertThrows(ConstraintViolationException.class, () -> this.interventionService.saveIntervention(intervention));
		assertThat(exception.getMessage()).contains("Validation failed");
	}
	
	@Test
	void shouldUpdateInterventionDescription() {
		Optional<Intervention> intervention = this.interventionService.findInterventionById(1);
		String oldDescription, newDescription;
		
		assertThat(intervention.isPresent()).isEqualTo(true);
		oldDescription = intervention.get().getInterventionDescription();
		newDescription = oldDescription + " Test";
		intervention.get().setInterventionDescription(newDescription);
		
		this.interventionService.saveIntervention(intervention.get());
		
		intervention = this.interventionService.findInterventionById(1);
		assertThat(intervention.isPresent()).isEqualTo(true);
		assertThat(intervention.get().getInterventionDescription()).isEqualTo(newDescription);
	}
	
	@Test
	void shouldDeleteIntervention() {
		Optional<Intervention> intervention = this.interventionService.findInterventionById(1);
		
		assertThat(intervention.isPresent()).isEqualTo(true);
		this.interventionService.delete(intervention.get());
		
		intervention = this.interventionService.findInterventionById(1);
		assertThat(intervention.isPresent()).isEqualTo(false);
	}
	
}
