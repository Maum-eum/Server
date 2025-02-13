package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.Elder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElderRepository extends JpaRepository<Elder, Long> {

    List<Elder> findByIsTemporary(boolean isTemporary);

    Elder findByElderIdAndIsTemporary(Long elderId, boolean isTemporary);
}
