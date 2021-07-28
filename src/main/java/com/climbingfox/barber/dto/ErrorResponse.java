package com.climbingfox.barber.dto;

import com.climbingfox.barber.entity.ErrorLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
@NoArgsConstructor
public class ErrorResponse implements Serializable {
    private ErrorLevel errorLevel;
    private List<ErrorDetail> errorDetails = new CopyOnWriteArrayList<>();

    @Data
    @AllArgsConstructor
    public static class ErrorDetail implements Serializable{
        private String errorCode;
        private String errorMsg;
    }
}
