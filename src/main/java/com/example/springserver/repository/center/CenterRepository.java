package com.example.springserver.repository.center;

import com.example.springserver.domain.center.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByCenterName(String centerName);
}
