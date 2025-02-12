package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Elder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElderRepository extends JpaRepository<Elder, Long> {

    List<Elder> findByCenterId(Long centerId);

    Elder findByCenterIdAndElderId(Long centerId, Long elderId);
}
