package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.RecruitTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecruitTimeRepository extends JpaRepository<RecruitTime, Long> {

    @Modifying
    @Query("DELETE FROM RecruitTime rt WHERE rt.recruitCondition.recruitConditionId = :recruitConditionId")
    void deleteByRecruitConditionId(Long recruitConditionId);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM recruit_time order by RAND() limit 1",nativeQuery = true)
    Optional<RecruitTime> findRandom();
}
