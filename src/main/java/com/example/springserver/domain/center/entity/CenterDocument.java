package com.example.springserver.domain.center.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "center")
public class CenterDocument {
    @Id
    private String id;
    private String centerName;
    private String address;

    public CenterDocument(Long id, String centerName, String address) {
        this.id = String.valueOf(id);
        this.centerName = centerName;
        this.address = address;
    }
}