package com.climbingfox.barber.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {
    private String token;
    private String name;

    @EqualsAndHashCode.Include
    private String email;

    private boolean currentlyProcessing;
    private long estimatedWaitingTime;
    private Date inTime;
    private int seqNo;
    private Chair chair;
}
