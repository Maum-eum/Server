package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkLocationRepository extends JpaRepository<WorkLocation,Long> {
    List<WorkLocation> findByJobCondition(JobCondition jobCondition);

    void deleteByJobCondition(JobCondition savedJobCondition);
}
