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
    ResponseGenerator response;
    @Context
    private UriInfo uriInfoo;
    @Override
    public Response toResponse(LoanException exception) {
        ErrorDto errors = new ErrorDto();
        errors.setErrorMessgae(exception.getMessage());
        errors.setErrorCode("404");
        ResponseDto responseDto= response.errorResponseGenerator(404,exception.getMessage(),errors,uriInfoo);

        return  Response.status(Response.Status.NOT_FOUND)
                .entity(responseDto)
                .status(404)
                .build();
    }
}
