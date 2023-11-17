package org.banker.loan.exception;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.utils.ResponseGenerator;
import org.jboss.logging.Logger;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {
    @Inject
    Logger log;
    @Inject
    ResponseGenerator response;

    @Context
    private UriInfo uriInfo;
    @Override
    public Response toResponse(ServiceException ex) {
        ErrorDto errors = new ErrorDto();
        String messgaeCode = ex.getMessage();
        if (messgaeCode.contains("##")) {
            errors.setErrorMessgae(messgaeCode.split("##")[0]);
            errors.setErrorCode(messgaeCode.split("##")[1]);
            log.error("Error Message|"+errors.getErrorMessgae());
            ResponseDto responseDto= response.errorResponseGenerator(Integer.parseInt(errors.getErrorCode()),errors.getErrorMessgae(),errors,uriInfo);
            return Response.status(Integer.parseInt(errors.getErrorCode())).entity(responseDto).build();
        } else {
            errors.setErrorMessgae(messgaeCode);
            errors.setErrorCode("500");
            log.error("Error Message|"+errors.getErrorMessgae());
            ResponseDto responseDto= response.errorResponseGenerator(Integer.parseInt(errors.getErrorCode()),errors.getErrorMessgae(),errors,uriInfo);
            return Response.status(Integer.parseInt(errors.getErrorCode())).entity(responseDto).build();
        }
    }
}
