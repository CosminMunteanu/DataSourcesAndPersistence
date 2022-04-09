package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.util.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        // Sanity check
        if (employeeDTO == null) {
            return null;
        }

        // Create a new customer entity from the DTO
        Employee employee = convertEmployeeDTOToEntity(employeeDTO);
        // Persist or merge the customer in the DB
        employee = employeeRepository.save(employee);

        return convertEmployeeEntityToDTO(employee);
    }

    public EmployeeDTO getById(Long id) {
        return convertEmployeeEntityToDTO(employeeRepository.findById(id).orElse(null));
    }

    void setAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee != null) {
            employee.setAvailability(Transformers.getBlobFromAvailability(daysAvailable));
            // Persist or merge the customer in the DB
            employee = employeeRepository.save(employee);
        }

    }

    List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO) {
        if ((employeeRequestDTO == null) || (employeeRequestDTO.getDate() == null)) {
            return null;
        }

        // Define predicates
        Predicate<Employee> isAvailableWhenNeeded = employee -> employee.isAvailable(employeeRequestDTO.getDate().getDayOfWeek());
        Predicate<Employee> hasTheRightSkills = employee -> {
            if (employeeRequestDTO.getSkills() == null) {
                return true;
            }

            return Transformers.getSkillsFromBlob(employee.getSkills()).containsAll(employeeRequestDTO.getSkills());
        };

        // Run the query and filter the results
        List<Employee> allAvailableEmployees = employeeRepository.findAll().stream()
                .filter(isAvailableWhenNeeded.and(hasTheRightSkills))
                .collect(Collectors.toList());

        // Build the response
        if (allAvailableEmployees.isEmpty()) {
            return null;
        }
        List<EmployeeDTO> employeesForServiceDTO = new ArrayList<>();
        for (Employee availableEmployee: allAvailableEmployees) {
            employeesForServiceDTO.add(convertEmployeeEntityToDTO(availableEmployee));
        }

        return employeesForServiceDTO;
    }

    private static EmployeeDTO convertEmployeeEntityToDTO(Employee employeeEntity) {
        // Sanity check
        if (employeeEntity == null) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();
        // Copy the same name properties from Entity to DTO
        BeanUtils.copyProperties(employeeEntity, employeeDTO);
        // Set the other fields
        employeeDTO.setDaysAvailable(Transformers.getAvailabilityFromBlob(employeeEntity.getAvailability()));
        employeeDTO.setSkills(Transformers.getSkillsFromBlob(employeeEntity.getSkills()));

        return employeeDTO;
    }

    private static Employee convertEmployeeDTOToEntity(EmployeeDTO employeeDTO) {
        // Sanity check
        if (employeeDTO == null) {
            return null;
        }

        Employee employeeEntity = new Employee();
        BeanUtils.copyProperties(employeeDTO, employeeEntity);
        // Set the other fields
        employeeEntity.setAvailability(Transformers.getBlobFromAvailability(employeeDTO.getDaysAvailable()));
        employeeEntity.setSkills(Transformers.getBlobFromSkills(employeeDTO.getSkills()));

        return employeeEntity;
    }
}
