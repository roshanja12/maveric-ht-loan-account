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

@Provider
public class LoanIdNotFoundExceptionMapper implements ExceptionMapper<LoanIdNotFoundException>{
    @Inject
    ResponseGenerator response;

    @Context
    private UriInfo uriInfoo;
    @Override
    public Response toResponse(LoanIdNotFoundException exception) {
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
