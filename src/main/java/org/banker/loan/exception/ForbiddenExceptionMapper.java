package org.banker.loan.exception;

import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException exception) {
        ErrorDto errors = new ErrorDto();
        String errorMessage = "";
        exception.printStackTrace();
        errors.setErrorMessgae("Unauthorized Access Type");
        errors.setErrorCode("403");
        return Response.status(Integer.parseInt(errors.getErrorCode())).entity(errors).build();
    }
}