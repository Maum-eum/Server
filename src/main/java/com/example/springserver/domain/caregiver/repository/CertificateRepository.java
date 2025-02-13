package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate,Long> {

}
