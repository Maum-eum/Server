package com.example.springserver.repository.elder;

import com.example.springserver.domain.entity.elder.ElderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElderRepository extends JpaRepository<ElderEntity, Long> {

    List<ElderEntity> findByCenterId(Long centerId);

    ElderEntity findByCenterIdAndElderId(Long centerId, Long elderId);
}
