package com.example.springserver.repository;

import com.example.springserver.domain.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByCenterName(String centerName);
}
