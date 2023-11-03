package org.banker.loan.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SQLCustomExceptionsMapper implements ExceptionMapper<SQLCustomExceptions> {
    @Override
    public Response toResponse(SQLCustomExceptions ex) {

        System.out.println("---------------###");
        ErrorDto errors = new ErrorDto();
        String messgaeCode = ex.getMessage();
        if (messgaeCode.contains("#")) {
            errors.setErrorMessgae(messgaeCode.split("##")[0]);
            errors.setErrorCode(messgaeCode.split("##")[1]);
            ex.getStackTrace();
            return Response.status(Integer.parseInt(errors.getErrorCode())).entity(errors).build();
        } else {
            errors.setErrorMessgae(messgaeCode);
            errors.setErrorCode("500");
            return Response.status(Integer.parseInt(errors.getErrorCode())).entity(errors).build();
        }
    }

}

