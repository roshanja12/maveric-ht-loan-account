package org.banker.loan.proxylayer;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@EqualsAndHashCode
@Setter
@ToString
public class RestClientErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorId;
    private List<ErrorMessage> errors;
    private String status = "FAILED_MSG";
    private String message;
    private Integer code = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    private String path = "SAVING_ACCOUNTS_URL_PATH";
    private Instant timeStamp = Instant.now();
    private Object data;

    public RestClientErrorResponse(String errorId, ErrorMessage errorMessage, Integer code ) {
        this.errorId = errorId;
        this.errors = List.of(errorMessage);
        this.code=code;
    }

    public RestClientErrorResponse(String errorId, ErrorMessage errorMessage) {
        this(errorId,errorMessage, HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
    }

    public RestClientErrorResponse(ErrorMessage errorMessage) {
        this(null, errorMessage);
    }

    public RestClientErrorResponse(List<ErrorMessage> errors) {
        this.errorId = null;
        this.errors = errors;
    }

    public RestClientErrorResponse() {
    }

    @Getter
    @EqualsAndHashCode
    public static class ErrorMessage {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String path = "SAVING_ACCOUNTS_URL_PATH";
        private String message;

        public ErrorMessage(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public ErrorMessage(String message) {
            this.path = null;
            this.message = message;
        }

        public ErrorMessage() {
        }
    }

}

