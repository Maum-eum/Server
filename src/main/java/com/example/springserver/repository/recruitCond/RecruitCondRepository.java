package com.example.springserver.repository.recruitCond;

import com.example.springserver.domain.center.entity.RecruitCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitCondRepository extends JpaRepository<RecruitCondition, Long> {
}
