//package com.example.springserver.global.config;
//
//import com.example.springserver.domain.center.entity.Center;
//import com.example.springserver.domain.center.entity.CenterDocument;
//import com.example.springserver.domain.center.repository.CenterRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class ElasticsearchInitializer {
//
//    private final CenterRepository centerRepository;
//    private final CenterSearchRepository centerSearchRepository;
//
//    @Bean
//    public ApplicationRunner initElasticsearch() {
//        return args -> {
//            log.info("===== Elasticsearch 초기화 ===== ");
//
//            centerSearchRepository.deleteAll();
//            log.info("Elasticsearch - 기존 데이터 삭제");
//
//            // MySQL에서 모든 Center 데이터 가져오기
//            List<Center> centers = centerRepository.findAll();
//            log.info("MySQL - Center 데이터 로드 완료: {} 개", centers.size());
//
//            // Elasticsearch에 저장
//            List<CenterDocument> documents = centers.stream()
//                    .map(c -> new CenterDocument(c.getCenterId(), c.getCenterName(), c.getCenterLeaderName(), c.getAddress(),
//                            c.getHasBathCar(), c.getRate(), c.getIntro(), c.getStartTime(), c.getEndTime()))
//                    .collect(Collectors.toList());
//
//            centerSearchRepository.saveAll(documents);
//            log.info("===== Elasticsearch - MySQL 데이터 동기화 완료 =====");
//        };
//    }
//}