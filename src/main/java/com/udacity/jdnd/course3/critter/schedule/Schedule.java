package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NamedQuery(
        name = "Schedule.findByEmployeeId",
        query = "Select s from Schedule s join s.employeeList e where e.id = :id"
)

@NamedQuery(
        name = "Schedule.findByPetId",
        query = "Select s from Schedule s join s.petList p where p.id = :id"
)

@Entity
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "schedule_employee",
            joinColumns = @JoinColumn(name = "schedule_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName="id", insertable = false, updatable = false)
    )
    private final List<Employee> employeeList = new ArrayList<>();;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "schedule_pet",
            joinColumns = @JoinColumn(name = "schedule_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id", referencedColumnName="id", insertable = false, updatable = false)
    )
    private final List<Pet> petList = new ArrayList<>();;

    @Lob
    private byte[] activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public byte[] getActivity() {
        return activity;
    }

    public void setActivity(byte[] activity) {
        this.activity = activity;
    }

    public void addPet(Pet pet) {
        if(petList.contains(pet)) {
            return;
        }
        // Add the new pet to the pets list
        petList.add(pet);
        // Add this schedule in pet
        pet.addSchedule(this);

    }

    public void removePet(Pet pet) {
        if (!petList.contains(pet)) {
            return;
        }
        // Remove the schedule in pet
        pet.removeSchedule(this);
        // Remove the deleted pet from the list
        petList.remove(pet);
    }

    public void addEmployee(Employee employee) {
        if(employeeList.contains(employee)) {
            return;
        }
        // Add the new employee to the employees list
        employeeList.add(employee);
        // Add this schedule in employee
        employee.addSchedule(this);

    }

    public void removeEmployee(Employee employee) {
        if (!employeeList.contains(employee)) {
            return;
        }
        // Remove the deleted employee from the list
        employeeList.remove(employee);
        // Remove the schedule in employee
        employee.removeSchedule(null);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object instanceof Schedule) {
            final Schedule otherSchedule = (Schedule)object;
            if ((id != null) && (otherSchedule.getId() != null)) {
                return id.equals(otherSchedule.getId());
            }
        }
        return false;
    }
}
