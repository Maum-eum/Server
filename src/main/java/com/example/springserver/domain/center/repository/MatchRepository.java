package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.match.entity.Match;
import com.example.springserver.domain.center.entity.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {

    List<Match> findAllByJobCondition(JobCondition jobCondition);

    @Query("SELECT DISTINCT m FROM Match m "
            + "JOIN FETCH m.requirementCondition rc "
            + "JOIN FETCH rc.elder e "
            + "LEFT JOIN FETCH rc.recruitTimes rt "
            + "WHERE m.jobCondition = :jobCondition AND m.status IN (:statuses)")
    List<Match> findAllByJobConditionWithStatus(
            @Param("jobCondition") JobCondition jobCondition,
            @Param("statuses") List<MatchStatus> statuses
    );
}
