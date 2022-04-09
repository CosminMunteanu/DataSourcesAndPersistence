package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public PetDTO save(PetDTO petDTO) {
        // Sanity check
        if (petDTO == null) {
            return null;
        }
        // Create a new pet entity from the DTO
        Pet pet = convertPetDTOToEntity(petDTO);
        //Save the new pet in DB
        pet = petRepository.save(pet, petDTO.getOwnerId());

        return convertPetEntityToDTO(pet);
    }

    public PetDTO getPetById(long id) {
        return convertPetEntityToDTO(petRepository.findById(id));
    }

    public List<PetDTO> getAll() {
        List<Pet> petList = petRepository.findAll();
        List<PetDTO> petDTOList = new ArrayList<>();
        // Prepare the DTOs if the query returns something
        if (petList != null) {
            petList.forEach(pet -> {
                petDTOList.add(convertPetEntityToDTO(pet));
            });
        }

        return petDTOList;
    }

    public List<PetDTO> getAllByOwnerId(long ownerId) {
        List<Pet> petListByOwnerId = petRepository.findAllByOwnerId(ownerId);
        List<PetDTO> petDTOList = new ArrayList<>();

        // Prepare the DTOs if the query returns something
        if (petListByOwnerId != null) {
            petListByOwnerId.forEach(pet -> {
                petDTOList.add(convertPetEntityToDTO(pet));
            });
        }

        return petDTOList;
    }

    private static PetDTO convertPetEntityToDTO(Pet petEntity) {
        // Sanity check
        if ((petEntity == null) || (petEntity.getCustomer() == null)) {
            return null;
        }
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(petEntity, petDTO);
        petDTO.setOwnerId(petEntity.getCustomer().getId());
        return petDTO;
    }

    private static Pet convertPetDTOToEntity(PetDTO petDTO) {
        // Sanity check
        if (petDTO == null) {
            return null;
        }

        Pet petEntity = new Pet();
        BeanUtils.copyProperties(petDTO, petEntity);
        return petEntity;
    }
}
