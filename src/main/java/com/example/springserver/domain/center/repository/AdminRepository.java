package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Boolean existsByUsername(String username);

    Optional<Admin> findByUsername(String username);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM admin order by RAND() limit 1",nativeQuery = true)
    Optional<Admin> findRandom();
}
