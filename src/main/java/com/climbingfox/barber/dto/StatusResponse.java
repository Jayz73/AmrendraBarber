package com.climbingfox.barber.dto;

import com.climbingfox.barber.entity.Chair;
import com.climbingfox.barber.entity.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class StatusResponse {
    private Chair chair;
    private String customerUUID;
    private EventType eventType;
}
