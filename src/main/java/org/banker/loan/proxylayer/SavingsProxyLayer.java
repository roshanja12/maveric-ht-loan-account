package org.banker.loan.proxylayer;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.banker.loan.models.TransactionRequestDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "saving.Layer")
public interface SavingsProxyLayer {

    @PUT
    @Path("/api/v1/accounts/saving/deposits")
 public Boolean getTransactionHistories(TransactionRequestDto transactionRequestDto);
}
