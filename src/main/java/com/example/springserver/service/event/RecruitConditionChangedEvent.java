package com.example.springserver.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RecruitConditionChangedEvent extends ApplicationEvent {
    private final Long recruitConditionId;

    public RecruitConditionChangedEvent(Object source, Long recruitConditionId) {
        super(source);
        this.recruitConditionId = recruitConditionId;
    }
}
