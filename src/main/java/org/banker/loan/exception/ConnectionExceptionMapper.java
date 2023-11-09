package org.banker.loan.exception;

import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.utils.ResponseGenerator;
import org.jboss.logging.Logger;

@Provider
public class ConnectionExceptionMapper implements ExceptionMapper<ProcessingException> {
    @Inject
    ResponseGenerator response;
    @Inject
    Logger log;
    @Context
    private UriInfo uriInfoo;
    @Override
    public Response toResponse(ProcessingException ex) {
        ErrorDto errors = new ErrorDto();
        String messageCode = ex.getMessage();
        errors.setErrorMessgae(messageCode);
        errors.setErrorCode("500");
        log.error(ex.getStackTrace());
        ResponseDto responseDto= response.errorResponseGenerator(Integer.parseInt(errors.getErrorCode()),errors.getErrorMessgae(),errors,uriInfoo);
        return Response.status(Integer.parseInt(errors.getErrorCode())).entity(responseDto).build();
    }
}
