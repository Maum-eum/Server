package com.example.springserver.repository;

import com.example.springserver.domain.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Boolean existsByUsername(String username);
}
