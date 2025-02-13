package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Boolean existsByUsername(String username);

    Optional<Admin> findByUsername(String username);
}
