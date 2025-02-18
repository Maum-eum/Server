package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Elder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ElderRepository extends JpaRepository<Elder, Long> {

    List<Elder> findByIsTemporarySave(boolean isTemporary);

    Elder findByElderIdAndIsTemporarySave(Long elderId, boolean isTemporary);

    Optional<Elder> findByElderIdAndCenter_CenterId(Long elderId, Long centerId);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM elder order by RAND() limit 1",nativeQuery = true)
    Optional<Elder> findRandom();

    boolean existsByElderIdAndCenter_CenterId(Long elderId, Long centerId);
}
