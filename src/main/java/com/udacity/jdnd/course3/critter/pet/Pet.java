package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.user.Customer;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO Test this query
@NamedQuery(
        name = "Pet.findByOwnerId",
        query = "Select p from Pet p where p.customer.id = :id"
)

@NamedQuery(
        name = "Pet.findById",
        query = "Select p from Pet p where p.id = :id"
)

@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Nationalized
    private String name;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany(mappedBy = "petList")
    private final List<Schedule> scheduleList = new ArrayList<>();;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        // Ignore adding same objects to prevent "detached entity passed to persist" exception
        if (Objects.equals(this.customer, customer))
            return;

        // Clear the old customer data
        Customer oldCustomer = this.customer;
        if (oldCustomer != null) {
            oldCustomer.removePet(this);
        }
        // Set the new customer
        this.customer = customer;
        if (customer != null) {
            customer.addPet(this);
        }
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public void addSchedule(Schedule schedule) {
        if(scheduleList.contains(schedule)) {
            return;
        }
        // Add the new pet to the pets list
        scheduleList.add(schedule);
        // Set this object as the customer in pet
        schedule.addPet(this);
    }

    public void removeSchedule(Schedule schedule) {
        if (!scheduleList.contains(schedule)) {
            return;
        }
        // Remove the pet in schedule
        schedule.removePet(this);
        // Remove the deleted pet from the list
        scheduleList.remove(schedule);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object instanceof Pet) {
            final Pet otherPet = (Pet)object;
            if ((id != null) && (otherPet.getId() != null)) {
                return id.equals(otherPet.getId());
            }
        }
        return false;
    }
}
