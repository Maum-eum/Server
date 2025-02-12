package com.example.springserver.repository;

import com.example.springserver.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Boolean existsByUsername(String username);
}
