package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Trainer;

public interface TrainerRepository extends CrudRepository<Trainer, Integer>{

}