package org.banker.loan.proxylayer;

import lombok.Data;
import org.banker.loan.exception.ErrorDto;

import java.time.LocalDateTime;

@Data
public class RestClientListOfResponse {
    private String status;
    private String msg;
    private int code;
    private ErrorDto errors [];
    private Object data[];
    private String path;
    private LocalDateTime timestamp;
}
