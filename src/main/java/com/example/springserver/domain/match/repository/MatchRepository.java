package com.example.springserver.domain.match.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByJobCondition(JobCondition jobCondition);

    List<Object[]> findTopCaregivers(@Param("recruitId") Long recruitId);

    Collection<Match> findAllByRecruitConditionId(Long recruitId);
}
