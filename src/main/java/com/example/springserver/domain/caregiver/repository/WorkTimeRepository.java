package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {
}
