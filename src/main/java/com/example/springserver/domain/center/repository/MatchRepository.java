package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.match.entity.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {

    @Query("SELECT DISTINCT m FROM Match m "
            + "JOIN FETCH m.recruitCondition rc "
            + "JOIN FETCH rc.elder e "
            + "LEFT JOIN FETCH rc.recruitTimes rt "
            + "WHERE m.jobCondition = :jobCondition AND m.status IN (:statuses)")
    List<Match> findAllByJobConditionWithStatus(
            @Param("jobCondition") JobCondition jobCondition,
            @Param("statuses") List<MatchStatus> statuses
    );

    @Query(value = "SELECT `match`.status FROM `match` " +
            "WHERE recruit_condition_id = :rc " +
            "AND job_condition_id = :jc " +
            "AND `match`.status IN ('WAITING', 'TUNING')" +
            "LIMIT 1",
            nativeQuery = true)
    MatchStatus findByJobCondition_IdAndRecruitCondition_Id(@Param("rc") Long rc, @Param("jc") Long jc);

    @Query(value = "SELECT * FROM `match` " +
            "WHERE recruit_condition_id = :rc " +
            "AND job_condition_id = :jc " +
            "AND `match`.status IN ('TUNING')" +
            "LIMIT 1",
            nativeQuery = true)
    Match findByJcAndRC(@Param("jc") Long jc, @Param("rc") Long rc);

    @Query("SELECT m FROM Match m WHERE m.recruitCondition.elder.center.centerId = :centerId")
    List<Match> findByCenterId(@Param("centerId") Long centerId);

    List<Match> findAllByRecruitCondition_RecruitConditionId(Long recruitConditionId);
}
