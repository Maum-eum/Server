package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.caregiver.entity.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {
    List<WorkTime> findByJobCondition(JobCondition jobCondition);

    void deleteByJobCondition(JobCondition savedJobCondition);
}
