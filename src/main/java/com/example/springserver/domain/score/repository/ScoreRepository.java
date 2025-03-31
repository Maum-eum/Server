package com.example.springserver.domain.score.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.score.entity.MatchScore;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<MatchScore,Long> {


    @Query(value = "SELECT * FROM match_score WHERE recruit_condition_id = :recruit_condition_id ORDER BY score FOR UPDATE;",nativeQuery = true)
    Optional<List<MatchScore>> findByRecruitConditionId(@Param("recruit_condition_id") Long request);

    @Query(value = "SELECT jc.* FROM job_condition jc\n" +
    "JOIN match_score ms ON jc.job_condition_id = ms.job_condition_id\n" +
    "WHERE ms.recruit_condition_id = :recruitConditionId\n"
    , nativeQuery = true)
    Optional<List<JobCondition>> findAllByRecruitConditionId(@Param("recruitConditionId") Long recruitId);

    @Modifying
    @Query(value = "UPDATE match_score\n" +
            "SET deleted_at = NOW()\n" +
            "WHERE recruit_condition_id = :recruitConditionId;",nativeQuery = true)
    void deleteAllByRecruitConditionId(@Param("recruitConditionId") Long recruitConditionId);

    @Modifying
    @Query(value = "UPDATE match_score\n" +
            "SET deleted_at = NOW()\n" +
            "WHERE job_condition_id = :jobConditionId;",nativeQuery = true)
    void deleteAllByJobConditionId(@Param("jobConditionId") Long jobConditionId);


    @Modifying
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("DELETE FROM MatchScore m WHERE m.deletedAt IS NOT NULL")
    void deleteAllMarkedAsDeleted();
}
