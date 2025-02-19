package com.example.springserver.repository.location;

import com.example.springserver.domain.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<Location> findByLocationId(Long locationId);
    List<Location> findAllBySigunguId(Long sigunguId);
}
