package com.example.springserver.repository.location;

import com.example.springserver.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location,Long> {
    List<Location> findAllBySigunguId(Long sigunguId);
}
