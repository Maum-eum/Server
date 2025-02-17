package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate,Long> {

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM certificate order by RAND() limit 1",nativeQuery = true)
    Optional<Certificate> findRandom();
}
