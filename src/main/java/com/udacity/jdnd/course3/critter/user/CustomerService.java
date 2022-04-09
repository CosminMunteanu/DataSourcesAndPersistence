package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.pet.Pet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public CustomerDTO save(CustomerDTO customerDTO) {
        // Sanity check
        if (customerDTO == null) {
            return null;
        }

        // Create a new customer entity from the DTO
        Customer customer = convertPetDTOToEntity(customerDTO);
        // Persist or merge the customer in the DB
        if (customer.getId() != null) {
            customer = customerRepository.merge(customer);
        }
        else {
            customerRepository.persist(customer);
        }

        return convertPetEntityToDTO(customer);
    }

    public List<CustomerDTO> getAll() {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerRepository.getAll().forEach(cust -> {
            customerDTOList.add(convertPetEntityToDTO(cust));
        });

        return customerDTOList;
    }

    public CustomerDTO getByPetId(long petId) {
        return convertPetEntityToDTO(customerRepository.getByPetId(petId));
    }

    private static CustomerDTO convertPetEntityToDTO(Customer customerEntity) {
        // Sanity check
        if (customerEntity == null) {
            return null;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        List<Long> petIds = new ArrayList<>();
        // Copy the same name properties from Entity to DTO
        BeanUtils.copyProperties(customerEntity, customerDTO);
        for (Pet pet : customerEntity.getPets()) {
            petIds.add(pet.getId());
        }
        customerDTO.setPetIds(petIds);
        return customerDTO;
    }

    private static Customer convertPetDTOToEntity(CustomerDTO customerDTO) {
        // Sanity check
        if (customerDTO == null) {
            return null;
        }

        Customer customerEntity = new Customer();
        BeanUtils.copyProperties(customerDTO, customerEntity);
        return customerEntity;
    }
}
