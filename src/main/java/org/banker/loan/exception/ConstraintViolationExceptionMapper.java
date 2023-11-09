package org.banker.loan.exception;


import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.modelmapper.spi.ErrorMessage;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
            ErrorDto errors = new ErrorDto();
            String errorMessage="";
            for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errorMessage = violation.getMessage()+","+errorMessage;
        }
            errors.setErrorMessgae(errorMessage);
            errors.setErrorCode("500");
            errors.setHttpStatus(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
           return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errors).build();
    }
}
