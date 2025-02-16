package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Care;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareRepository extends JpaRepository<Care, Long> {
}
