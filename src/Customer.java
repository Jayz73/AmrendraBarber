package com.climbingfox.amrendrabarber.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {
    private Long token;

    private String name;
    private String email;
    private boolean currentlyProcessing;
    private LocalTime estimatedWaitingTime;
    private Chair chair;
}