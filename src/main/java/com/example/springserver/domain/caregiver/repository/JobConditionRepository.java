package com.example.springserver.domain.caregiver.repository;

import com.example.springserver.domain.caregiver.entity.Caregiver;
import com.example.springserver.domain.caregiver.entity.JobCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JobConditionRepository extends JpaRepository<JobCondition,Long> {
    Optional<JobCondition> findByCaregiver(Caregiver caregiver);

    @Query(value = """
            SELECT DISTINCT jc.*
        FROM job_condition jc
        JOIN work_location wl\s
          ON jc.job_condition_id = wl.job_condition_id
        JOIN recruit_condition rc\s
          ON rc.location_id = wl.location_id
        JOIN recruit_time rt2\s
          ON rt2.recruit_condition_id = rc.recruit_condition_id
        JOIN recruit_time rt3\s
          ON rt3.recruit_condition_id = rc.recruit_condition_id
          AND rt3.dayOfWeek = rt2.dayOfWeek
        WHERE\s
          -- 요일 조건: job_condition의 day_of_week와 recruit_time의 요일 플래그 비교
          (jc.day_of_week & rt2.dayOfWeek) != 0
          -- 시간 조건: job_condition의 근무시간이 recruit_time의 시작시간과 겹치는지 확인
          AND jc.start_time < rt3.startTime\s
          AND jc.end_time > rt3.startTime;""", nativeQuery = true)
    List<JobCondition> findAllRecommendedListByElder(Long id);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM job_condition order by RAND() limit 1",nativeQuery = true)
    Optional<JobCondition> findRandom();
}
