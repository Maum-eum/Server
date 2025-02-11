package com.example.springserver.repository.elder;

import com.example.springserver.domain.entity.elder.ElderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ElderRepository extends JpaRepository<ElderEntity, Long> {

}
