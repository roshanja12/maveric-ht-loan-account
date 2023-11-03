package org.banker.loan.exception;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException exception) {
        ErrorDto errors = new ErrorDto();
        String errorMessage = "";
        System.out.println(exception);
        errors.setErrorMessgae(errorMessage);
        errors.setErrorCode("401");
        return Response.status(Integer.parseInt(errors.getErrorCode())).entity(errors).build();
    }
}
