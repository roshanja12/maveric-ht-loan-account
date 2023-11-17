package org.banker.loan.proxylayer;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.banker.loan.exception.ServiceException;
import org.banker.loan.models.TransactionRequestDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.modelmapper.ModelMapper;

@RegisterRestClient(configKey = "saving.Layer")
public interface SavingsProxyLayer {

    @PUT
    @Path("/api/v1/accounts/saving/withdraws")
 public Response getTransactionHistories(TransactionRequestDto transactionRequestDto);

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == 500) {
            ModelMapper modelMapper = new ModelMapper();
            RestClientErrorResponse responsedto= response.readEntity(RestClientErrorResponse.class);
            RestClientErrorDto errorDto =modelMapper.map(responsedto.getErrors().get(0), RestClientErrorDto.class);
            return new ServiceException(errorDto.getMessage()+"##"+500);
        }
        if (response.getStatus() == 404) {
            ModelMapper modelMapper = new ModelMapper();
            RestClientErrorResponse responsedto= response.readEntity(RestClientErrorResponse.class);
            RestClientErrorDto errorDto =modelMapper.map(responsedto.getErrors().get(0), RestClientErrorDto.class);
            return new ServiceException(errorDto.getMessage()+"##"+404);
        }
        if (response.getStatus() == 400) {
            ModelMapper modelMapper = new ModelMapper();
            RestClientErrorResponse responsedto= response.readEntity(RestClientErrorResponse.class);
            RestClientErrorDto errorDto =modelMapper.map(responsedto.getErrors().get(0), RestClientErrorDto.class);
            return new ServiceException(errorDto.getMessage()+"##"+400);
        }
        return null;
    }
}
