package com.example.springserver.domain.match.converter;

import com.example.springserver.domain.match.dto.response.MatchResponseDto;
import com.example.springserver.domain.match.dto.response.MatchResponseDto.*;

import com.example.springserver.domain.match.dto.response.MatchResponseDto.RequestsListRes;
import java.util.List;

public class MatchConverter {

    // 날짜를 포맷하는 메서드

    public static MatchedListRes toMatchedListRes(List<MatchedStatus> list) {
        return MatchedListRes.builder().list(list).build();
    }

    public static RequestsListRes toRequestListRes(List<MatchResponseDto.WorkRequest> list) {
        return RequestsListRes.builder()
                .list(list)
                .build();
    }

    public static MatchRecommendList toRecommendList(List<MatchedCaregiver> list) {
        return MatchRecommendList.builder().list(list).build();
    }
}
