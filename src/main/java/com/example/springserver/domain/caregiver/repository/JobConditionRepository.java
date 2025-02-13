package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobConditionRepository extends JpaRepository<JobCondition,Long> {
}
