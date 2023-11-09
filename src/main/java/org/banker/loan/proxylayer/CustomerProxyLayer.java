package org.banker.loan.proxylayer;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.banker.loan.exception.ErrorCodes;
import org.banker.loan.exception.ServiceException;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "customer.Layer")
public interface CustomerProxyLayer {

    @GET
    @Path("/api/v1/customers/searchByCustomerId")
    public Response getCustomerByCriteria(@QueryParam("customerId") String searchValue) ;
    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == 500) {
            return new ServiceException(ErrorCodes.SERVICE_CONNECTION_ISSUE+ " in Customer Service");
        }
        if (response.getStatus() == 404) {
            return new ServiceException(ErrorCodes.NO_CUSTOMER_FOUND);
        }
        return null;
    }
}
