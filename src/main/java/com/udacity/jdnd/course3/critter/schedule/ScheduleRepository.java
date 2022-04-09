package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;

public interface ScheduleRepository {
    Schedule save(Schedule schedule, List<Long> employeeIds, List<Long> petIds);
    Schedule getById(Long id);
    List<Schedule> findAll();
    List<Schedule> findAllByPetId(Long petId);
    List<Schedule> getByEmployeeId(Long employeeId);
    List<Schedule> findAllByCustomerId(Long customerId);
}
