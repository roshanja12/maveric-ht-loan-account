package org.banker.loan.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;
import org.banker.loan.exception.ErrorDto;
import org.banker.loan.models.ResponseDto;

import java.time.LocalDateTime;

@ApplicationScoped
public class ResponseGenerator {

    public ResponseDto successResponseGenerator(String message, Object data, UriInfo uriInfo){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(200);
        responseDto.setMsg(message);
        responseDto.setStatus("Success");
        responseDto.setPath(uriInfo.getPath());
        responseDto.setTimestamp(LocalDateTime.now());
        responseDto.setData(data);
        responseDto.setErrors(null);
        return responseDto;
    }
    public ResponseDto errorResponseGenerator(int statusCode,String message, ErrorDto data, UriInfo uriInfo){
        ResponseDto responseDto = new ResponseDto();
        responseDto.setCode(statusCode);
        responseDto.setMsg(message);
        responseDto.setStatus("Error");
        responseDto.setPath(uriInfo.getPath());
        responseDto.setTimestamp(LocalDateTime.now());
        responseDto.setData(null);
        responseDto.setErrors(data);
        return responseDto;
    }

}
