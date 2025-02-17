package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CenterRepository extends JpaRepository<Center, Long> {
    Optional<Center> findByCenterName(String centerName);
    Center findByCenterLeaderName(String adminUsername);

    List<Center> findByCenterNameContaining(String keyword);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM center order by RAND() limit 1",nativeQuery = true)
    Optional<Center> findRandom();
}
