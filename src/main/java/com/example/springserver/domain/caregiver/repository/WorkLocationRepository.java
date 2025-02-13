package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.WorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLocationRepository extends JpaRepository<WorkLocation,Long> {
}
