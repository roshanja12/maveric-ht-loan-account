package org.banker.loan.proxylayer;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.banker.loan.exception.ErrorCodes;
import org.banker.loan.exception.ServiceException;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "account.Layer")
public interface AccountProxyLayer {

    @GET
    @Path("/api/v1/accounts/saving/{accountId}")
    public Response getSavingAccountDetailBasedOnAccountId(Long accountId);

    @ClientExceptionMapper
    static RuntimeException toException(Response response) {
        if (response.getStatus() == 500) {
            return new ServiceException(ErrorCodes.CONNECTION_ISSUE);
        }
        if (response.getStatus() == 404) {
            return new ServiceException(ErrorCodes.NO_SAVINGS_ACCOUNT_FOUND);
        }
        if (response.getStatus() == 400) {
            return new ServiceException(ErrorCodes.NO_SAVINGS_ACCOUNT_FOUND);
        }
        return null;
    }
}
