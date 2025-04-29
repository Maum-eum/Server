package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.WorkLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkLocationRepository extends JpaRepository<WorkLocation,Long> {

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM work_location order by RAND() limit 1",nativeQuery = true)
    Optional<WorkLocation> findRandom();
}
