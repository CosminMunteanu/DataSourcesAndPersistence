package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String name;

    private String phoneNumber;
    private String notes;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Pet> pets = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void addPet(Pet pet) {
        if(pets.contains(pet)) {
            return;
        }
        // Add the new pet to the pets list
        pets.add(pet);
        // Set this object as the customer in pet
        pet.setCustomer(this);
    }

    public void removePet(Pet pet) {
        if (!pets.contains(pet)) {
            return;
        }
        // Set the customer in pet to null
        pet.setCustomer(null);
        // Remove the deleted pet from the list
        pets.remove(pet);

    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object instanceof Customer) {
            final Customer otherCustomer = (Customer)object;
            if ((id != null) && (otherCustomer.getId() != null)) {
                return id.equals(otherCustomer.getId());
            }
        }
        return false;
    }
}
