package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.util.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    public ScheduleDTO save(ScheduleDTO newScheduleDTO) {
        // Sanity check
        if (newScheduleDTO == null) {
            return null;
        }

        Schedule schedule = convertScheduleDTOToEntity(newScheduleDTO);
        //Save the new schedule in DB
        schedule = scheduleRepository.save(schedule, newScheduleDTO.getEmployeeIds(), newScheduleDTO.getPetIds());

        return convertScheduleEntityToDTO(schedule);
    }

    public List<ScheduleDTO> getAll() {
        List<Schedule> allScheduleList = scheduleRepository.findAll();

        if (allScheduleList == null) {
            return null;
        }

        // Build the response DTO
        List<ScheduleDTO> petDTOList = new ArrayList<>();
        allScheduleList.forEach(schedule -> petDTOList.add(convertScheduleEntityToDTO(schedule)));

        return petDTOList;
    }

    List<ScheduleDTO> getAllByPetId(Long petId) {
        List<Schedule> scheduleListByPetId = scheduleRepository.findAllByPetId(petId);

        if (scheduleListByPetId == null) {
            return null;
        }

        // Build the response DTO
        List<ScheduleDTO> petDTOList = new ArrayList<>();
        scheduleListByPetId.forEach(schedule -> petDTOList.add(convertScheduleEntityToDTO(schedule)));

        return petDTOList;
    }

    List<ScheduleDTO> getAllByEmployeeId(Long employeeId) {
        List<Schedule> scheduleListByEmployeeId = scheduleRepository.getByEmployeeId(employeeId);

        if (scheduleListByEmployeeId == null) {
            return null;
        }

        // Build the response DTO
        List<ScheduleDTO> petDTOList = new ArrayList<>();
        scheduleListByEmployeeId.forEach(schedule -> petDTOList.add(convertScheduleEntityToDTO(schedule)));

        return petDTOList;
    }

    List<ScheduleDTO> getAllByCustomerId(Long customerId) {
        List<Schedule> scheduleListByCustomerId = scheduleRepository.findAllByCustomerId(customerId);

        if (scheduleListByCustomerId == null) {
            return null;
        }

        // Build the response DTO
        List<ScheduleDTO> petDTOList = new ArrayList<>();
        scheduleListByCustomerId.forEach(schedule -> petDTOList.add(convertScheduleEntityToDTO(schedule)));

        return petDTOList;
    }

    private static ScheduleDTO convertScheduleEntityToDTO(Schedule scheduleEntity) {
        // Sanity check
        if (scheduleEntity == null) {
            return null;
        }
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(scheduleEntity, scheduleDTO);
        // Get the id lists
        if (scheduleEntity.getPetList() != null) {
            scheduleDTO.setPetIds(scheduleEntity.getPetList().stream().map(Pet::getId).collect(Collectors.toList()));
        }
        if (scheduleEntity.getEmployeeList() != null) {
            scheduleDTO.setEmployeeIds(scheduleEntity.getEmployeeList().stream().map(Employee::getId).collect(Collectors.toList()));
        }
        if (scheduleEntity.getActivity() != null) {
            scheduleDTO.setActivities(Transformers.getSkillsFromBlob(scheduleEntity.getActivity()));
        }
        return scheduleDTO;
    }

    private static Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO) {
        // Sanity check
        if (scheduleDTO == null) {
            return null;
        }

        Schedule scheduleEntity = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, scheduleEntity);
        if (scheduleDTO.getActivities() != null) {
            scheduleEntity.setActivity(Transformers.getBlobFromSkills(scheduleDTO.getActivities()));
        }
        return scheduleEntity;
    }
}
