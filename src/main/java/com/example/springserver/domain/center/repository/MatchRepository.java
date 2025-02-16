package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.caregiver.entity.JobCondition;
import com.example.springserver.domain.center.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Long> {

    List<Match> findAllByJobCondition(JobCondition jobCondition);
}
