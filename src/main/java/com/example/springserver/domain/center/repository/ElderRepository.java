package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Elder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ElderRepository extends JpaRepository<Elder, Long> {

    List<Elder> findByIsTemporarySave(boolean isTemporary);

    Elder findByElderIdAndIsTemporarySave(Long elderId, boolean isTemporary);

    Optional<Elder> findByElderIdAndCenter_CenterId(Long elderId, Long centerId);
}
