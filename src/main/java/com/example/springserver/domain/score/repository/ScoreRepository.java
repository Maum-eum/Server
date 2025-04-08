package com.example.springserver.domain.score.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.score.entity.MatchScore;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<MatchScore,Long> {

    /**
     * RC_ID 기준으로 List<MatchScore> 조회합니다.
     */
    @Query(value = "SELECT * FROM match_score WHERE recruit_condition_id = :recruit_condition_id ORDER BY score FOR UPDATE;",nativeQuery = true)
    Optional<List<MatchScore>> findByRecruitConditionId(@Param("recruit_condition_id") Long request);

    /**
     * JC_ID와 RC_ID 기준으로 List<MatchScore> 조회합니다.
     */
    @Query(value = "SELECT jc.* FROM job_condition jc\n" +
    "JOIN match_score ms ON jc.job_condition_id = ms.job_condition_id\n" +
    "WHERE ms.recruit_condition_id = :recruitConditionId\n"
    , nativeQuery = true)
    Optional<List<JobCondition>> findAllByRecruitConditionId(@Param("recruitConditionId") Long recruitId);

    /**
     *  RC 기준으로 조회한 모든 match_score에 대해 soft_delete 해버립니다.
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE match_score\n" +
            "SET deleted_at = NOW()\n" +
            "WHERE recruit_condition_id = :recruitConditionId;",nativeQuery = true)
    void deleteAllByRecruitConditionId(@Param("recruitConditionId") Long recruitConditionId);

    /**
     *  JC 기준으로 조회한 모든 match_score에 대해 soft_delete 해버립니다.
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE match_score\n" +
            "SET deleted_at = NOW()\n" +
            "WHERE job_condition_id = :jobConditionId;",nativeQuery = true)
    void deleteAllByJobConditionId(@Param("jobConditionId") Long jobConditionId);


    /**
     * 스케줄러에서 호출되는 Hard_Delete 입니다.
     */
    @Modifying
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    @Query("DELETE FROM MatchScore m WHERE m.deletedAt IS NOT NULL")
    void deleteAllMarkedAsDeleted();

    /**
     * Deleted_at 이 Null이 아니어도 부를수 있는 커스텀 SQL 쿼리입니다.
     * 이 쿼리를 통해서 기존 MatchScore 존재하는지 불러올거예요.
     */
    @Query(value = "SELECT * FROM match_score WHERE job_condition_id = :jobConditionId", nativeQuery = true)
    List<MatchScore> findAllByJobConditionIncludingDeleted(@Param("jobConditionId") Long jobConditionId);

    @Query(value = "SELECT * FROM match_score WHERE recruit_condition_id = :recruitConditionId", nativeQuery = true)
    List<MatchScore> findAllByRecruitConditionIncludingDeleted(Long recruitConditionId);

    /**
     *  UPSERT 인데 당장에 쓰이지 않습니다 무시 ㄱㄱ
     */
//    @Modifying
//    @Query(value = """
//    INSERT INTO match_score (
//        caregiver_name, caregiver_img, recruit_condition_id, job_condition_id, score, matching_status, created_at, updated_at
//    ) VALUES (
//        :caregiverName, :caregiverImg, :rcId, :jcId, :score, :status, NOW(), NOW()
//    ) ON DUPLICATE KEY UPDATE
//        deleted_at = NULL,
//        score = VALUES(score),
//        matching_status = VALUES(matching_status),
//        updated_at = NOW()
//    """, nativeQuery = true)
//    void upsertScore(@Param("caregiverName") String caregiverName,
//                     @Param("caregiverImg") String caregiverImg,
//                     @Param("rcId") Long recruitConditionId,
//                     @Param("jcId") Long jobConditionId,
//                     @Param("score") int score,
//                     @Param("status") String status);

}
