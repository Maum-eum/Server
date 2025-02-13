package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Boolean existsByUsername(String username);

    Optional<Caregiver> findByUsername(String username);
}
