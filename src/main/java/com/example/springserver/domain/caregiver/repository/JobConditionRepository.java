package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobConditionRepository extends JpaRepository<JobCondition,Long> {
    Optional<JobCondition> findByCaregiver(Caregiver caregiver);
}
