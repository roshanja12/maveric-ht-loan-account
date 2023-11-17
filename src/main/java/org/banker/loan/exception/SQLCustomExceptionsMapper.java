package org.banker.loan.exception;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.utils.ResponseGenerator;
import org.jboss.logging.Logger;

@Provider
public class SQLCustomExceptionsMapper implements ExceptionMapper<SQLCustomExceptions> {
    @Inject
    ResponseGenerator response;
    @Inject
    Logger log;
    @Override
    public Response toResponse(SQLCustomExceptions ex) {

        ErrorDto errors = new ErrorDto();
        String messageCode = ex.getMessage();
        if (messageCode.contains("#")) {
            errors.setErrorMessgae(messageCode.split("##")[0]);
            errors.setErrorCode(messageCode.split("##")[1]);
            log.error("Error|"+ex.getMessage());
            ResponseDto responseDto= response.errorResponseGenerator(Integer.parseInt(errors.getErrorCode()),errors.getErrorMessgae(),errors,null);
            return Response.status(Integer.parseInt(errors.getErrorCode())).entity(responseDto).build();
        } else {
            errors.setErrorMessgae(messageCode);
            errors.setErrorCode("500");
            log.error("Error|"+ex.getMessage());
            ResponseDto responseDto= response.errorResponseGenerator(Integer.parseInt(errors.getErrorCode()),errors.getErrorMessgae(),errors,null);
            return Response.status(Integer.parseInt(errors.getErrorCode())).entity(responseDto).build();
        }
    }

}

