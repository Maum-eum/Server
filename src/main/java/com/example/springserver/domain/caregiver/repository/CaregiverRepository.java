package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Boolean existsByUsername(String username);
}
