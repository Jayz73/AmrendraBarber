package com.climbingfox.barber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class APIException extends Exception implements Serializable {
    private final transient ErrorResponse errorResponse;
}
