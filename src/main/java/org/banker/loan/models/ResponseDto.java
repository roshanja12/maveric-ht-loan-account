package org.banker.loan.models;

import lombok.Data;
import org.banker.loan.exception.ErrorDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseDto {
    private String status;
    private String msg;
    private int code;
    private ErrorDto errors;
    private Object data;
    private String path;
    private LocalDateTime timestamp;
}
