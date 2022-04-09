package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class PetRepositoryImpl implements PetRepository{
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<Pet> findAll() {
        String jpqlAllPets = "SELECT p FROM Pet p";
        // Create a query using the above JPQL
        TypedQuery<Pet> petsQuery = entityManager.createQuery(jpqlAllPets, Pet.class);

        // Run the query and return the results
        try {
            return petsQuery.getResultList();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Pet> findAllByOwnerId(Long ownerId) {
        String jpqlAllPetsByCustomerId = "SELECT c FROM Customer c WHERE c.id = :id";
        // Create a typed query using the named query defined in Pet entity class
        TypedQuery<Customer> allPetsByCustomerIdQuery = entityManager.createQuery(jpqlAllPetsByCustomerId, Customer.class);
        // Set the name parameter to the query
        allPetsByCustomerIdQuery.setParameter("id", ownerId);

        // Run the query and return the results
        try {
            return allPetsByCustomerIdQuery.getSingleResult().getPets();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public Pet findById(Long id) {
        return entityManager.find(Pet.class, id);
    }

    @Override
    public Pet save(Pet pet, Long ownerId) {
        // Persist or merge the new pet
        if (pet.getId() != null) {
            pet = entityManager.merge(pet);
        }
        else {
            entityManager.persist(pet);
        }

        // Link the pet to its owner
        Customer customer = entityManager.find(Customer.class, ownerId);
        pet.setCustomer(customer);

        return pet;
    }

    @Override
    public void delete(Pet pet) {
        if (pet != null){
            if (entityManager.contains(pet)) {
                entityManager.remove(pet);
            }
            else {
                entityManager.remove(entityManager.merge(pet));
            }
        }
    }
}
