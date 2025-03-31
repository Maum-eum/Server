package com.example.springserver.service.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JobConditionChangedEvent extends ApplicationEvent {
    private final Long jobConditionId;

    public JobConditionChangedEvent(Object source, Long jobConditionId) {
        super(source);
        this.jobConditionId = jobConditionId;
    }
}
