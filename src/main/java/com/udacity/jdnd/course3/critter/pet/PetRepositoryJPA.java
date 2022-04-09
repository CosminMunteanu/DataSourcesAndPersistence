package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface PetRepositoryJPA extends JpaRepository<Pet, Long> {
//    @Query("select p.delivery.completed from Plant p where p.id = :plantId")
//    Boolean deliveryCompleted(Long plantId);

    @Query("select c from Customer c where c.id = :ownerId")
    Customer getCustomerByOwnerId(Long ownerId);

    @Query("select c.pets from Customer c where c.id = :ownerId")
    List<Pet> findAllByOwnerId(Long ownerId);
}
