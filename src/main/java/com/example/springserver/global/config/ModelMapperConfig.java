package com.example.springserver.global.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // 필요한 경우, 설정 추가 가능
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true) // 필드명 자동 매칭 활성화
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE); // private 필드에도 접근 가능

        return modelMapper;
    }
}