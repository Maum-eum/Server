package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.RecruitCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecruitCondRepository extends JpaRepository<RecruitCondition, Long> {

    @Query("SELECT rc FROM RecruitCondition rc LEFT JOIN FETCH rc.recruitTimes WHERE rc.recruitConditionId = :recruitId")
    Optional<RecruitCondition> findWithRecruitTimesById(@Param("recruitId") Long recruitId);

    @Query("SELECT DISTINCT rc FROM RecruitCondition rc LEFT JOIN FETCH rc.recruitTimes WHERE rc.elder.elderId = :elderId")
    List<RecruitCondition> findWithRecruitTimesByElderId(@Param("elderId") Long elderId);
}
