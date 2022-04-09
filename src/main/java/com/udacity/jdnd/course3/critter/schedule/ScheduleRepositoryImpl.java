package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Repository
public class ScheduleRepositoryImpl implements ScheduleRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Schedule save(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        // Persist or merge the new schedule
        if (schedule.getId() != null){
            schedule = entityManager.merge(schedule);
        }
        else {
            entityManager.persist(schedule);
        }

        // Lambda needs a final object
        Schedule finalSchedule = schedule;
        employeeIds.forEach(employeeId -> {
            Employee employee = entityManager.find(Employee.class, employeeId);
            // Link the pet to the schedule
            employee.addSchedule(finalSchedule);
        });

        petIds.forEach(petId -> {
            Pet pet = entityManager.find(Pet.class, petId);
            // Link the pet to the schedule
            pet.addSchedule(finalSchedule);
        });

        return finalSchedule;
    }

    @Override
    public Schedule getById(Long id) {
        return entityManager.find(Schedule.class, id);
    }

    @Override
    public List<Schedule> findAll() {
        String jpqlAllSchedules = "SELECT s FROM Schedule s";
        // Create a query using the above JPQL
        TypedQuery<Schedule> allSchedulesQuery = entityManager.createQuery(jpqlAllSchedules, Schedule.class);

        // Run the query and return the results
        try {
            return allSchedulesQuery.getResultList();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Schedule> findAllByPetId(Long petId) {
        // Create a typed query using the named query defined in Pet entity class
        TypedQuery<Schedule> allPetsQuery = entityManager.createNamedQuery("Schedule.findByPetId", Schedule.class);
        // Set the name parameter to the query
        allPetsQuery.setParameter("id", petId);

        // Run the query and return the results
        try {
            return allPetsQuery.getResultList();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Schedule> getByEmployeeId(Long employeeId) {
        // Create a typed query using the named query defined in Pet entity class
        TypedQuery<Schedule> allEmployeesQuery = entityManager.createNamedQuery("Schedule.findByEmployeeId", Schedule.class);
        // Set the name parameter to the query
        allEmployeesQuery.setParameter("id", employeeId);

        // Run the query and return the results
        try {
            return allEmployeesQuery.getResultList();
        }
        catch (javax.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Schedule> findAllByCustomerId(Long customerId) {
        // Create a typed query using the named query defined in Pet entity class
        TypedQuery<Pet> allPetsByCustomerIdQuery = entityManager.createNamedQuery("Pet.findByOwnerId", Pet.class);
        // Set the name parameter to the query
        allPetsByCustomerIdQuery.setParameter("id", customerId);

        // Run the query and return the results
        try {
            List<Schedule> allSchedulesForCustomer = new ArrayList<>();
            List<Pet> allPetsByCustomerId = allPetsByCustomerIdQuery.getResultList();
            for (Pet pet: allPetsByCustomerId) {
                if (pet.getScheduleList() != null) {
                    allSchedulesForCustomer.addAll(pet.getScheduleList());
                }
            }

            return allSchedulesForCustomer;
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
}
