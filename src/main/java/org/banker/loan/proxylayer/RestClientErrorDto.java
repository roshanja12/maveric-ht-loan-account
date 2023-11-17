package org.banker.loan.proxylayer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestClientErrorDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
     String path = "/api/v1/accounts/saving";
     String message;

    public RestClientErrorDto(String path, String message) {
        this.path = path;
        this.message = message;
    }

    public RestClientErrorDto(String message) {
        this.path = null;
        this.message = message;
    }
}
