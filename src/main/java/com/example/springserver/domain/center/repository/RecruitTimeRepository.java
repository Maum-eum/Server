package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.RecruitCondition;
import com.example.springserver.domain.center.entity.RecruitTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecruitTimeRepository extends JpaRepository<RecruitTime, Long> {

    @Modifying
    @Query("DELETE FROM RecruitTime rt WHERE rt.recruitCondition.recruitConditionId = :recruitConditionId")
    void deleteByRecruitConditionId(Long recruitConditionId);
}
