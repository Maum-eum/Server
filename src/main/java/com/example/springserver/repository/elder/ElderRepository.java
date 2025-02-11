package com.example.springserver.repository.elder;

import com.example.springserver.domain.entity.elder.ElderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElderRepository extends JpaRepository<ElderEntity, Long> {

}
