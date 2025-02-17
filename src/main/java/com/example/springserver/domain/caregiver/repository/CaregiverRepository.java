package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CaregiverRepository extends JpaRepository<Caregiver, Long> {

    Boolean existsByUsername(String username);

    Optional<Caregiver> findByUsername(String username);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM care_giver order by RAND() limit 1",nativeQuery = true)
    Optional<Caregiver> findRandom();
}
