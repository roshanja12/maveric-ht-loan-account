package org.banker.loan.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class NoDataExceptionMapper implements ExceptionMapper<NoDataException> {
    @Override
    public Response toResponse(NoDataException exception) {
        return  Response.status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .status(204)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
