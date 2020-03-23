/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Rehab;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.RehabRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private PetRepository			petRepository;

	private VisitRepository			visitRepository;

	private RehabRepository	        rehabRepository;


	@Autowired
	public PetService(final PetRepository petRepository, final VisitRepository visitRepository, final RehabRepository rehabRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.rehabRepository = rehabRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepository.findPetTypes();
	}

	@Transactional
	public void saveVisit(final Visit visit) throws DataAccessException {
		this.visitRepository.save(visit);
	}

	@Transactional(readOnly = true)
	public Pet findPetById(final int id) throws DataAccessException {
		return this.petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void savePet(final Pet pet) throws DataAccessException, DuplicatedPetNameException {
		Pet otherPet = new Pet();
		if (pet.getOwner() != null && StringUtils.hasLength(pet.getName()) && otherPet != null && otherPet.getId() != pet.getId()) {
			otherPet = pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
			throw new DuplicatedPetNameException();
		} else {
			this.petRepository.save(pet);
		}
	}

	public Collection<Visit> findVisitsByPetId(final int petId) {
		return this.visitRepository.findByPetId(petId);
	}

	//This method allows us to find all homeless pets
	public List<Pet> findHomelessPets() throws DataAccessException {
		return this.petRepository.findHomelessPets();
	}

	//This method allows us to delete a given pet
	public void deletePet(final Pet pet) throws DataAccessException {
		this.petRepository.delete(pet);
	}

	@Transactional
	public void saveRehab(final Rehab rehab) throws DataAccessException {
		this.rehabRepository.save(rehab);
	}


}

