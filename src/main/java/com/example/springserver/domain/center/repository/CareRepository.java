package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Care;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CareRepository extends JpaRepository<Care, Long> {

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM care order by RAND() limit 1",nativeQuery = true)
    Optional<Care> findRandom();
}
