package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

// Transactional instructs JPA to start a transaction when the method starts,
// and close it at the end, ensuring the flush of the entire operation towards the database
// OR a complete rollback of the change, in case of error
@Transactional
@Repository
public class CustomerRepositoryImpl implements CustomerRepository{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Customer getById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public void persist(Customer customer) {
        // Save or update the customer in DB
        entityManager.persist(customer);
    }

    @Override
    public Customer merge(Customer customer) {
        // Save or update the customer in DB
        return entityManager.merge(customer);
    }

    @Override
    public List<Customer> getAll() {
        String jpqlAllCustomers = "SELECT c FROM Customer c";
        // Create a query using the above JPQL
        TypedQuery<Customer> allCustomersQuery = entityManager.createQuery(jpqlAllCustomers, Customer.class);

        // Run the query and return the results
        try {
            return allCustomersQuery.getResultList();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public Customer getByPetId(Long petId) {
        String jpqlPetById= "SELECT p FROM Pet p where p.id = :id";
        // Create a query using the above JPQL
        TypedQuery<Pet> petsFindByIdQuery = entityManager.createQuery(jpqlPetById, Pet.class);
        // Set the query parameter
        petsFindByIdQuery.setParameter("id", petId);
        // Run the query and return the results
        try {
            return petsFindByIdQuery.getSingleResult().getCustomer();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public void delete(Customer customer) {
        if (customer != null){
            if (entityManager.contains(customer)) {
                entityManager.remove(customer);
            }
            else {
                entityManager.remove(entityManager.merge(customer));
            }
        }
    }
}
