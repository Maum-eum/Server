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
        SELECT jc.* 
        FROM job_condition jc
        JOIN work_location wl ON jc.job_condition_id = wl.job_condition_id
        JOIN recruit_condition rc ON rc.location_id = wl.location_id
        WHERE 
            EXISTS (
                SELECT 1 
                FROM work_location wl2 
                WHERE wl2.job_condition_id = jc.job_condition_id 
                AND wl2.location_id = rc.location_id
            ) -- 1) 구직자의 다양한 지역 중 하나라도 포함되는지 확인
            AND EXISTS (
                SELECT 1 
                FROM recruit_time rt2 
                WHERE rt2.recruit_condition_id = rc.recruit_condition_id 
                AND (
                    jc.day_of_week & (
                        CASE rt2.dayOfWeek 
                            WHEN 'SUN' THEN 1
                            WHEN 'MON' THEN 2
                            WHEN 'TUE' THEN 4
                            WHEN 'WED' THEN 8
                            WHEN 'THU' THEN 16
                            WHEN 'FRI' THEN 32
                            WHEN 'SAT' THEN 64
                        END
                    )
                ) != 0 -- 2) "겹치는 요일"이 있는지 확인
                AND EXISTS (
                    SELECT 1 
                    FROM recruit_time rt3 
                    WHERE rt3.recruit_condition_id = rc.recruit_condition_id
                    AND rt3.dayOfWeek = rt2.dayOfWeek -- 3) 같은 요일에 대해 시간 비교
                    AND jc.start_time < rt3.startTime 
                    AND jc.end_time > rt3.startTime
                ) -- 4) 겹치는 요일에 대해서만 시간 비교 수행
            )
            AND (
                SELECT COUNT(*)
                FROM (
                    SELECT 'self_feeding' AS field_name, rc.selfFeeding AS rc, jc.self_feeding AS jc UNION ALL
                    SELECT 'meal_preparation', rc.mealPreparation AS rc, jc.meal_preparation AS jc UNION ALL
                    SELECT 'cooking_assistance', rc.cookingAssistance AS rc, jc.cooking_assistance AS jc UNION ALL
                    SELECT 'enteral_nutrition_support', rc.enteralNutritionSupport AS rc, jc.enteral_nutrition_support AS jc UNION ALL
                    SELECT 'self_toileting', rc.selfToileting AS rc, jc.self_toileting AS jc UNION ALL
                    SELECT 'occasional_toileting_assist', rc.occasionalToiletingAssist AS rc, jc.occasional_toileting_assist AS jc UNION ALL
                    SELECT 'diaper_care', rc.diaperCare AS rc, jc.diaper_care AS jc UNION ALL
                    SELECT 'catheter_or_stoma_care', rc.catheterOrStomaCare AS rc, jc.catheter_or_stoma_care AS jc UNION ALL
                    SELECT 'independent_mobility', rc.independentMobility AS rc, jc.independent_mobility AS jc UNION ALL
                    SELECT 'mobility_assist', rc.mobilityAssist AS rc, jc.mobility_assist AS jc UNION ALL
                    SELECT 'wheelchair_assist', rc.wheelchairAssist AS rc, jc.wheelchair_assist AS jc UNION ALL
                    SELECT 'immobile', rc.immobile AS rc, jc.immobile AS jc UNION ALL
                    SELECT 'cleaning_laundry_assist', rc.cleaningLaundryAssist AS rc, jc.cleaning_laundry_assist AS jc UNION ALL
                    SELECT 'bathing_assist', rc.bathingAssist AS rc, jc.bathing_assist AS jc UNION ALL
                    SELECT 'hospital_accompaniment', rc.hospitalAccompaniment AS rc, jc.hospital_accompaniment AS jc UNION ALL
                    SELECT 'exercise_support', rc.exerciseSupport AS rc, jc.exercise_support AS jc UNION ALL
                    SELECT 'emotional_support', rc.emotionalSupport AS rc, jc.emotional_support AS jc UNION ALL
                    SELECT 'cognitive_stimulation', rc.cognitiveStimulation AS rc, jc.cognitive_stimulation AS jc
                ) AS conditions
                WHERE conditions.rc = TRUE AND conditions.jc = 'IMPOSSIBLE' -- 필수 조건이면서 불가능한 경우 필터링
            ) < 3 -- 5) IMPOSSIBLE이 3개 이상이면 필터링
      """, nativeQuery = true)
    List<JobCondition> findAllRecommendedListByElder(Long id);

    // Mock 데이터 생성용
    @Query(value = "SELECT * FROM job_condition order by RAND() limit 1",nativeQuery = true)
    Optional<JobCondition> findRandom();
}
