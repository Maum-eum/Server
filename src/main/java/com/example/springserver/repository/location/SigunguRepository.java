package com.example.springserver.repository.location;


import com.example.springserver.domain.location.entity.Sigungu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SigunguRepository extends JpaRepository<Sigungu,Long> {
    List<Sigungu> findBySidoId(Long request);
}
