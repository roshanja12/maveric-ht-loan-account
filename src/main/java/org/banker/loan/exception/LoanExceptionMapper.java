package org.banker.loan.exception;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.utils.ResponseGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LoanExceptionMapper implements ExceptionMapper<LoanException> {
    @Inject
    ResponseGenerator responseGenerator;
    @Context
    private UriInfo uriInfoo;
    @Override
    public Response toResponse(LoanException exception) {
        ErrorDto errors = new ErrorDto();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Failed");
        response.put("code", Response.Status.NOT_FOUND.getStatusCode());
        response.put("msg", "Data not found");
        response.put("errors",exception.getMessage());
        response.put("data", null);
        response.put("path", "/bankersApp/v1/loan");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timestamp = dateFormat.format(new Date());
        response.put("timestamp", timestamp);
        errors.setErrorMessgae(exception.getMessage());
        errors.setErrorCode("404");
        ResponseDto responseDto= responseGenerator.errorResponseGenerator(404,"Data not found",errors,uriInfoo);
        return Response.status(Response.Status.NOT_FOUND)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
