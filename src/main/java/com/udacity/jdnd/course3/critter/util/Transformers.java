package com.udacity.jdnd.course3.critter.util;

import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Transformers {
    public static Set<EmployeeSkill> getSkillsFromBlob(byte[] skills) {
        // Sanity check
        if (skills == null) {
            return null;
        }

        Set<EmployeeSkill> employeeSkills = new HashSet<>();
        String skillsStringCSV = new String(skills);
        List<String> skillsList = Arrays.asList(skillsStringCSV.split(","));
        for (String employeeStringSkill : skillsList) {
            try {
                employeeSkills.add(EmployeeSkill.valueOf(employeeStringSkill));
            } catch (java.lang.IllegalArgumentException e) {

            }
        }

        return employeeSkills;
    }

    public static byte[] getBlobFromSkills(Set<EmployeeSkill> employeeSkills) {
        if (employeeSkills == null) {
            return null;
        }

        StringBuilder employeeSkillsStringBuilder =  new StringBuilder();

        for (EmployeeSkill skill: employeeSkills) {
            employeeSkillsStringBuilder.append(skill.name());
            employeeSkillsStringBuilder.append(",");
        }

        return employeeSkillsStringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static Set<DayOfWeek> getAvailabilityFromBlob(byte[] availability) {
        // Sanity check
        if (availability == null) {
            return null;
        }

        Set<DayOfWeek> employeeAvailability = new HashSet<>();
        String availabilityStringCSV = new String(availability);
        List<String> skillsList = Arrays.asList(availabilityStringCSV.split(","));
        for (String employeeStringSkill : skillsList) {
            try {
                employeeAvailability.add(DayOfWeek.valueOf(employeeStringSkill));
            } catch (java.lang.IllegalArgumentException e) {

            }
        }

        return employeeAvailability;
    }

    public static byte[] getBlobFromAvailability(Set<DayOfWeek> employeeAvailability) {
        if (employeeAvailability == null) {
            return null;
        }

        StringBuilder employeeAvailabilityStringBuilder =  new StringBuilder();

        for (DayOfWeek day: employeeAvailability) {
            employeeAvailabilityStringBuilder.append(day.name());
            employeeAvailabilityStringBuilder.append(",");
        }

        return employeeAvailabilityStringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
