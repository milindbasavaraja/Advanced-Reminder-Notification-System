package org.nyu.java.project.reminderregister.repository;

import org.nyu.java.project.reminderregister.entity.ReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ReminderRepository extends JpaRepository<ReminderEntity, Integer> {
    List<ReminderEntity> findByUserId(Long userId);



    List<ReminderEntity> findReminderEntitiesByReminderDateAndReminderTimeBetweenAndIsExpired(LocalDate date, LocalTime startTime,LocalTime endTime,Boolean isExpired);
}
