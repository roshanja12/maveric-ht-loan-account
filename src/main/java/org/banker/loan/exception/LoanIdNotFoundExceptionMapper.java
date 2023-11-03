package org.banker.loan.exception;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
public class LoanIdNotFoundExceptionMapper implements ExceptionMapper<LoanIdNotFoundException>{

    @Override
    public Response toResponse(LoanIdNotFoundException exception) {
        return  Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .status(404)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
