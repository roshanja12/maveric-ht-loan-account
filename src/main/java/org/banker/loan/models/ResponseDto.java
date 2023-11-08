package org.banker.loan.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
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
