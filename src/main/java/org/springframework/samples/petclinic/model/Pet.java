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

package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate			birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType				type;

	@ManyToOne(optional = true)
	@JoinColumn(name = "owner_id")
	private Owner				owner;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Visit>			visits;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Intervention>	interventions;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Rehab> rehabs;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Adoption> adoptions;


	public void setBirthDate(final LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public PetType getType() {
		return this.type;
	}

	public void setType(final PetType type) {
		this.type = type;
	}

	public Owner getOwner() {
		return this.owner;
	}

	protected void setOwner(final Owner owner) {
		this.owner = owner;
	}

		
	protected Set<Visit> getVisitsInternal() {
		if (this.visits == null) {
			this.visits = new HashSet<>();
		}
		return this.visits;
	}

	protected void setVisitsInternal(final Set<Visit> visits) {
		this.visits = visits;
	}

	public List<Visit> getVisits() {
		List<Visit> sortedVisits = new ArrayList<>(this.getVisitsInternal());
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(final Visit visit) {
		this.getVisitsInternal().add(visit);
		visit.setPet(this);
	}


	public void removeVisit(final Visit visit) {
		this.getVisitsInternal().remove(visit);
	}

	protected Set<Intervention> getInterventionsInternal() {
		if (this.interventions == null) {
			this.interventions = new HashSet<>();
		}
		return this.interventions;
	}

	public List<Intervention> getInterventions() {
		List<Intervention> sortedInterventions = new ArrayList<>(this.getInterventionsInternal());
		PropertyComparator.sort(sortedInterventions, new MutableSortDefinition("interventionDate", false, false));
		return Collections.unmodifiableList(sortedInterventions);
	}
	
	protected void setInterventionsInternal(final Set<Intervention> interventions) {
		this.interventions = interventions;
	}

	public void addIntervention(final Intervention intervention) {
		this.getInterventionsInternal().add(intervention);
		intervention.setPet(this);
	}

	public void removeIntervention(final Intervention intervention) {
		this.getInterventionsInternal().remove(intervention);
	}

	public void addRehab(Rehab rehab) {
		getRehabsInternal().add(rehab);
		rehab.setPet(this);
	}
	
	protected void setRehabsInternal(Set<Rehab> rehabs) {
		this.rehabs = rehabs;
	}
	 
	protected Set<Rehab> getRehabsInternal() {
		if (this.rehabs == null) {
			this.rehabs = new HashSet<>();
		}
		return this.rehabs;
	}

	public List<Rehab> getRehabs() {
		List<Rehab> sortedRehabs = new ArrayList<>(getRehabsInternal());
		PropertyComparator.sort(sortedRehabs, new MutableSortDefinition("rehabdate", false, false));
		return Collections.unmodifiableList(sortedRehabs);
	}
	
	public void removeRehab(final Rehab rehab) {
		this.getRehabsInternal().remove(rehab);
	}
  
}
