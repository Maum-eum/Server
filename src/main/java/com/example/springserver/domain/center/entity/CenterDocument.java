//package com.example.springserver.domain.center.entity;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.elasticsearch.annotations.Document;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@Document(indexName = "center")
//public class CenterDocument {
//    @Id
//    private String id;
//    private String centerName;
//    private String centerLeaderName;
//    private String address;
//    private Boolean hasBathCar; // 목욕차량 보유 여부
//    private String rate;
//    private String intro;
//    private String startTime;
//    private String endTime;
//
//    public CenterDocument(Long id, String centerName, String centerLeaderName,
//                          String address, Boolean hasBathCar, String rate, String intro,
//                          String startTime, String endTime) {
//        this.id = String.valueOf(id);
//        this.centerName = centerName;
//        this.centerLeaderName = centerLeaderName;
//        this.address = address;
//        this.hasBathCar = hasBathCar;
//        this.rate = rate;
//        this.intro = intro;
//        this.startTime = startTime;
//        this.endTime = endTime;
//    }
//}