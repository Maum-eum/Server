package com.example.springserver;

import com.example.springserver.domain.caregiver.entity.*;
import com.example.springserver.domain.caregiver.entity.enums.CertType;
import com.example.springserver.domain.caregiver.entity.enums.Level;
import com.example.springserver.domain.caregiver.entity.enums.ScheduleAvailability;
import com.example.springserver.domain.caregiver.repository.*;
import com.example.springserver.domain.center.entity.*;
import com.example.springserver.domain.center.entity.enums.CareType;
import com.example.springserver.domain.center.entity.enums.ElderRate;
import com.example.springserver.domain.center.entity.enums.Inmate;
import com.example.springserver.domain.center.entity.enums.Week;
import com.example.springserver.domain.center.repository.*;
import com.example.springserver.domain.location.entity.Location;
import com.example.springserver.repository.location.LocationRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Transactional
@Component
public class MockDataGenerator implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("Mock 데이터 생성 메서드 실행...");
//        createMockData();
//        System.out.println("Mock 데이터 생성 메서드 끝...");
    }

    /* 애플리케이션 실행 시점에 Mock 데이터 생성 */
    Faker faker = new Faker(new Locale("ko", "ko")); // 언어 설정
    @Autowired
    AdminRepository adminRepository; // 관리자
    @Autowired
    ElderRepository elderRepository; // 어르신
    @Autowired
    RecruitCondRepository recruitCondRepository; // 어르신 - 구인조건
    @Autowired
    RecruitTimeRepository recruitTimeRepository; // 어르신 - 구인조건 별 시간
    @Autowired
    CaregiverRepository caregiverRepository; // 요양보호사
    @Autowired
    CertificateRepository certificateRepository; // 요양보호사 - 자격증
    @Autowired
    ExperienceRepository experienceRepository; // 요양보호사 - 경럭
    @Autowired
    JobConditionRepository jobConditionRepository; // 요양보호사 - 구직 조건
    @Autowired
    WorkLocationRepository workLocationRepository; // 요양보호사 - 구직 지역
    @Autowired
    CenterRepository centerRepository; // 센터
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CareRepository careRepository;

    void createMockData() {
        for (int i = 0; i < 10; i++) {
            admin();
        }
        for (int i = 0; i < 1000; i++) {
            elder();
        }

        // 3. 요양보호사 데이터 생성
        for (int i = 0; i < 1000; i++) {
            careGiver();
        }

        // 4. 자격증 및 경력 데이터 생성
        for (int i = 0; i < 5; i++) {
            certificate();
            experience();
        }

        // 5. 구직 조건, 근무지 데이터 생성
        for (int i = 0; i < 10; i++) {
            jobCondition(i+1);
            workLocation();
        }

        // 6. 구인 조건과 시간 데이터 생성
        for (int i = 0; i < 100; i++) {
            recruitCondition(i+1);
            care(i+1);
        }
        for (int i = 0; i < 100; i++) {
            recruitTime();
            recruitLocation();
        }
    }

    void certificate() {
        Caregiver caregiver = caregiverRepository.findRandom().get();
        String certNum = faker.text().text();
        CertType certType = faker.options().option(CertType.class);
        Level certRate = faker.options().option(Level.class);

        Certificate certificate = new Certificate(caregiver, certNum, certType, certRate);
        certificateRepository.save(certificate);
    }

    void experience() {
        Caregiver caregiver = caregiverRepository.findRandom().get();
        Integer duration = faker.number().numberBetween(1, 10);
        String title = faker.text().text();
        String description = faker.text().text();

        Experience experience = new Experience(caregiver, duration, title, description);
        experienceRepository.save(experience);
    }

    void admin() {
        String username = faker.funnyName().name();
        String password = faker.hashing().sha256();
        String name = faker.name().fullName();
        Center center = centerRepository.findRandom()
                .orElseThrow(() -> new RuntimeException("Center not found"));
        String connect = faker.text().text();

        Admin admin = new Admin(username, password, name, center, connect);
        adminRepository.save(admin);
    }

    void elder() {
        Center center = centerRepository.findRandom().get(); // 센터: 1~5
        String name = faker.name().fullName();
        Integer gender = faker.number().numberBetween(0, 1); // 1 or 0
        LocalDate birth = faker.timeAndDate().birthday();
        ElderRate rate = faker.options().option(ElderRate.class); // 장기 요양 등급
        List<Inmate> inmateTypes = IntStream.range(0, faker.random().nextInt(1, 3)) // 1~3개의 요소
                .mapToObj(i -> faker.options().option(Inmate.class))
                .toList();
//        String imgUrl = faker.image().base64JPG();
        Integer weight = faker.number().numberBetween(30, 90);
        boolean isTemporarySave = faker.bool().bool(); // 임시 저장 여부
        boolean isNormal = faker.bool().bool(); // 증상 보유 여부
        boolean hasShortTermMemoryLoss = faker.bool().bool(); // 단기 기억 장애 여부
        boolean wandersOutside = faker.bool().bool(); // 집밖을 배회 여부
        boolean actsLikeChild = faker.bool().bool(); // 어린아이 같은 행동 여부
        boolean hasDelusions = faker.bool().bool(); // 망상 여부
        boolean hasAggressiveBehavior = faker.bool().bool(); // 공격적인 행동 여부

        Elder elder = new Elder(center, name, gender, birth, rate, inmateTypes, null, weight,
                isTemporarySave, isNormal, hasShortTermMemoryLoss, wandersOutside, actsLikeChild,
                hasDelusions, hasAggressiveBehavior);
        elderRepository.save(elder);
    }

    void recruitCondition(int i) {
        Elder elder = elderRepository.findById(Long.valueOf(i))
                .orElseThrow(() -> new RuntimeException("===== elder 중복 ====="));

        List<CareType> careTypes = Collections.singletonList(faker.options().option(CareType.class));

        // 랜덤 Location
        Location location = locationRepository.findByLocationId(faker.random().nextLong(1, 10))
                .orElseThrow(() -> new RuntimeException("=== locatino error ====="));

        Integer desiredHourlyWage = faker.number().numberBetween(10030, 30000);
        String detailRequiredService = faker.text().text();

        // 10개 항목 false 초기화
        boolean[] boolFields = new boolean[]{
                false, false, false, false, false, false, false, false, false, false
        };

        // 10개의 항목 중 true로 설정할 index 2개 생성
        Set<Integer> selectedIndexes = new HashSet<>();
        while (selectedIndexes.size() < 2) {
            selectedIndexes.add(faker.number().numberBetween(0, boolFields.length));
        }
        // 선택된 index의 항목만 true
        for (int index : selectedIndexes) {
            boolFields[index] = true;
        }

        RecruitCondition recruitCondition = new RecruitCondition(
                elder, location, careTypes,
                boolFields[0], boolFields[1], boolFields[2], boolFields[3],
                boolFields[4], desiredHourlyWage, boolFields[5], boolFields[6],
                boolFields[7], boolFields[8], boolFields[9], false, false, false,
                false, false, false, false, false, false, false, false, false, false,
                detailRequiredService
        );

        // recruitTimes 리스트 요소 랜덤 2개 생성
        for (int k = 0; k < 2; k++) {
            RecruitTime recruitTime = new RecruitTime(
                    recruitCondition,
                    faker.options().option(Week.class), // 요일 랜덤 선택
                    faker.number().numberBetween(18L, 24L), // 시작 시간 (0~23시)
                    faker.number().numberBetween(26L, 28L) // 종료 시간 (1~24시)
            );

            // recruitCondition에 추가
            recruitCondition.addRecruitTime(recruitTime);
        }

        // recruitCondition 저장
        recruitCondRepository.save(recruitCondition);
    }


    void care(int i) {
        Elder elder = elderRepository.findById(Long.valueOf(i))
                .orElseThrow(() -> new RuntimeException("===== elder 중복 ====="));
        List<CareType> careTypes = Collections.singletonList(faker.options().option(CareType.class));
        Location careLocation = locationRepository.findByLocationId(faker.random().nextLong(1,10))
                .orElseThrow(() -> new RuntimeException("=== locatino error ====="));;
        boolean mealAssistance = faker.bool().bool();
        boolean toiletAssistance = faker.bool().bool();
        boolean moveAssistance = faker.bool().bool();
        boolean dailyLivingAssistance = faker.bool().bool();
        Integer desiredHourlyWage = faker.number().numberBetween(10030, 30000); // 희망 급여
        boolean selfFeeding = faker.bool().bool(); // 스스로 식사 가능
        boolean mealPreparation = faker.bool().bool(); // 식사 차려드리기
        boolean cookingAssistance = faker.bool().bool(); // 요리 필요
        boolean enteralNutritionSupport = faker.bool().bool(); // 경관식 보조
        boolean selfToileting = faker.bool().bool(); // 스스로 배변 가능
        boolean occasionalToiletingAssist = faker.bool().bool(); // 가끔 대소변 실수
        boolean diaperCare = faker.bool().bool(); // 기저귀 케어 필요
        boolean catheterOrStomaCare = faker.bool().bool(); // 유치도뇨/방광루/장루 관리
        boolean independentMobility = faker.bool().bool(); // 스스로거동가능
        boolean mobilityAssist = faker.bool().bool(); // 이동시 부축도움
        boolean wheelchairAssist = faker.bool().bool(); // 휠체어 이동 보조
        boolean immobile = faker.bool().bool(); // 거동 불가
        boolean cleaningLaundryAssist = faker.bool().bool(); // 청소 빨래 보조
        boolean bathingAssist = faker.bool().bool(); // 목욕 보조
        boolean hospitalAccompaniment = faker.bool().bool(); // 병원 보조
        boolean exerciseSupport = faker.bool().bool(); // 산책, 간단한 운동
        boolean emotionalSupport = faker.bool().bool(); // 정서적 지원
        boolean cognitiveStimulation = faker.bool().bool(); // 인지 자극 활동
        String detailRequiredService = faker.text().text();

        Care care = new Care(elder, careTypes, careLocation,
                mealAssistance, toiletAssistance, moveAssistance, dailyLivingAssistance, desiredHourlyWage, selfFeeding, mealPreparation,
                cookingAssistance, enteralNutritionSupport, selfToileting, occasionalToiletingAssist, diaperCare, catheterOrStomaCare,
                independentMobility, mobilityAssist, wheelchairAssist, immobile, cleaningLaundryAssist, bathingAssist, hospitalAccompaniment,
                exerciseSupport, emotionalSupport, cognitiveStimulation, detailRequiredService);
        careRepository.save(care);
    }

    void recruitTime() {
        RecruitCondition recruitCondition = recruitCondRepository.findRandom().get();
        Week dayOfWeek = faker.options().option(Week.class);
        Long startTime = (long) faker.number().numberBetween(18, 28);
        Long endTime = (long) faker.number().numberBetween(30, 36);

        RecruitTime recruitTime = new RecruitTime(recruitCondition, dayOfWeek, startTime, endTime);
        recruitTimeRepository.save(recruitTime);
    }

    void recruitLocation() {
        RecruitCondition recruitCondition = recruitCondRepository.findRandom().get();
        Week dayOfWeek = faker.options().option(Week.class);
        Long startTime = (long) faker.number().numberBetween(18, 28);
        Long endTime = (long) faker.number().numberBetween(30, 36);

        RecruitTime recruitTime = new RecruitTime(recruitCondition, dayOfWeek, startTime, endTime);
        recruitTimeRepository.save(recruitTime);
    }

    void careGiver() {
        String username = faker.funnyName().name();
        String password = faker.hashing().sha256();
        String name = faker.name().fullName();
        String contact = String.valueOf(faker.phoneNumber());
        Boolean car = faker.bool().bool();
        Boolean education = faker.bool().bool();
//        String img = faker.image().base64JPG();
        String intro = faker.text().text();
        String address = String.valueOf(faker.address());
        Boolean employmentStatus = faker.bool().bool();
        List<Experience> experiences = experienceRepository.findRandom().stream().toList();
        List<Certificate> certificates = certificateRepository.findRandom().stream().toList();

        Caregiver caregiver = new Caregiver(username, password, name, contact, car, education, null, intro, address, employmentStatus, experiences, certificates);
        Caregiver savedCaregiver = caregiverRepository.save(caregiver);
        System.out.println("Saved Caregiver ID: " + savedCaregiver.getId());
    }

    void jobCondition(int i) {
        ScheduleAvailability flexibleSchedule = faker.options().option(ScheduleAvailability.class);
        Integer desiredHourlyWage = faker.number().numberBetween(10030, 50000);
        ScheduleAvailability selfFeeding = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability mealPreparation = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability cookingAssistance = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability enteralNutritionSupport = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability selfToileting = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability occasionalToiletingAssist = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability diaperCare = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability catheterOrStomaCare = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability independentMobility = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability mobilityAssist = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability wheelchairAssist = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability immobile = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability cleaningLaundryAssist = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability bathingAssist = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability hospitalAccompaniment = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability exerciseSupport = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability emotionalSupport = faker.options().option(ScheduleAvailability.class);
        ScheduleAvailability cognitiveStimulation = faker.options().option(ScheduleAvailability.class);

        Integer dayOfWeek = faker.number().numberBetween(1, 6);
        Long startTime = (long) faker.number().numberBetween(18, 28);
        Long endTime = (long) faker.number().numberBetween(30, 36);
        Caregiver caregiver = caregiverRepository.findById((long)i)
                .orElseThrow(() -> new RuntimeException("=========== caregiver 매핑 안됨 ==========="));

        JobCondition jobCondition = new JobCondition(flexibleSchedule, desiredHourlyWage, selfFeeding, mealPreparation,
                cookingAssistance, enteralNutritionSupport, selfToileting, occasionalToiletingAssist, diaperCare, catheterOrStomaCare,
                independentMobility, mobilityAssist, wheelchairAssist, immobile, cleaningLaundryAssist, bathingAssist, hospitalAccompaniment,
                exerciseSupport, emotionalSupport, cognitiveStimulation, dayOfWeek, startTime, endTime, caregiver);

        // 근무 가능 지역 랜덤 2개 생성
        for(int k=0; k<2; k++) {
            WorkLocation workLocation = new WorkLocation(
                    jobCondition, locationRepository.findByLocationId((long) faker.number().numberBetween(1, 10))
                    .orElseThrow(() -> new RuntimeException("=== locatino error ====="))
            );
            // recruitCondition에 추가
            jobCondition.addWokLocation(workLocation);
        }
        jobConditionRepository.save(jobCondition);
    }

    void workLocation() {
        Location locationId = locationRepository.findByLocationId((long) faker.number().numberBetween(1, 10))
                .orElseThrow(() -> new RuntimeException("=== locatino error ====="));;
        JobCondition jobCondition = jobConditionRepository.findRandom().get();

        WorkLocation workLocation = new WorkLocation(locationId, jobCondition);
        workLocationRepository.save(workLocation);
    }
}
