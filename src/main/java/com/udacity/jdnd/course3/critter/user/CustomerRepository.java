package com.udacity.jdnd.course3.critter.user;

import java.util.List;

public interface CustomerRepository {

    Customer getById(Long id);
    void persist(Customer customer);
    Customer merge(Customer customer);
    List<Customer> getAll();
    Customer getByPetId(Long petId);
    void delete(Customer customer);
}
