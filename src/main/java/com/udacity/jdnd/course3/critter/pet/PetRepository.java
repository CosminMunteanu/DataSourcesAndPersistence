package com.udacity.jdnd.course3.critter.pet;

import java.util.List;

public interface PetRepository {
    List<Pet> findAll();
    List<Pet> findAllByOwnerId(Long ownerId);
    Pet findById(Long id);
    Pet save(Pet pet, Long ownerId);
    void delete(Pet pet);
}
