package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobConditionRepository extends JpaRepository<JobCondition,Long> {

    // fetch join으로 수정
    Optional<JobCondition> findByCaregiver(Caregiver caregiver);

    boolean existsById(Long id);

    @Query(value = """
        SELECT jc.*
        FROM job_condition jc
        JOIN work_location wl ON wl.job_condition_id = jc.job_condition_id
        WHERE wl.location_id = :rcLocationId""", nativeQuery = true)
    Optional<List<JobCondition>> findAllByRecommendedListByElder(@Param("rcLocationId") Long rcLocationId);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM job_condition order by RAND() limit 1",nativeQuery = true)
    Optional<JobCondition> findRandom();
}
