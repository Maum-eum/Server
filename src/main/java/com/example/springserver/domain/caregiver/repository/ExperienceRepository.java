package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience,Long> {

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM experience order by RAND() limit 1",nativeQuery = true)
    Optional<Experience> findRandom();
}
