package com.example.springserver.domain.center.repository;

import com.example.springserver.domain.center.entity.CenterDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Repository
public interface CenterSearchRepository extends ElasticsearchRepository<CenterDocument, String> {
    List<CenterDocument> findByCenterName(String centerName);

    // 키워드 포함 검색
    Page<CenterDocument> findByCenterNameContaining(String centerName, PageRequest pageRequest);
}