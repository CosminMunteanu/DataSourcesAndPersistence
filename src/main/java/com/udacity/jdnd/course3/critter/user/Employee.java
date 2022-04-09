package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.schedule.Schedule;
import com.udacity.jdnd.course3.critter.util.Transformers;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.*;

@NamedQuery(
        name = "Employee.findById",
        query = "Select e from Employee e where e.id = :id"
)

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String name;

    @Lob
    private byte[] skills;
    @Lob
    private byte[] availability;

    @ManyToMany(mappedBy = "employeeList")
    private final List<Schedule> scheduleList = new ArrayList<>();;

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

    public byte[] getSkills() {
        return skills;
    }

    public void setSkills(byte[] skills) {
        this.skills = skills;
    }

    public byte[] getAvailability() {
        return availability;
    }

    public void setAvailability(byte[] availability) {
        this.availability = availability;
    }

    public boolean hasSkill(EmployeeSkill skill) {
        if (skills == null) {
            return false;
        }
        return Transformers.getSkillsFromBlob(skills).contains(skill);
    }

    public boolean isAvailable(DayOfWeek day) {
        if (availability == null) {
            return true;
        }
        return Transformers.getAvailabilityFromBlob(availability).contains(day);
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
        schedule.addEmployee(this);
    }

    public void removeSchedule(Schedule schedule) {
        if (!scheduleList.contains(schedule)) {
            return;
        }
        // Set the schedule in pet to null
        schedule.removeEmployee(this);
        // Remove the deleted pet from the list
        scheduleList.remove(schedule);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object instanceof Employee) {
            final Employee otherEmployee = (Employee)object;
            if ((id != null) && (otherEmployee.getId() != null)) {
                return id.equals(otherEmployee.getId());
            }
        }
        return false;
    }
}
